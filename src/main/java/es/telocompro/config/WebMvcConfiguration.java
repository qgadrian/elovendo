package es.telocompro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import es.telocompro.util.Constant;

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
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/css/**").addResourceLocations("/resources/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/resources/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("/resources/images/");
        registry.addResourceHandler("/imgs/**") // TODO: This folder its totally temporary
        	.addResourceLocations(Constant.RESOURCE_IMAGES_PATH);
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
//        slr.setDefaultLocale(Locale.forLanguageTag("es"));
//        return slr;
//    }
 
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
 
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

//	@Bean
//	public ResourceBundleMessageSource messageSource() {
//		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
//        source.setBasename("messages");
//        return source;
//	}
    
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

}
