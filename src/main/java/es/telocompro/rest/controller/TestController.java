package es.telocompro.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {
	
	@RequestMapping("/jsptest")
    public String test(ModelAndView modelAndView) {
        System.out.println("~~~~~~~~ jstp test controller called");
        return "index";
  }

}
