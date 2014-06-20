package es.telocompro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by @adrian on 13/06/14.
 * All rights reserved.
 */

@EnableAutoConfiguration
//@Configuration
@ComponentScan("es.telocompro")
@EnableJpaRepositories
public class TelocomproMain {

    public static void main(String[] args) {
        SpringApplication.run(TelocomproMain.class, args);
    }

}
