package es.telocompro.rest.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
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
import es.telocompro.service.item.ItemService;
import es.telocompro.service.item.category.CategoryService;
import es.telocompro.service.province.ProvinceService;
import es.telocompro.util.Constant;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */

@Controller
public class MainController implements ErrorController {
	
    @Autowired
    private ItemService itemService;
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private CategoryService categoryService;
    
    //TODO: Testing
    static Logger logger = Logger.getLogger(MainController.class.getName());
	
	/**
	 * 
	 * USER SESSION MANAGEMENT
	 * 
	 */
    
	@RequestMapping(value="/login")
    public String loginPage(HttpSession session, HttpServletRequest request, Device device) {

		if (device.isNormal()) {
			String referrer = request.getParameter("referrer");
			if (referrer != null) 
				session.setAttribute("referrer", referrer);
			
	    	return "elovendo/login";
		}
		
		return "";
    }

	@RequestMapping(value = "/loginRedirect")
	@ResponseStatus(HttpStatus.OK)
	// TODO Testear esta notación
	public String login(Model model, Device device, Principal principal,
			HttpSession session) {

		if (device.isNormal()) {
			String referrer = (String) session.getAttribute("referrer");
			// logger.debug("Referrer is " + referrer);
			if (referrer != null)
				return "redirect:" + referrer;
			else
				return "redirect:elovendo/index";
		} 
		
		return "";
    	
//    	 User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    	 String userId = String.valueOf(user.getUserId());
//    	 
//    	 model.addAttribute(user);
//
//    	if (device.isNormal())
////    		return principal.getName() + " you are loggued motherfucker!!! and your id is " + userId + " btw";
//    		return "welcome";
//    	return "hola? " + principal.getName() +"? your id is " + userId + "?";
    }
    
    @RequestMapping(value="/logout")
    public String logout(HttpServletResponse response, Device device) {
    	System.out.println("logout controller");
    	
    	SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    	Cookie cookie = new Cookie("JSESSIONID", null);
    	response.addCookie(cookie);
    	
    	if (device.isNormal()) return "elovendo/index";
    	
    	return "";
    }
    
    /** 
     * 
     * WEB PAGES
     * 
     */
    
    @RequestMapping(value="/elovendo/index", method = RequestMethod.GET)
    public String indexPage(Model model) {
    	
    	model.addAttribute("featuredItems", itemService.getRandomItems(Constant.MAX_RANDOM_ITEMS, null));
    	
        return "elovendo/index";
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
    
    /** ERRORS */
    
    @RequestMapping(value="/error")
    public String errorPage() {
    	return "elovendo/error/error";
    }
    
    @RequestMapping(value="/error404")
    public String error404Page() {
    	return "elovendo/error/error404";
    }
    
    @RequestMapping(value="/error401")
    public String error401Page() {
    	return "elovendo/error/error401";
    }

	@Override
	public String getErrorPath() {
		return "/elovendo/error";
	}

}