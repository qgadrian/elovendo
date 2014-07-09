//package es.telocompro.config;
//
//import org.springframework.boot.context.embedded.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.DispatcherServlet;
//
//@Configuration
//public class DispatcherConfiguration {
//	
//	@Bean
//	public ServletRegistrationBean dispatcherRegistration() {
//		ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet());
//        registration.addUrlMappings("/");
//        registration.setLoadOnStartup(1);
//        
//        System.out.println("~~~~~~~ Servlet regristated " + registration.getServletName());
//        return registration;
//        
//	}
//	
//	@Bean
//	public DispatcherServlet dispatcherServlet() {
//        return new DispatcherServlet();
//    }
//	
////	@Bean
////	public ServletRegistrationBean dispatcherRegistration() {
////		ServletRegistrationBean registration = new ServletRegistrationBean(
////                new DispatcherServlet());
////        registration.addUrlMappings("/");
////        registration.setLoadOnStartup(1);
////        
////        System.out.println("~~~~~~~ Servlet regristated " + registration.getServletName());
////        return registration;
////        
////	}
////	
//////	@Bean
//////	public DispatcherServlet dispatcherServlet() {
//////        return new DispatcherServlet();
//////    }
//
//}