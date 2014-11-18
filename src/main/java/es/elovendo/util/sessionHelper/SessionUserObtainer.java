package es.elovendo.util.sessionHelper;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import es.elovendo.model.user.User;
import es.elovendo.util.sessionHelper.exception.AnonymousUserAuthenticationException;

public class SessionUserObtainer {

	private static SessionUserObtainer obtainer;

	public static SessionUserObtainer getInstance() {
		if (obtainer == null)
			obtainer = new SessionUserObtainer();
		return obtainer;
	}

	public User getSessionUser() throws AnonymousUserAuthenticationException {
		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) context.getAuthentication().getPrincipal();

		if (user != null) throw new AnonymousUserAuthenticationException();
		return user;
	}

	public void closeSession() {
		SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
		SecurityContextHolder.clearContext();
	}

}
