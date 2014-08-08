package es.telocompro.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Order(1)
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

	// FAQ: Character encoding filter has to be setted BEDFORE spring security chain filter, otherwise, won't work
//	@Override
//	protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
//		FilterRegistration.Dynamic characterEncodingFilter = servletContext
//				.addFilter("characterEncodingFilter", new CharacterEncodingFilter());
//		characterEncodingFilter.setInitParameter("encoding", "UTF-8");
//		characterEncodingFilter.setInitParameter("forceEncoding", "false");
//		characterEncodingFilter.addMappingForUrlPatterns(null, false, "/*");
//		
////		insertFilters(servletContext, new MultipartFilter());
//		
//	}

}
