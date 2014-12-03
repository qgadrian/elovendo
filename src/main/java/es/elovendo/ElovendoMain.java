package es.elovendo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by @Adrian on 13/06/14. All rights reserved.
 */

@EnableAutoConfiguration
@Configuration
@ComponentScan("es.elovendo")
@EnableJpaRepositories
public class ElovendoMain extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ElovendoMain.class);
	}

	public static void main(String[] args) {
		 SpringApplication.run(ElovendoMain.class, args);
	}

}
