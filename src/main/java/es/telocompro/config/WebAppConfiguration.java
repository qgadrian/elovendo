//package es.telocompro.config;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
//import org.springframework.boot.context.embedded.ErrorPage;
//import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//
//@Configuration
//public class WebAppConfiguration {
//	
//	private Set<ErrorPage> pageHandlers;
//
//	  @PostConstruct
//	  private void init(){
//	      pageHandlers = new HashSet<ErrorPage>();
//	      pageHandlers.add(new ErrorPage(HttpStatus.NOT_FOUND, "/error404"));
//	      pageHandlers.add(new ErrorPage(HttpStatus.UNAUTHORIZED, "/error401"));
//	  }
//
//	  @Bean
//	  public EmbeddedServletContainerFactory servletContainer() {
//	      TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
//	      factory.setErrorPages(pageHandlers);
//	      return factory;
//	  }
//
//}