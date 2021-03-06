package com.model2.mvc.web.purchase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.DeliveryInfo;
import com.model2.mvc.service.domain.Payment;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;


//==> 噺据淫軒 Controller
@RestController
@RequestMapping("/purchase/*")
public class PurchaseRestController {
	
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
	//setter Method 姥薄 省製
		
	public PurchaseRestController(){
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
	
	
	@RequestMapping( value = "json/addPurchase", method=RequestMethod.POST)
	public Map<String, Object> addProduct( @RequestBody Map<String, Object> map ) throws Exception {

		System.out.println("/json/addProduct : POST");
		ObjectMapper objmapper = new ObjectMapper();
		DeliveryInfo dlvyInfo = objmapper.convertValue(map.get("dlvyInfo"), DeliveryInfo.class);
		Payment payment = objmapper.convertValue(map.get("payment"), Payment.class);
		User user = objmapper.convertValue(map.get("user"), User.class);
		
		int prodNo = (Integer)map.get("prodNo");
		int quantity = (Integer)map.get("quantity");
		
		Purchase purchase = new Purchase();
		Product purchaseProd = productService.getProduct(prodNo);
		purchaseProd.setQuantity(quantity);
		List<Product> prodList = new ArrayList<Product>();
		prodList.add(purchaseProd);
		
		purchase.setBuyer(user);
		purchase.setDlvyInfo(dlvyInfo);
		purchase.setPayment(payment);
		purchase.setPurchaseProd(prodList);
		
		purchaseService.addPurchase(purchase);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("purchase", purchase);
		result.put("user", user);
		result.put("purchaseProd", purchaseProd);
		result.put("payment", payment);
		result.put("dlvyInfo", dlvyInfo);
		
		return result;
	}
	
	@RequestMapping( value = "json/addPurchase", method=RequestMethod.GET )
	public Map<String,Object> addPurchase( @RequestParam("prodNo") int prodNo ) throws Exception{

		System.out.println("/json/addPurchaseView.do");
		//Business Logic
		User user = new User(); // session拭辞 亜閃神走 公背辞 常走稽 食奄辞 歯稽 識情敗;;;
		user.setUserId("user01");
		
		// Model 引 View 尻衣
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("product", productService.getProduct(prodNo));
		map.put("user", userService.getUser(user.getUserId()));
		
		return map;
	}
	
	@RequestMapping( value = "json/getPurchase/{tranNo}", method=RequestMethod.GET)
	public Map<String, Object> getProduct( @PathVariable int tranNo ) throws Exception {
		
		System.out.println("/getProduct.do");
		System.out.println("左壱 粛精 雌念 腰硲 : "+tranNo);
		//Business Logic
		Product product = productService.getProduct(tranNo);
		// Model 引 View 尻衣
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("product", product);
		
		return map;
	}
	
	@RequestMapping( value="json/updatePurchase/{prodNo}", method=RequestMethod.GET)
	public  Map<String, Object> updateProduct( @PathVariable int prodNo ) throws Exception{

		System.out.println("/updateUserView.do");
		//Business Logic
		// Model 引 View 尻衣
		Product product = productService.getProduct(prodNo);
		
		// Model 引 View 尻衣
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("product", product);
		
		return map;
	}
	
	@RequestMapping( value="json/updatePurchase", method=RequestMethod.POST)
	public void updateProduct( @RequestBody Product product ) throws Exception{

		System.out.println("/updateProduct.do");
		//Business Logic
		productService.updateProduct(product);
		
	}
	
	
	
	@RequestMapping( value="json/listPurchase", method=RequestMethod.POST )
	public Map<String, Object> listProduct( @RequestBody Search search ) throws Exception{
		
		System.out.println("/listProduct.do");
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		System.out.println("凪戚走 拡");
		// Business logic 呪楳
		Map<String , Object> map=productService.getProductList(search);
		System.out.println("productService 陥橿身");
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		// Model 引 View 尻衣
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", map.get("list"));
		result.put("resultPage", resultPage);
		result.put("search", search);
		System.out.println("ぞぞぞぞぞぞぞlistProduct 刃戟"+resultPage);
		
		return result;
	}
	
	@RequestMapping( value="json/listPurchase", method=RequestMethod.GET )
	public Map<String, Object> listProduct( ) throws Exception{
		
		System.out.println("/listProduct.do");
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(pageSize);
		System.out.println("凪戚走 拡");
		// Business logic 呪楳
		Map<String , Object> map=productService.getProductList(search);
		System.out.println("productService 陥橿身");
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		// Model 引 View 尻衣
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", map.get("list"));
		result.put("resultPage", resultPage);
		result.put("search", search);
		System.out.println("ぞぞぞぞぞぞぞlistProduct 刃戟"+resultPage);
		
		return result;
	}
	
	
	
}