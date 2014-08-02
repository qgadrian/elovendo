package es.telocompro.rest.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/site/pricing")
public class PricingController {
	
    @RequestMapping(value="/points", method = RequestMethod.GET)
    public String itemListPage(Model model) {
    	return "elovendo/pricing/points";
    }

}
