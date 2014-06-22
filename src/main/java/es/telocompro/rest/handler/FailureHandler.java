package es.telocompro.rest.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class FailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest arg0,
			HttpServletResponse arg1, AuthenticationException arg2)
			throws IOException, ServletException {
		arg1.sendError(HttpServletResponse.SC_FORBIDDEN);
	}

}
