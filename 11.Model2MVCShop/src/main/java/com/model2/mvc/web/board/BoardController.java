package com.model2.mvc.web.board;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.board.BoardService;
import com.model2.mvc.service.domain.Board;
import com.model2.mvc.service.domain.User;


@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	@Autowired
	@Qualifier("boardServiceImpl")
	private BoardService boardService;
		
	public BoardController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 凧繕 拝依
	//==> 焼掘税 砧鯵研 爽汐聖 熱嬢 税耕研 溌昔 拝依
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping( value="addBoard" )
	public String addBoard( @ModelAttribute("board") Board board, @ModelAttribute("user") User sessionUser, HttpServletRequest request, HttpSession session) throws Exception {

		System.out.println("/addBoard.do");
		//Business Logic
		System.out.println("board :: "+board);
		System.out.println("拙失切 :: "+sessionUser);
		boardService.addBoard(board);
		return "forward:/board/listBoard";
	}
	
	@RequestMapping( value="getBoard" )
	public String getBoard( @RequestParam("boardNo") int boardNo,  Model model) throws Exception {

		System.out.println("/getBoard.do");
		System.out.println("boardNo :: "+boardNo);
		Board board = boardService.getBoard(boardNo);
		model.addAttribute("board", board);
		return "forward:/board/getBoard.jsp";
	}
	
	
	@RequestMapping(value="listBoard")
	public String listBasket( @ModelAttribute("search") Search search , Model model , HttpSession session, HttpServletRequest request) throws Exception{
		System.out.println("巨獄焔!!! : "+search.getCurrentPage());
		System.out.println("/listBoard.do");
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		System.out.println("[pageSize] : "+pageSize);
		System.out.println("Search :: "+search);
		System.out.println("search.searchCondition :: "+search.getSearchCondition()+"\n\n\n");
		Map<String , Object> map=boardService.getBoardList(search);
		System.out.println("[currentPage] : "+search.getCurrentPage());
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		// Model 引 View 尻衣
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		System.out.println("ぞぞぞぞぞぞぞlistBoard 刃戟"+resultPage);
		
		
		
		
		return "forward:/board/listBoard.jsp";
	}
	
}