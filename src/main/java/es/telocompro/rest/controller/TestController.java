package es.telocompro.rest.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import es.telocompro.model.user.User;

@Controller
public class TestController {
	
	@RequestMapping("/thymeleaf")
    public String test(Model model) {
        System.out.println("~~~~~~~~ jstp test controller called");
        
        model.addAttribute("name", "testeando");
        model.addAttribute("nametest", "directly name");
        return "thymetest";
  }

}
