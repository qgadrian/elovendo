package es.telocompro.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CharacterEncodingFilter extends org.springframework.web.filter.CharacterEncodingFilter {
		
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
    		FilterChain filterChain) throws ServletException, IOException {
        
		//FAQ: Get parameter BREAKS the filter chain
		if (request != null && !request.getRequestURI().contains("paypalok")) 
			super.doFilterInternal(request, response, filterChain);
        else 
        	filterChain.doFilter(request, response);
        
    }

}
