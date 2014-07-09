package es.telocompro.rest.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import es.telocompro.model.user.User;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */

@RestController
public class MainController {

    @RequestMapping(value = "/welcome")
    @ResponseStatus(HttpStatus.OK) // TODO Testear esta notación
    public String login(HttpServletResponse response, Device device, Principal principal) {
    	System.out.println("We are in main controller");
    	
    	 User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	 String userId = String.valueOf(user.getUserId());

    	if (device.isNormal())
    		return principal.getName() + " you are loggued motherfucker!!! and your id is " + userId + " btw";
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
    
    @RequestMapping(value="/error404")
    public ModelAndView error404Page(HttpServletResponse response) {
    	System.out.println("error 404 controller");
    	
    	ModelAndView model = new ModelAndView("elovendo/error/error405");
    	response.setContentType("text/html");
//        response.setCharacterEncoding("UTF-8");
    	
    	return model;
    }
    
    @RequestMapping(value="/error401")
    public ModelAndView error401Page(HttpServletResponse response) {
    	System.out.println("error 401 controller");
    	
    	ModelAndView model = new ModelAndView("elovendo/error/error401");
    	response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
    	
    	return model;
    }
    
    /** WEB MVC **/
    
    @RequestMapping(value="/elovendo/index", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8",
    		headers="Accept=*/*")
    public ModelAndView pageTest4(HttpServletResponse response) {
    	
        ModelAndView model = new ModelAndView("elovendo/index");
//        response.setContentType("text/html");
//        response.setCharacterEncoding("UTF-8");
        return model;
//        return "/elovendo/index";
    }
    
    @RequestMapping(value="about", method = RequestMethod.GET)
    public ModelAndView pageAbout(HttpServletResponse response) {
    	
        ModelAndView model = new ModelAndView("elovendo/left-sidebar");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        return model;
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
