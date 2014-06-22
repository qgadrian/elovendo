package es.telocompro.rest.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */

@RestController
public class MainController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String hello() {
    	System.out.println("We are in main controller");

        return "";
    }
    
    @RequestMapping(value="/logout")
    public String logout(HttpServletResponse response) {
    	System.out.println("logout controller");
    	
    	SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    	Cookie cookie = new Cookie("JSESSIONID", null);
    	response.addCookie(cookie);
    	
    	return "logged out";
    }

}
