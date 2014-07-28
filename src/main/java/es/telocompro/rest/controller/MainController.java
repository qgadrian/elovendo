package es.telocompro.rest.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import es.telocompro.model.item.Item;
import es.telocompro.model.user.User;
import es.telocompro.service.item.ItemService;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */

@Controller
public class MainController {
	
    @Autowired
    private ItemService itemService;
	
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
    public String pageTest4() {
//        response.setContentType("text/html");
//        response.setCharacterEncoding("UTF-8");
        return "elovendo/index";
//        return "/elovendo/index";
    }
    
    @RequestMapping(value="/elovendo/itemtest", method = RequestMethod.GET)
    public String pageAbout(Model model) {
    	
    	List<Item> items = itemService.getAllItemsBySubCategory("Móviles", 0, 0, 0, 5).getContent();
    	model.addAttribute("items", items);
    	
        return "elovendo/left-sidebar";
//    	return "elovendo/itemListTest";
    }
    
    @RequestMapping(value="about2", method = RequestMethod.GET)
    public ModelAndView pageAbout2(HttpServletResponse response) {
    	
        ModelAndView model = new ModelAndView("elovendo/right-sidebar");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        return model;
    }
    
//    @RequestMapping(value="/**")
//    public String errorController(HttpServletResponse response) throws IOException {
//    	
//    	response.sendError(HttpServletResponse.SC_NOT_FOUND);
//    	return "error";
//    }

}
