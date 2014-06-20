package es.telocompro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

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
        http
            .authorizeRequests()
            	.antMatchers("/resources/**", "/signup").permitAll()
                .antMatchers("/bazaar/categories").permitAll()
                .antMatchers("/bazaar/**").authenticated()
                .and()
            .formLogin()
//                .loginPage("/login").permitAll()
            	.and()
            .logout().permitAll();
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