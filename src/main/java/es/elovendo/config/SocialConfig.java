//package es.elovendo.config;
//
//import org.springframework.core.env.Environment;
//import org.springframework.social.UserIdSource;
//import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
//import org.springframework.social.config.annotation.SocialConfigurer;
//import org.springframework.social.connect.ConnectionFactoryLocator;
//import org.springframework.social.connect.UsersConnectionRepository;
//import org.springframework.social.facebook.connect.FacebookConnectionFactory;
//
//public class SocialConfig implements SocialConfigurer {
//
//	@Override
//	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment environment) {
//		cfConfig.addConnectionFactory(new FacebookConnectionFactory(environment.getProperty("facebook.appKey"),
//				environment.getProperty("facebook.appSecret")));
//
//	}
//
//	@Override
//	public UserIdSource getUserIdSource() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
