package es.elovendo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment environment) {
//		cfConfig.addConnectionFactory(new FacebookConnectionFactory(environment.getProperty("facebook.appKey"),
//				environment.getProperty("facebook.appSecret")));
		
		cfConfig.addConnectionFactory(new GoogleConnectionFactory(environment
				.getProperty("spring.social.google.clientId"), environment
				.getProperty("spring.social.google.clientSecret")));
		
//		cfConfig.addConnectionFactory(new TwitterConnectionFactory(environment
//				.getProperty("spring.social.twitter.appId"), environment
//				.getProperty("spring.social.twitter.appSecret")));

	}

	@Override
	public UserIdSource getUserIdSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		// TODO Auto-generated method stub
		return null;
	}

}
