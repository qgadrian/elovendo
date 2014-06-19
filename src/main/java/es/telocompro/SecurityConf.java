package es.telocompro;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

/**
 * Created by @adrian on 19/06/14.
 * All rights reserved.
 */

@Configuration
@EnableWebMvcSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER");
//    }

    // DOCUMENTATION: http://docs.spring.io/spring-security/site/docs/3.2.x/guides/form.html

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
//                .antMatchers("/tecnologia").permitAll()
//                .antMatchers("/categories").permitAll()
//                .antMatchers("/resources/**").permitAll()
//                .antMatchers("/helloworld").permitAll()
                .antMatchers("/*").permitAll()
//                .antMatchers("/*").authenticated()
                .and()
            .formLogin()
                .loginPage("/login").permitAll();
    }


}
