package es.telocompro;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */

@Configuration
public class CustomRespositoryRestMvcConfiguration extends RepositoryRestMvcConfiguration {

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        //config.exposeIdsFor(Category.class, SubCategory.class);
    }

}
