package es.elovendo.rest.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LegalWebController {

	@RequestMapping(value = "/legal", method = RequestMethod.GET)
	public String termsAndConditionsPage() {
		System.out.println("LALALALALALALALAALALALALALa");
		return "elovendo/legal/termsAndConditions";
	}
	
}
