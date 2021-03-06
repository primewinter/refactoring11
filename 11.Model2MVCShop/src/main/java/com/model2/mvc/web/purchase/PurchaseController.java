package com.model2.mvc.web.purchase;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.basket.BasketService;
import com.model2.mvc.service.domain.DeliveryInfo;
import com.model2.mvc.service.domain.Payment;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;


//==> 噺据淫軒 Controller
@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	@Autowired
	@Qualifier("basketServiceImpl")
	private BasketService basketService;
	//setter Method 姥薄 省製
		
	public PurchaseController(){
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
	
	
	@RequestMapping( value="addPurchase", method=RequestMethod.POST )
	public String addPurchase(@RequestParam("prodNo") List<Integer> prodNo, @ModelAttribute("deliveryInfo") DeliveryInfo dlvyInfo, Model model,
												@ModelAttribute("payment") Payment payment, HttpSession session,@RequestParam(value="quantity") List<Integer> quantity,
												@RequestParam(value="basketNo", required=false) List<String> basketNoArr) throws Exception {

		System.out.println("/addPurchase.do");
		//Business Logic
		Purchase purchase = new Purchase();
		
		List<Product> prod = new ArrayList<Product>();
		
		for(int i = 0; i <prodNo.size(); i++ ) {
			Product purchaseProd = productService.getProduct(prodNo.get(i));
			purchaseProd.setQuantity(quantity.get(i));
			prod.add(purchaseProd);
		}
		User user = (User)session.getAttribute("user");
		purchase.setBuyer(user);
		purchase.setDlvyInfo(dlvyInfo);
		purchase.setPayment(payment);
		System.out.println("::: payment 舛左 : "+payment);
		purchase.setPurchaseProd(prod);
		purchase.setTranCode("爽庚刃戟");
		int tranNo = purchaseService.addPurchase(purchase);
		purchase.setTranNo(tranNo);
		System.out.println("/addPurchase 刃戟::: tranNo = "+tranNo);
		
		if(basketNoArr != null) {
			for(int i=0; i<basketNoArr.size(); i++) {
				System.out.println("舌郊姥艦 角嬢尽嬢推 "+basketNoArr.get(i));
			}
			basketService.deleteBasket(basketNoArr);
		}
		
		session.setAttribute("user", userService.getUser(user.getUserId()));
		model.addAttribute("purchase", purchase);
		model.addAttribute("purchaseProd", prod);
		model.addAttribute("payment", payment);
		model.addAttribute("dlvyInfo", dlvyInfo);
		
		return "forward:/purchase/addPurchase.jsp";
	}
	
	@RequestMapping( value = "addPurchase", method=RequestMethod.GET )
	public String addPurchase( @RequestParam(value="prodNo", required=false) List<Integer> prodNo, @RequestParam(value="quantity", required=false) int quantity, HttpSession session, Model model ) throws Exception{

		System.out.println("/addPurchaseView.do");

		List<Product> prodList = new ArrayList<Product>();
		for(int prod : prodNo) {
			Product product =productService.getProduct(prod);
			product.setQuantity(quantity);
			prodList.add(product);
		}
		
		
		model.addAttribute("productList", prodList);
		model.addAttribute("user", (User)session.getAttribute("user"));
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
	@RequestMapping( value ="getPurchase", method=RequestMethod.GET )
	public String getPurchase( @RequestParam("tranNo") int tranNo , Model model ) throws Exception {
		
		System.out.println("/getPurchase.do");
		System.out.println("左壱 粛精 暗掘 腰硲 : "+tranNo);
		//Business Logic
		Purchase purchase = purchaseService.getPurchase(tranNo);
		// Model 引 View 尻衣
		model.addAttribute("purchase", purchase);
		
		return  "forward:/purchase/getPurchase.jsp";
	}
	
	@RequestMapping( value = "getSale", method=RequestMethod.GET )
	public String getSale( @RequestParam("tranNo") int tranNo , Model model ) throws Exception {
		
		System.out.println("/getSale.do");
		System.out.println("左壱 粛精 暗掘 腰硲 : "+tranNo);
		//Business Logic
		Purchase purchase = purchaseService.getPurchase(tranNo);
		// Model 引 View 尻衣
		model.addAttribute("purchase", purchase);
		
		return  "forward:/purchase/getSale.jsp";
	}
	
	@RequestMapping( value = "updatePurchase", method=RequestMethod.GET )
	public String updatePurchase( @RequestParam("tranNo") int tranNo, Model model ) throws Exception{

		System.out.println("/updatePurchaseView.do");
		//Business Logic
		// Model 引 View 尻衣
		Purchase purchase = purchaseService.getPurchase(tranNo);
		model.addAttribute("purchase", purchase);
		System.out.println("引尻 purchase税 payment拭 叔形 神澗亜!? "+purchase.getPayment());
		
		return "forward:/purchase/updatePurchaseView.jsp";
	}
	
	@RequestMapping( value= "updatePurchase", method=RequestMethod.POST)
	public String updatePurchase( @ModelAttribute("dlvyInfo") DeliveryInfo dlvyInfo, @RequestParam("tranNo") int tranNo, Model model) throws Exception{

		System.out.println("/updatePurchase.do");
		//Business Logic
		System.out.println("閤焼神澗亜??! dlvyInfo"+dlvyInfo);
		Purchase purchase = new Purchase();
		purchase.setDlvyInfo(dlvyInfo);
		purchase.setTranNo(tranNo);
		purchaseService.updatePurchase(purchase);
		
		return  "redirect:/purchase/getPurchase?tranNo="+tranNo;
	}
	
	
	
	@RequestMapping( value="listSale")
	public String listSale( @ModelAttribute("search") Search search, Model model) throws Exception{
		
		System.out.println("/listSale.do");
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 呪楳
		Map<String , Object> map = purchaseService.getSaleList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		// Model 引 View 尻衣
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		System.out.println("ぞぞぞぞぞぞぞlistPurchase 刃戟"+resultPage);
		
		return "forward:/purchase/listSale.jsp?";
	}
	
	@RequestMapping(value="listPurchase")
	public String listPurchase( @ModelAttribute("search") Search search, HttpSession session, Model model) throws Exception{
		
		System.out.println("/listPurchase.do");
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 呪楳
		User user = (User)session.getAttribute("user");
		Map<String , Object> map = purchaseService.getPurchaseList(search, user.getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		// Model 引 View 尻衣
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		System.out.println("ぞぞぞぞぞぞぞlistPurchase 刃戟"+resultPage);
		
		return "forward:/purchase/listPurchase.jsp?";
	}
	
	@RequestMapping(value="listCancelPurchase")
	public String listCancelPurchase( @ModelAttribute("search") Search search, HttpSession session, Model model) throws Exception{
		
		System.out.println("/listCancelPurchase.do");
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 呪楳
		User user = (User)session.getAttribute("user");
		Map<String , Object> map = purchaseService.getCancelPurchaseList(search, user.getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		// Model 引 View 尻衣
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		System.out.println("ぞぞぞぞぞぞぞlistPurchase 刃戟"+resultPage);
		
		return "forward:/purchase/listCancelPurchase.jsp";
	}
	
	@RequestMapping(value="updateTranCode")
	public String updateTranCode( @RequestParam("list") String list, @RequestParam("tranNo") String tNum, @ModelAttribute("search") Search search, HttpServletRequest request, HttpSession session,  Model model) throws Exception{

		System.out.println("/updateTranCode.do");
		//Business Logic
		Purchase purchase = new Purchase();
		int tranNo=Integer.parseInt(tNum);
		System.out.println("=========tNum :::"+tNum);
		String tranCode = "";
		if( request.getParameter("list").equals("purchase") ) {
			tranCode = "壕勺刃戟";
			System.out.println("null 戚 焼観 tranCode = "+tranCode);
		} else {
			tranCode =request.getParameter(tNum);
			System.out.println("SaleList拭辞 紳 tranCode : "+tranCode);
		}
		purchase.setTranCode(tranCode);
		System.out.println("穿凪戚走拭辞 握壱紳 tranCode : "+tranCode);
		purchase.setTranNo(tranNo);
		if(tranCode.equals("発災刃戟")) {
			purchase = purchaseService.getPurchase(tranNo);
			purchase.setTranCode(tranCode);
			purchaseService.completeRefund(purchase);
		} else {		
			purchaseService.updateTranCode(purchase);
		}
	
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		System.out.println("五敢 嬢巨拭辞 尽嬢? "+list);
		if(list.equals("purchase") ) {
			User user = (User)session.getAttribute("user");
			Map<String , Object> map = purchaseService.getPurchaseList(search, user.getUserId());
			
			Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
			
			model.addAttribute("list", map.get("list"));
			model.addAttribute("resultPage", resultPage);
			model.addAttribute("search", search);
			System.out.println("壱梓戚嬢辞 listPurchase稽 娃陥!");
			return "forward:/purchase/listPurchase";
		} else {
			
			Map<String , Object> map = purchaseService.getSaleList(search);
			
			Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
			
			// Model 引 View 尻衣
			model.addAttribute("list", map.get("list"));
			model.addAttribute("resultPage", resultPage);
			model.addAttribute("search", search);
			System.out.println("淫軒切食辞 listSale稽 娃陥!");
			 return "forward:/purchase/listSale";
		}
	}
	
	@RequestMapping(value="cancelPurchase")
	public ModelAndView cancelPurchase(@RequestParam("tranNo") int tranNo, HttpSession session, Model model) throws Exception {
		
		System.out.println("::::::爽庚 昼社");
		Purchase purchase = new Purchase();
		purchase = purchaseService.getPurchase(tranNo);
		purchase.setTranCode("爽庚昼社");
		
		User user = userService.getUser(purchase.getBuyer().getUserId());
		session.setAttribute("user", user);
		
		purchaseService.cancelPurchase(purchase);
		
		return new ModelAndView("forward:/purchase/listPurchase");
	}
	
	@RequestMapping(value="requestRefund")
	public ModelAndView requestRefund(@RequestParam("tranNo") int tranNo, Model model ) throws Exception {
		
		System.out.println("::::::::発災 推短");
		Purchase purchase = purchaseService.getPurchase(tranNo);
		purchase.setTranCode("発災 重短");
		purchaseService.requestRefund(purchase);
		
		User user = userService.getUser(purchase.getBuyer().getUserId());
		model.addAttribute("user", user);
		
		return new ModelAndView("forward:/purchase/listPurchase");
	}
	
	
}