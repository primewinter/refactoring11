package com.model2.mvc.web.basket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
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
import com.model2.mvc.common.util.IPUtil;
import com.model2.mvc.service.basket.BasketService;
import com.model2.mvc.service.domain.Basket;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;


//==> 噺据淫軒 Controller
@Controller
@RequestMapping("/basket/*")
public class BasketController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	@Autowired
	@Qualifier("basketServiceImpl")
	private BasketService basketService;
	//setter Method 姥薄 省製
		
	public BasketController(){
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
	
	
	@RequestMapping( value="addBasket" )
	public String addBasket( @ModelAttribute("basket") Basket basket, @ModelAttribute("user") User sessionUser, @RequestParam("prodNo")int prodNo, HttpServletRequest request, HttpSession session) throws Exception {

		System.out.println("/addBasket.do");
		//Business Logic
		//Basket basket = new Basket();
		User user = new User();
		String visitor = null;
		Cookie[] cookies = request.getCookies();
		if(session.getAttribute("user") != null ) { //稽益昔 雌殿虞檎 userId亜 娃陥.
			user = (User)session.getAttribute("user");
			visitor = user.getUserId();
			System.out.println("稽益昔 雌殿虞檎 置曽 政煽澗 焼戚巨嬢醤 原競馬走... -->"+visitor);
		} else { //稽益焼数 雌殿虞檎 ip爽社亜 娃陥.
				if (cookies!=null && cookies.length > 0) {
					for (int i = 0; i < cookies.length; i++) {
						Cookie cookie = cookies[i];
						System.out.println("cookie 食君鯵 掻 廃鯵 : "+cookie.getName());
						if (cookie.getName().equals("visitors")) { //仙号庚切税 庭徹澗?!
							visitor = cookie.getValue();
							user.setUserId(visitor);
							System.out.println("搾稽益昔 + visitors 握壱 赤澗 切 "+user.getUserId());
						} else {
							visitor = IPUtil.getClientIP(request);
							user.setUserId(visitor);
							System.out.println("陣走澗 乞牽畏走幻.. IP 袷 握壱 人忽績;"+visitor);
						}
					}
				}
		}
		System.out.println("置曽 政煽澗?? "+visitor);
		basket.setVisitor(user);
		Product prod = productService.getProduct(prodNo);
		basket.setProduct(prod);
		System.out.println("督寓生稽 閤焼紳 basket税 quantity 澗? "+basket.getQuantity());
		basketService.addBasket(basket);
		return "forward:/product/getProduct";
	}
	
	
	@RequestMapping(value="listBasket")
	public String listBasket( @ModelAttribute("search") Search search , Model model , HttpSession session, HttpServletRequest request) throws Exception{
		System.out.println("醤醤醤醤醤醤醤ちし:");
		System.out.println("巨獄焔!!! : "+search.getCurrentPage());
		System.out.println("/listBasket.do");
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		System.out.println("[pageSize] : "+pageSize);
		// Business logic 呪楳
		User userVO = new User();
		
		// 庭徹 掻拭辞 visitors庭徹 握壱神奄
		Cookie[] cookies = request.getCookies();
		List<String> visitor = new ArrayList<String>();
		if (cookies!=null && cookies.length > 0) { // 紳握 庭徹級 掻拭辞...
		
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals("visitors")) { // 仙号庚切虞檎 ip爽社 握壱紳陥
					visitor.add(cookie.getValue());
					System.out.println("仙号庚切亜 visitors 握壱 尽嬢遂");
				}
			}
			
			if ( visitor.size() == 0 ) { //湛号庚切
				if(session.getAttribute("user") != null ) { //噺据戚 陥献 陳濃斗稽 級嬢尽陥檎 userId, ip爽社 隔奄
					userVO = (User)session.getAttribute("user");
					visitor.add(userVO.getUserId());
					System.out.println("噺据戚 陥献 陳濃斗稽 級嬢尽陥.");
				}
				visitor.add(IPUtil.getClientIP(request));
				
			} else { //仙号庚切
				System.out.println("仙号庚切掻拭...");
				if(session.getAttribute("user") != null) { //仙号庚切 + 稽益昔 雌殿虞檎 userId隔奄
					System.out.println("仙号庚切 + 稽益昔 雌殿陥");
					userVO = (User)session.getAttribute("user");
					visitor.add(userVO.getUserId());
				}
			}
			
			
		}
		System.out.println(":::::::db拭 哀 visior "+visitor);
		
		Map<String , Object> map=basketService.getBasketList(search, visitor);
		System.out.println("[currentPage] : "+search.getCurrentPage());
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		// Model 引 View 尻衣
		model.addAttribute("basket", map);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		System.out.println("Debugging : " +search.getCurrentPage());
		System.out.println(">>>>>>>>>>listBasket : "+map);
		System.out.println("ぞぞぞぞぞぞぞlistBasket 刃戟"+resultPage);
		
		return "forward:/basket/listBasket.jsp";
	}
	
	@RequestMapping(value="deleteBasket")
	public String deleteBasket( @RequestParam("chkBasketNo") List<String> basketNoArr) throws Exception {
		System.out.println("/deleteBasket.do");
		basketService.deleteBasket(basketNoArr);
		
		for(String i : basketNoArr) {
			System.out.println("List拭 眼奄澗亜?" +i);
		}

		return "forward:/basket/listBasket";
	}
	
	@RequestMapping(value="buyBasket")
	public String buyBasket( @RequestParam("chkBasketNo") List<String> basketNoArr, HttpSession session, Model model ) throws Exception {
		System.out.println("/buyBasket.do");
		List<Product> prodList = new ArrayList<Product>();
		
		for(String basketNo : basketNoArr ) {
			System.out.println("舌郊姥艦拭辞 紳 腰硲 : "+ basketNo);
			Basket basket = basketService.getProdNo(Integer.parseInt(basketNo));
			Product product = basket.getProduct();
			product.setQuantity(basket.getQuantity());
			prodList.add(product);
			System.out.println("\t\t\t馬蟹 眼製 "+product);
		}
		
		model.addAttribute("basketNoArr", basketNoArr);
		model.addAttribute("productList", prodList);
		model.addAttribute("user", (User)session.getAttribute("user"));
		System.out.println("addPurchase稽 亜惟掌");
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
}