package es.telocompro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import es.telocompro.rest.handler.LoginFailureHandler;
import es.telocompro.rest.handler.LoginSuccessHandler;
import es.telocompro.rest.handler.UserLogoutSuccessHandler;
import es.telocompro.util.CharacterEncodingFilter;

/**
 * Created by @adrian on 19/06/14.
 * All rights reserved.
 */

@Configuration
@EnableWebMvcSecurity
//@EnableGlobalMethodSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {
	
	@Autowired
    public void configureGlobal(UserDetailsService userDetailsService, AuthenticationManagerBuilder auth) 
    		throws Exception {
        auth
            .userDetailsService(userDetailsService);
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
//                .antMatchers("/bazaar/*").permitAll()
//                .antMatchers("/bazaar/items/**").authenticated()
            	.antMatchers("/imgs/**").permitAll() //TODO: Not sure if necesary...
            	.antMatchers("/login").permitAll()
            	.antMatchers("/site/user/**").permitAll()
            	.antMatchers("/bazaar/**").permitAll()
            	.antMatchers("/items/item/**").authenticated()
            	.antMatchers("/site/delete/**").authenticated()
            	.antMatchers("/logout").authenticated()
                .and()
            .formLogin()
            	.failureHandler(new LoginFailureHandler())
            	.successHandler(new LoginSuccessHandler())
            	.defaultSuccessUrl("/loginRedirect")
                .loginPage("/login").permitAll()
            	.and()
            .logout()
            	.invalidateHttpSession(true)
//            	.logoutUrl("/logout")
//            	.addLogoutHandler(new SecurityContextLogoutHandler())
            	.logoutSuccessHandler(new UserLogoutSuccessHandler())
                .logoutSuccessUrl("/bazaar/categories")
            	.deleteCookies("jsessionid", "JSESSIONID")
            	.permitAll()
            	.and()
            .sessionManagement()
            .maximumSessions(5);
        
        http.csrf().disable();
        
        http.rememberMe();
        
        /** HTTPS **/
//        http.requiresChannel().anyRequest().requiresSecure();
    }

}



//@Autowired
//private DataSource dataSource;
//
//@Autowired
//public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//    auth
////            .inMemoryAuthentication()
////            .withUser("user").password("password").roles("USER");
//    .jdbcAuthentication()
//    	.dataSource(dataSource)
//    	.withDefaultSchema()
//    	.usersByUsernameQuery(getUserQuery());
//    	
//}