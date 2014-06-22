package es.telocompro.rest.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public class UserLogoutHandler implements LogoutHandler {

	@Override
	public void logout(HttpServletRequest arg0, HttpServletResponse arg1,
			Authentication arg2) {
		
//		Cookie[] cookies = arg0.getCookies();
//		Cookie[] newCookies = cookies;
//		int i = 0;
//		
//		for (Cookie cookie : cookies) {
//			if (!cookie.getName().equalsIgnoreCase("JSESSIONID"))
//				newCookies[i++] = cookie;
//		}
//		
//		for (Cookie cookie : newCookies) {
//			arg1.addCookie(cookie);
//		}
//		
//		try {
//			arg1.sendError(HttpServletResponse.SC_OK);
//		} catch (IOException e) {
//			System.out.println("Error sending http response from logout handler");
//		}
		
		System.out.println("logout handler");
		
		try {
			arg0.logout();
		} catch (ServletException e) {
			
		}
		arg0.getSession().invalidate();
		
		try {
			arg1.sendError(HttpServletResponse.SC_OK);
		} catch (IOException e) {
			
		}
		
//		SecurityContextHolder.clearContext();

	}
	
	

}
