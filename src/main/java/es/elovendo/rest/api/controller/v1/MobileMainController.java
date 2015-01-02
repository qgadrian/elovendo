package es.elovendo.rest.api.controller.v1;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;

import es.elovendo.model.user.User;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.service.user.UserService;
import es.elovendo.util.Constant;

@Controller
@RequestMapping(Constant.MOBILE_API_URL_PREFIX_V1) 
public class MobileMainController {
	
	@Autowired
	private UserService userService;
	static Logger logger = Logger.getLogger(MobileMainController.class.getName());

	@RequestMapping(value = "login")
	public @ResponseBody String loginPage(
			@RequestParam(value="username", required=true) String email,
			@RequestParam(value="password", required=true) String password,
			Device device) {

//		if (device.isMobile()) {
			
			User user = null;

			try {
				user = userService.findUserByEmail(email);
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				
				if (encoder.matches(password, user.getPassword())) {
					Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
					logger.debug("MOBILE USER LOGGED " + user.getLogin());
					return RequestContextHolder.currentRequestAttributes().getSessionId();
				} else return null;
				
			} catch (UserNotFoundException e) { logger.error("USER NOT FOUND"); return null; }
//		}

//		return null;
	}
	
}
