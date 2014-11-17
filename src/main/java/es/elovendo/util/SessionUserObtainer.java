package es.elovendo.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import es.elovendo.model.user.User;

public class SessionUserObtainer {

	private static SessionUserObtainer obtainer;

	public static SessionUserObtainer getInstance() {
		if (obtainer == null)
			obtainer = new SessionUserObtainer();
		return obtainer;
	}

	public User getSessionUser() {
		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) context.getAuthentication().getPrincipal();

		if (user != null)
			return user;
		else
			return user;
		// else throw exception noUserssession or so;
	}

	public void closeSession() {
		SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
		SecurityContextHolder.clearContext();
	}

}
