//package es.telocompro.config;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//
//import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
//
//import es.telocompro.BeanConf;
//
//public class WebAppInitializer extends
//AbstractAnnotationConfigDispatcherServletInitializer {
//
//	
//	@Override
//	public void onStartup(ServletContext servletContext) throws ServletException {
//		super.onStartup(servletContext);
//	}
//
//	@Override
//	protected Class<?>[] getRootConfigClasses() {
//		return new Class[] { BeanConf.class };
//	}
//
////	@Override
////	protected Class<?>[] getServletConfigClasses() {
////		return new Class[] { ThymeleafConfig.class, WebMvcConfiguration.class  };
////	}
//	
//	@Override
//	protected Class<?>[] getServletConfigClasses() {
//		return new Class[] { WebMvcConfiguration.class  };
//	}
//
//	@Override
//	protected String[] getServletMappings() {
////		return new String[] {"*.jpg", "*.js", "*.css", "/**/*.html", "/"};
//		return new String[] {"/"};
//	}
//	
//}