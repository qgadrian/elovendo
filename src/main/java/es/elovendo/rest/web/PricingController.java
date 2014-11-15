package es.elovendo.rest.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.elovendo.model.user.User;
import es.elovendo.util.SessionUserObtainer;

@Controller
@RequestMapping(value = "/site/pricing")
public class PricingController {

	@RequestMapping(value = "/points", method = RequestMethod.GET)
	public String itemListPage(Model model) {

		User user = SessionUserObtainer.getInstance().getSessionUser();
		model.addAttribute("user", user);

		return "elovendo/pricing/points";
	}

	@RequestMapping(value = "/paymentok", method = RequestMethod.GET)
	public String paymentOkPage() {
		return "elovendo/pricing/paymentOk";
	}

	@RequestMapping(value = "/paymentfailed", method = RequestMethod.GET)
	public String paymentFailedPage() {
		return "elovendo/pricing/paymentFailed";
	}

}
