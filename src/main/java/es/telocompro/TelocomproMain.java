package es.telocompro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by @adrian on 13/06/14.
 * All rights reserved.
 */

@EnableAutoConfiguration
@ComponentScan
@EnableJpaRepositories
public class TelocomproMain {

    public static void main(String[] args) {
        SpringApplication.run(TelocomproMain.class, args);
    }

}
