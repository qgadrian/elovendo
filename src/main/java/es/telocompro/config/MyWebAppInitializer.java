//package es.telocompro.config;
//
//import java.util.Set;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRegistration;
//import javax.servlet.ServletSecurityElement;
//
//import org.apache.log4j.Logger;
//import org.springframework.context.ApplicationContext;
//import org.springframework.web.WebApplicationInitializer;
//import org.springframework.web.context.ContextLoaderListener;
//import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
//import org.springframework.web.servlet.DispatcherServlet;
//
//import es.telocompro.BeanConf;
//
//public class MyWebAppInitializer implements WebApplicationInitializer {
//	
//	 @Override
//	 public void onStartup(ServletContext container) {
//		 System.out.println("############################# Web initializer");
//	  // Create the 'root' Spring application context
//	  AnnotationConfigWebApplicationContext rootContext =
//	                       new AnnotationConfigWebApplicationContext();
//	  rootContext.register(BeanConf.class);
//
//	  // Manage the lifecycle of the root application context
//	  container.addListener(new ContextLoaderListener(rootContext));
//
//	  // Create the dispatcher servlet's Spring application context
//	  AnnotationConfigWebApplicationContext dispatcherContext =
//	                     new AnnotationConfigWebApplicationContext();
//	  dispatcherContext.register(WebMvcConfiguration.class);
//
//	  // Register and map the dispatcher servlet
//	  ServletRegistration.Dynamic dispatcher =
//	    container.addServlet("dispatcherServlet", new DispatcherServlet(dispatcherContext));
//	    dispatcher.setLoadOnStartup(1);
//	    dispatcher.addMapping("/");
//	  }
//
////    @Override
////    public void onStartup(ServletContext container) {
////      // Create the 'root' Spring application context
////      AnnotationConfigWebApplicationContext rootContext =
////        new AnnotationConfigWebApplicationContext();
////      rootContext.register(BeanConf.class);
////
////      // Manage the lifecycle of the root application context
////      container.addListener(new ContextLoaderListener(rootContext));
////
////      // Create the dispatcher servlet's Spring application context
////      AnnotationConfigWebApplicationContext dispatcherContext =
////        new AnnotationConfigWebApplicationContext();
////      dispatcherContext.register(WebMvcConfiguration.class);
////
////      // Register and map the dispatcher servlet
////      ServletRegistration.Dynamic dispatcher =
////        container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
////      dispatcher.setLoadOnStartup(1);
////      dispatcher.addMapping("/");
////
////    }
//	
////	private static final String DISPATCHER_SERVLET_NAME = "elovendoDispatcher";
////    private static final String DISPATCHER_SERVLET_MAPPING = "/";
////     
////    @Override
////    public void onStartup(ServletContext servletContext) throws ServletException {
////        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
////        rootContext.register(ApplicationContext.class);
//// 
////        ServletRegistration.Dynamic dispatcher = 
////        		servletContext.addServlet(DISPATCHER_SERVLET_NAME, new DispatcherServlet(rootContext));
////        dispatcher.setLoadOnStartup(1);
////        dispatcher.addMapping(DISPATCHER_SERVLET_MAPPING);
//// 
////        servletContext.addListener(new ContextLoaderListener(rootContext));
////    }	
////	
////	@Override
////	public void onStartup(ServletContext servletContext) throws ServletException {
////		Logger log = Logger.getLogger(MyWebAppInitializer.class.getName());
////		log.debug("MyWebAppInitializer");
////		System.out.println("############################# Web initializer");
////	 
////	   // Create the root appcontext
////	   AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
////	   rootContext.register(BeanConf.class);
////	   // since we registered RootConfig instead of passing it to the constructor
////	   rootContext.refresh(); 
////	 
////	   // Manage the lifecycle of the root appcontext
////	   servletContext.addListener(new ContextLoaderListener(rootContext));
////	   servletContext.setInitParameter("defaultHtmlEscape", "true");
////	 
////	   // now the config for the Dispatcher servlet
////	   AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();
////	   mvcContext.register(WebMvcConfiguration.class);
////	 
////	   // The main Spring MVC servlet.
////	   ServletRegistration.Dynamic appServlet = servletContext.addServlet(
////	      "appServlet", new DispatcherServlet(mvcContext));
////			appServlet.setLoadOnStartup(1);
////	   Set<String> mappingConflicts = appServlet.addMapping("/");
////	 
////	   if (!mappingConflicts.isEmpty()) {
////	      for (String s : mappingConflicts) {
//////	         System.out.println("Mapping conflict: " + s);
////	    	  log.error("Mapping conflict: " + s);
////	      }
////	      throw new IllegalStateException(
////	         "'appServlet' cannot be mapped to '/' under Tomcat versions <= 7.0.14");
////	   }
////	}
//
// }
