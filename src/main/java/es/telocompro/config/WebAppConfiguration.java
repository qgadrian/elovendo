package es.telocompro.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class WebAppConfiguration {

	private Set<ErrorPage> pageHandlers;

	@PostConstruct
	private void init() {
		pageHandlers = new HashSet<ErrorPage>();
		pageHandlers.add(new ErrorPage(HttpStatus.NOT_FOUND, "/error404"));
		pageHandlers.add(new ErrorPage(HttpStatus.UNAUTHORIZED, "/error401"));
	}

	/** HTTPS and Paging error **/
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {

//		final String keystoreFile = "/home/adrian/Proyectos/eclipse/elovendo/keystore.p12";
//		final String keystorePass = "KLbT0@2a330oO09zMM@d";
//		final String keystoreType = "PKCS12";
//		final String keystoreProvider = "SunJSSE";
//		final String keystoreAlias = "elovendo";

		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		factory.setErrorPages(pageHandlers);
		factory.setUriEncoding("UTF-8");

//		TomcatConnectorCustomizer tomcatConnectorCustomizer = new TomcatConnectorCustomizer() {
//
//			@Override
//			public void customize(Connector con) {
//				con.setScheme("https");
//				con.setSecure(true);
//				Http11NioProtocol proto = (Http11NioProtocol) con.getProtocolHandler();
//				proto.setSSLEnabled(true);
//				proto.setKeystoreFile(keystoreFile);
//				proto.setKeystorePass(keystorePass);
//				proto.setKeystoreType(keystoreType);
//				proto.setProperty("keystoreProvider", keystoreProvider);
//				proto.setKeyAlias(keystoreAlias);
//
//			}
//		};

//		factory.addConnectorCustomizers(tomcatConnectorCustomizer);

		return factory;
	}
	
//	 @Bean
//	  public GzipFilter gzipFilter() {
//	    return new GzipFilter();
//	  }

	// FAQ: This is not working on login, seems doesn't load before Spring Security stuff
//	  @Bean
//	  public CharacterEncodingFilter characterEncodingFilter() {
//	    final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//	    characterEncodingFilter.setEncoding("UTF-8");
//	    characterEncodingFilter.setForceEncoding(true);
//	    return characterEncodingFilter;
//	  }
	  
	  @Bean
	    public FilterRegistrationBean characterEncodingFilter() {
	        CharacterEncodingFilter filter = new CharacterEncodingFilter();
	        filter.setEncoding("UTF-8");
	        filter.setForceEncoding(true);
	        FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
	        filterRegBean.setFilter(filter);
	        List<String> urlPatterns = new ArrayList<String>();
	        urlPatterns.add("/*");
	        filterRegBean.setUrlPatterns(urlPatterns);
	        filterRegBean.setOrder(1); // first filter !
	        return filterRegBean;
	    }
}