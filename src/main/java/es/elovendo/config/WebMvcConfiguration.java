package es.elovendo.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import es.elovendo.util.Constant;

/**
 * Created by @adrian on 19/06/14.
 * All rights reserved.
 */

@Configuration
@ComponentScan("es.telocompro")
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

//	@Override
//    public void addViewControllers(ViewControllerRegistry registry) {
////        registry.addViewController("/login").setViewName("login");
//        
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }

    // Maps resources path to webapp/resources
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/robots.txt").addResourceLocations("/resources/robots.txt");
//        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/css/**").addResourceLocations("/resources/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/resources/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("/resources/images/");
        registry.addResourceHandler("/imgs/**") // TODO: This folder its totally temporary
        	.addResourceLocations(Constant.RESOURCE_IMAGES_PATH);
    }
    
    @Bean
    public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                ((TomcatEmbeddedServletContainerFactory) container).addConnectorCustomizers(
                        new TomcatConnectorCustomizer() {
                            @Override
                            public void customize(Connector connector) {
                                AbstractHttp11Protocol<?> httpProtocol = (AbstractHttp11Protocol<?>) connector.getProtocolHandler();
                                httpProtocol.setCompression("on");
                                httpProtocol.setCompressionMinSize(256);
                                String mimeTypes = httpProtocol.getCompressableMimeTypes();
                                String mimeTypesWithJson = mimeTypes + "," + MediaType.APPLICATION_JSON_VALUE;
                                httpProtocol.setCompressableMimeTypes(mimeTypesWithJson);
                            }
                        }
                );
            }
        };
    }
    
//
//    @Bean
//    public UrlBasedViewResolver setupViewResolver() {
//        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
//        resolver.setPrefix("/WEB-INF/views/");
//        resolver.setSuffix(".jsp");
////        resolver.setContentType("text/html");
//        resolver.setViewClass(JstlView.class);
//        
//        return resolver;
//    }

    // FAQ: For fix error "No mapping found for HTTP request with URI (..) with name 'dispatcherServlet' (..)"
//    @Override
//    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }
    
//    @Bean
//    public ViewResolver viewResolver() {
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setViewClass(JstlView.class);
//        viewResolver.setPrefix("/WEB-INF/views/");
//        viewResolver.setSuffix(".jsp");
//        return viewResolver;
//    }

    // Provides internationalization of messages
//    @Bean
//    public ResourceBundleMessageSource messageSource() {
//        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
//        source.setBasename("messages");
//        return source;
//    }
    
    // Esto es de pruebas, es muy posible que sea basura
//    @Override
//	public void configureHandlerExceptionResolvers(
//			List<HandlerExceptionResolver> exceptionResolvers) {
//		// TODO Auto-generated method stub
//		super.configureHandlerExceptionResolvers(exceptionResolvers);
//		exceptionResolvers.add(new HandlerExceptionResolver() {
//			
//			@Override
//			public ModelAndView resolveException(HttpServletRequest arg0,
//					HttpServletResponse arg1, Object arg2, Exception arg3) {
//				return new ModelAndView("login");
//			}
//		});
//	}

	@Bean
    public MultipartResolver multipartResolver() {
    	CommonsMultipartResolver resolver = new CommonsMultipartResolver();
    	// Size in bytes
//    	resolver.setMaxUploadSize(1752300);
    	resolver.setMaxUploadSize(3000);
    	resolver.setDefaultEncoding("utf-8");
    	return resolver;
    }
	
	/** Internationalization **/
	
	// FAQ: This force the default language to the setted in setDefaultLocale (you have to send "?lang=en to change it)
//	@Bean
//    public LocaleResolver localeResolver() {
//        SessionLocaleResolver slr = new SessionLocaleResolver();
//        slr.setDefaultLocale(Locale.US);
//        return slr;
//    }
// 
//    @Bean
//    public LocaleChangeInterceptor localeChangeInterceptor() {
//        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
//        lci.setParamName("lang");
//        return lci;
//    }
// 
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(localeChangeInterceptor());
//    }

    /**/
    
	/** Message resources **/
//	private static final String MESSAGE_SOURCE = "/WEB-INF/i18n/messages";
//	
//	@Bean
//	public ResourceBundleMessageSource messageSource() {
//		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
////        source.setBasename("messages");
//        source.setBasename(MESSAGE_SOURCE);
//        return source;
//	}
	
//	@Bean(name = "messageSource")
//	public MessageSource messageSource() {
//		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//		messageSource.setBasename(MESSAGE_SOURCE);
//		messageSource.setCacheSeconds(5);
//		return messageSource;
//	}
	
	/**/
    
//    @Bean
//    public ServletContextInitializer servletContextInitializer(ServletContext servletContext) {
//	    final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//	    characterEncodingFilter.setEncoding("UTF-8");
//	    characterEncodingFilter.setForceEncoding(false);
//	    
//	    servletContext.addFilter("characterEncodingFilter", characterEncodingFilter)
//	    	.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
//	    
//	    return servletContext;
//    }
//    
//  @Bean
//  public CharacterEncodingFilter characterEncodingFilter() {
//      CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//      characterEncodingFilter.setEncoding("UTF-8");
//      characterEncodingFilter.setForceEncoding(true);
//      return characterEncodingFilter;
//  }
    
    // FAQ: This HAVE TO BE javax.filter to make utf-8 work, 
//	@Bean
//	public CharacterEncodingFilter characterEncodingFilter() {
//		final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//		characterEncodingFilter.setEncoding("UTF-8");
//		characterEncodingFilter.setForceEncoding(false);
//		return characterEncodingFilter;
//	}
    
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/login").setViewName("elovendo/login");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }

}
