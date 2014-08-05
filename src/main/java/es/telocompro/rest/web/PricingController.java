package es.telocompro.rest.web;

import java.security.Principal;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.telocompro.model.user.User;


@Controller
@RequestMapping(value = "/site/pricing")
public class PricingController {
	
    @RequestMapping(value="/points", method = RequestMethod.GET)
    public String itemListPage(Model model) {
    	
    	User user = null;
    	SecurityContext context = SecurityContextHolder.getContext();
    	if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken)) {
    		System.out.println("user authenticated");
    		user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		model.addAttribute("user", user);
    	} else System.out.println("user not authenticated");
    	
    	return "elovendo/pricing/points";
    }

}
