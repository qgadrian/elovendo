package es.telocompro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import es.telocompro.rest.handler.FailureHandler;
import es.telocompro.rest.handler.SuccessHandler;
import es.telocompro.rest.handler.UserLogoutSuccessHandler;

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

	//TODO: Logout no funciona
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            	.antMatchers("/resources/**", "/signup").permitAll()
                .antMatchers("/bazaar/categories").permitAll()
                .antMatchers("/bazaar/**").authenticated()
                .and()
            .formLogin()
            	.failureHandler(new FailureHandler())
            	.successHandler(new SuccessHandler())
            	.defaultSuccessUrl("/bazaar/moda")
//                .loginPage("/login").permitAll()
            	.and()
            .logout()
            	.invalidateHttpSession(true)
//            	.logoutUrl("/logout")
//            	.addLogoutHandler(new SecurityContextLogoutHandler())
            	.logoutSuccessHandler(new UserLogoutSuccessHandler())
                .logoutSuccessUrl("/bazaar")
            	.deleteCookies("jsessionid", "JSESSIONID")
            	.permitAll()
            	.and()
            .sessionManagement()
            .maximumSessions(1);
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