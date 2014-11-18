package es.elovendo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

import es.elovendo.rest.handler.UserLogoutSuccessHandler;
import es.elovendo.util.ApiRequestMatcher;
import es.elovendo.util.CharacterEncodingFilter;

/**
 * Created by @adrian on 19/06/14.
 * All rights reserved.
 */

@Configuration
@EnableWebMvcSecurity
//@EnableGlobalMethodSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {
	
//	@Autowired
//	DataSource dataSource;
//	@Autowired
//    TextEncryptor textEncryptor;
	
	@Autowired
	CompositeSessionAuthenticationStrategy compositeSessionAuthenticationStrategy;
	
	@Autowired
    public void configureGlobal(UserDetailsService userDetailsService, AuthenticationManagerBuilder auth) 
    		throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(new BCryptPasswordEncoder());
    }

    // DOCUMENTATION: http://docs.spring.io/spring-security/site/docs/3.2.x/guides/form.html

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, WebAsyncManagerIntegrationFilter.class);
        
        http
            .authorizeRequests()
//            	.antMatchers("/resources/**", "/signup").permitAll()
            	/* Web matches */
            	.antMatchers("/private/admin/**").hasRole("ADMIN")
            	.antMatchers("/login", "/favicon.ico").permitAll()
            	.antMatchers("/imgs/**").permitAll() //TODO: Not sure if necesary...
            	.antMatchers("/site/profile/**").authenticated()
            	.antMatchers("/site/vote**").authenticated()
            	.antMatchers("/site/public/**").permitAll()
            	.antMatchers("/site/**").permitAll()
            	.antMatchers("/bazaar/**").permitAll()
            	.antMatchers("/auth/**").permitAll()
            	.antMatchers("/bazaar/edit**").authenticated()
            	.antMatchers("/bazaar/delete**").authenticated()
            	.antMatchers("/bazaar/add**").authenticated()
            	.antMatchers("/bazaar/item/fav**").authenticated()
            	.antMatchers("/logout").authenticated()
            	.antMatchers("/elovendo/messages/public/send").anonymous()
            	.antMatchers("/elovendo/messages/**").authenticated()
            	.antMatchers("/elovendo/offer/public/send").anonymous()
            	.antMatchers("/elovendo/offer/**").authenticated()
                .and()
            .formLogin()
//            	.failureHandler(new LoginFailureHandler())
//            	.successHandler(new LoginSuccessHandler())
            	.defaultSuccessUrl("/loginRedirect")
            	.failureUrl("/login?error")
                .loginPage("/login").permitAll()
            	.and()
            .logout()
            	.invalidateHttpSession(true)
//            	.logoutUrl("/logout")
//            	.addLogoutHandler(new SecurityContextLogoutHandler())
            	.logoutSuccessHandler(new UserLogoutSuccessHandler())
                .logoutSuccessUrl("/index")
            	.deleteCookies("jsessionid", "JSESSIONID")
            	.permitAll()
            .and()
            	.rememberMe()
            .and()
            		.sessionManagement()
            			.sessionAuthenticationStrategy(compositeSessionAuthenticationStrategy)
            .and()
            	.apply(new SpringSocialConfigurer().postLoginUrl("/").alwaysUsePostLoginUrl(true));
//            	.and()
//            .sessionManagement()
//            .maximumSessions(5);
        
        // Disabling CSRF for API
        ApiRequestMatcher requestMatcher = ApiRequestMatcher.getInstance();
        http.csrf().requireCsrfProtectionMatcher(requestMatcher);
        
//        http.rememberMe().tokenRepository(persistentTokenRepository()).tokenValiditySeconds(1209600).userDetailsService(userDetailsService);
        
        /** HTTPS **/
//        http.requiresChannel().anyRequest().requiresSecure();
    }
    
    @Bean
    public CompositeSessionAuthenticationStrategy compositeSessionAuthenticationStrategy(
    		ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy, 
    		SessionFixationProtectionStrategy sessionFixationProtectionStrategy, 
    		RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy) {
    	List<SessionAuthenticationStrategy> strategies = new ArrayList<>();
    	strategies.add(concurrentSessionControlAuthenticationStrategy);
    	strategies.add(sessionFixationProtectionStrategy);
    	strategies.add(registerSessionAuthenticationStrategy);
    	return new CompositeSessionAuthenticationStrategy(strategies);
    }
    
    @Bean
    public SessionFixationProtectionStrategy sessionFixationProtectionStrategy() {
    	return new SessionFixationProtectionStrategy();
    }
    
    @Bean
    public RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy(SessionRegistry sessionRegistry) {
    	return new RegisterSessionAuthenticationStrategy(sessionRegistry);
    }
    
    @Bean
    public ConcurrentSessionControlAuthenticationStrategy strategy(SessionRegistry sessionRegistry) {
    	return new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
    }
   
    @Bean
    public ConcurrentSessionFilter concurrencyFilter(SessionRegistry sessionRegistry) {
    	return new ConcurrentSessionFilter(sessionRegistry, "/logout");
    }
    
    @Bean
    public SessionRegistry sessionRegistry() {
    	return new SessionRegistryImpl();
    }
    
//    @Bean
//    public PersistentTokenRepository persistentTokenRepository() {
//    	JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
//    	db.setDataSource(dataSource);
//    	return db;
//    }
    
    @Bean
    public SocialUserDetailsService socialUserDetailsService(UserDetailsService userDetailsService) {
        return new SimpleSocialUserDetailsService(userDetailsService);
    }
    
//    @Bean
//    public ProviderSignInController providerSignInController(
//                ConnectionFactoryLocator connectionFactoryLocator,
//                UsersConnectionRepository usersConnectionRepository) {
//        return new ProviderSignInController(
//            connectionFactoryLocator,
//            usersConnectionRepository,
//            new SimpleSignInAdapter(new HttpSessionRequestCache()));
//    }

}