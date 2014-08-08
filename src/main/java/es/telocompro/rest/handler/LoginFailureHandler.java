package es.telocompro.rest.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class LoginFailureHandler implements AuthenticationFailureHandler {
	
	private Logger log = Logger.getLogger(LoginFailureHandler.class);

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, 
			AuthenticationException arg2) throws IOException, ServletException {
		
		log.warn(request.getParameter("username") + " tried and failed to login.");
		
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

}
