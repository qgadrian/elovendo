package es.elovendo.rest.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class UserLogoutSuccessHandler extends
SimpleUrlLogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) 
					throws IOException, ServletException {

		System.out.println("logout sucess handler");
		
		request.getSession().invalidate();
		response.setStatus(HttpServletResponse.SC_OK);
		super.onLogoutSuccess(request, response, authentication);
//		arg0.logout();
//		SecurityContextHolder.clearContext();
//		SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
//		arg2.setAuthenticated(false);

		
	}

}
