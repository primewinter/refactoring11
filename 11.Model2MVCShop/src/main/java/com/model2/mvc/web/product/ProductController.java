package com.model2.mvc.web.product;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


//==> 噺据淫軒 Controller
@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 姥薄 省製
		
	public ProductController(){
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
	
	
	@RequestMapping( value = "addProduct" )
	public String addProduct( @ModelAttribute("product") Product product, @RequestParam(required = false)MultipartFile[] file) throws Exception {

		System.out.println("/addProduct.do");
		System.out.println("@modelAttribute PRODUCT : "+product);
		System.out.println("@requestParam file : "+file);
		
		// 角嬢紳 督析 虹希拭 煽舌
		if( file != null ) {
			String saveName = "";
			for (int i = 0 ; i <file.length ; i++) {
				saveName += saveFile(file[i])+",";
			}
			System.out.println("雌念税 戚耕走 督析 : "+saveName);
			product.setFileName(saveName);
		} else {
			product.setFileName("monsterball.png");
		}
		
		productService.addProduct(product);
		
		return "forward:/product/addProduct.jsp";
	}
	
	@RequestMapping( value = "getProduct", method=RequestMethod.GET)
	public String getProduct( @RequestParam("prodNo") int prodNo , HttpServletRequest request, Model model ) throws Exception {
		
		System.out.println("/getProduct.do");
		System.out.println("左壱 粛精 雌念 腰硲 : "+prodNo);
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model 引 View 尻衣
		model.addAttribute("product", product);
		
		return "forward:/product/getProduct.jsp";
	}
	
	@RequestMapping( value="updateProduct", method=RequestMethod.GET)
	public String updateProduct( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{

		System.out.println("/updateUserView.do");
		//Business Logic
		// Model 引 View 尻衣
		model.addAttribute("product", productService.getProduct(prodNo));
		
		return "forward:/product/updateProduct.jsp";
	}
	
	@RequestMapping( value="updateProduct", method=RequestMethod.POST)
	public String updateProduct( @ModelAttribute("product") Product product , @RequestParam(required = false)MultipartFile[] file, Model model , HttpSession session) throws Exception{

		System.out.println("/updateProduct.do");
		//Business Logic
		if( file != null && file.length != 0 ) {
			String saveName = "";
			for (int i = 0 ; i <file.length ; i++) {
				saveName += saveFile(file[i])+",";
			}
			System.out.println("雌念税 戚耕走 督析 : "+saveName);
			product.setFileName(saveName);
		} else {
			product.setFileName("monsterball.png");
		}
		productService.updateProduct(product);
		
		return "redirect:/product/getProduct?prodNo="+product.getProdNo();
	}
	
	
	
	@RequestMapping( value="listProduct" )
	public String listProduct( @ModelAttribute("search") Search search , Model model , HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		System.out.println("/listProduct.do");
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 呪楳
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		// Model 引 View 尻衣
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		System.out.println("ぞぞぞぞぞぞぞlistProduct 刃戟"+resultPage);
		
		return "forward:/product/listProduct.jsp?";
	}
	
	@RequestMapping( value="deleteProduct" )
	public String deleteProduct( @ModelAttribute("search") Search search, @RequestParam("chk_prodNo") String[] prodNoArr, Model model) throws Exception {
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String , Object> map=productService.getProductList(search);
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);

		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		productService.deleteProduct(prodNoArr);
		
		return "forward:/product/listProduct";
	}
	
	//穣稽球廃 督析 煽舌拝 箭企井稽
	private static final String UPLOAD_PATH = "C:\\Users\\USER\\git\\repository3\\11.Model2MVCShop\\WebContent\\images\\uploadFiles";
	
	//MultipartFile稽 閤焼辞 督析 煽舌馬壱 戚硯 蓄窒馬澗 五社球
	private String saveFile(MultipartFile file){
	    // 督析 戚硯 痕井
	    String saveName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

	    // 煽舌拝 File 梓端研 持失(荻汽奄 督析)い
	    File saveFile = new File(UPLOAD_PATH,saveName); // 煽舌拝 虹希 戚硯, 煽舌拝 督析 戚硯

	    try {
	        file.transferTo(saveFile); // 穣稽球 督析拭 saveFile戚虞澗 荻汽奄 脊毘
	    } catch ( Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	    
	    return saveName;
	}
}