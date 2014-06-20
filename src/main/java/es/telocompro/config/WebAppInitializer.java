package es.telocompro.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import es.telocompro.BeanConf;

public class WebAppInitializer extends
AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { BeanConf.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { ThymeleafConfig.class, WebMvcConfiguration.class  };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}

}
