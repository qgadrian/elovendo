package es.telocompro.rest.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.province.Province;
import es.telocompro.model.user.User;
import es.telocompro.service.item.ItemService;
import es.telocompro.service.item.category.CategoryService;
import es.telocompro.service.province.ProvinceService;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */

@Controller
public class MainController {
	
    @Autowired
    private ItemService itemService;
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private CategoryService categoryService;
	
	/** Login **/
    
	@RequestMapping(value="/login")
    public String loginPage() {
    	return "elovendo/login";
    }

    @RequestMapping(value = "/welcome")
    @ResponseStatus(HttpStatus.OK) // TODO Testear esta notación
    public String login(Model model, Device device, Principal principal) {
    	System.out.println("We are in main controller");
    	
    	 User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	 String userId = String.valueOf(user.getUserId());
    	 
    	 model.addAttribute(user);

    	if (device.isNormal())
//    		return principal.getName() + " you are loggued motherfucker!!! and your id is " + userId + " btw";
    		return "welcome";
    	return "hola? " + principal.getName() +"? your id is " + userId + "?";
    }
    
    @RequestMapping(value="/logout")
    public String logout(HttpServletResponse response) {
    	System.out.println("logout controller");
    	
    	SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    	Cookie cookie = new Cookie("JSESSIONID", null);
    	response.addCookie(cookie);
    	
    	return null;
    }
    
//    @RequestMapping(value="/logout")
//    public ResponseEntity<?> logout(HttpServletResponse response) {
//    	System.out.println("logout controller");
//    	
//    	SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
//    	Cookie cookie = new Cookie("JSESSIONID", null);
//    	response.addCookie(cookie);
//    	
//    	return new ResponseEntity<HttpStatus>(HttpStatus.OK);
//    }
    
    /** ERRORS */
    
    @RequestMapping(value="/error404")
    public String error404Page() {
    	return "elovendo/error/error404";
    }
    
    @RequestMapping(value="/error401")
    public String error401Page() {
    	return "elovendo/error/error401";
    }
    
    /** WEB PAGES **/
    
    @RequestMapping(value="/elovendo/index", method = RequestMethod.GET)
    public String indexPage() {
        return "elovendo/index";
    }
    
    @RequestMapping(value="/elovendo/add_user", method = RequestMethod.GET)
    public String addUserPage(Model model) {
    	model.addAttribute("user", new User());
    	model.addAttribute("provinceName", new String());
    	
    	@SuppressWarnings("unchecked")
		List<Province> provinces = IteratorUtils.toList(provinceService.findAllProvinces().iterator());
    	model.addAttribute("provinces", provinces);
    	
        return "elovendo/user/add_user";
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value="/elovendo/add_item", method = RequestMethod.GET)
    public String addItemPage(Model model) {
    	model.addAttribute("item", new Item());
    	model.addAttribute("provinceName", new String());
    	model.addAttribute("categoryName", new Category());
    	model.addAttribute("subCategoryName", new SubCategory());
    	
		List<Province> provinces = IteratorUtils.toList(provinceService.findAllProvinces().iterator());
    	model.addAttribute("provinces", provinces);
    	
    	List<Category> categories = IteratorUtils.toList(categoryService.findAllCategories().iterator());
    	model.addAttribute("categories", categories);
    	List<SubCategory> subCategories = IteratorUtils.toList(categoryService.findAllSubCategories().iterator());
    	model.addAttribute("subCategories", subCategories);
    	
        return "elovendo/item/add_item";
    }
    
    @RequestMapping(value="/elovendo/itemtest", method = RequestMethod.GET)
    public String pageAbout(Model model) {
    	
    	List<Item> items = itemService.getAllItemsBySubCategory("Móviles", 0, 0, 0, 5).getContent();
    	model.addAttribute("items", items);
    	
        return "elovendo/left-sidebar";
//    	return "elovendo/itemListTest";
    }

}
