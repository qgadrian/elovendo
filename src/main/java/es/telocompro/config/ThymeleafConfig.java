package es.telocompro.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
public class ThymeleafConfig {
	
	@Bean
    public TemplateResolver templateResolver(){
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
//        templateResolver.setTemplateMode("HTML5");
        templateResolver.setTemplateMode("LEGACYHTML5");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        
        return templateResolver;
    }
 
    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        
        templateEngine.addDialect(new SpringSecurityDialect());
        return templateEngine;
    }
 
    @Bean
    public ViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver() ;
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setOrder(1);
 
        return viewResolver;
    }
    
    @Bean
    public MessageSource messageSource() {

        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("/resources/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(0);
        return messageSource;
    }

}
