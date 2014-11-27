package es.elovendo.rest.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.elovendo.util.Constant;
import es.elovendo.util.mail.MailSender;

@Controller
@RequestMapping(value="/elovendo/contact")
public class ContactWebController {
	
	@RequestMapping(value = "/about", method = RequestMethod.POST)
	public @ResponseBody void contactForm(@RequestParam("msg") String message,
			@RequestParam("name") String name,
			@RequestParam("email") String email) {
		
		MailSender mailSender = MailSender.getInstance();
		String msgBuilt = "Name: " + name + "\nEmail: " + email + "\n\n" + message;
		mailSender.sendMail(name, email, Constant.CONTACT_EMAIL, "About contact", msgBuilt);
		
	}

}
