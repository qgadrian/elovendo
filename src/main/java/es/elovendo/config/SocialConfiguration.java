//package es.elovendo.config;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Scope;
//import org.springframework.context.annotation.ScopedProxyMode;
//import org.springframework.core.env.Environment;
//import org.springframework.social.UserIdSource;
//import org.springframework.social.config.annotation.SocialConfigurer;
//import org.springframework.social.connect.ConnectionFactoryLocator;
//import org.springframework.social.connect.ConnectionRepository;
//import org.springframework.social.connect.UsersConnectionRepository;
//import org.springframework.util.Assert;
//@Configuration
//public class SocialConfiguration {
//	
//private static boolean securityEnabled = isSocialSecurityAvailable();
//	
//	@Autowired
//	private Environment environment;
//	
//	private List<SocialConfigurer> socialConfigurers;
//
//	@Autowired
//	public void setSocialConfigurers(List<SocialConfigurer> socialConfigurers) {
//		Assert.notNull(socialConfigurers, "At least one configuration class must implement SocialConfigurer (or subclass SocialConfigurerAdapter)");
//		Assert.notEmpty(socialConfigurers, "At least one configuration class must implement SocialConfigurer (or subclass SocialConfigurerAdapter)");
//		this.socialConfigurers = socialConfigurers;
//	}
//
//	@Bean
//	public ConnectionFactoryLocator connectionFactoryLocator() {
//		if (securityEnabled) {
//			SecurityEnabledConnectionFactoryConfigurer cfConfig = new SecurityEnabledConnectionFactoryConfigurer();
//			for (SocialConfigurer socialConfigurer : socialConfigurers) {
//				socialConfigurer.addConnectionFactories(cfConfig, environment);
//			}
//			return cfConfig.getConnectionFactoryLocator();
//		} else {
//			DefaultConnectionFactoryConfigurer cfConfig = new DefaultConnectionFactoryConfigurer();
//			for (SocialConfigurer socialConfigurer : socialConfigurers) {
//				socialConfigurer.addConnectionFactories(cfConfig, environment);
//			}
//			return cfConfig.getConnectionFactoryLocator();
//		}
//	}
//	
//	@Bean
//	public UserIdSource userIdSource() {
//		UserIdSource userIdSource = null;
//		for (SocialConfigurer socialConfigurer : socialConfigurers) {
//			UserIdSource userIdSourceCandidate = socialConfigurer.getUserIdSource();
//			if (userIdSourceCandidate != null) {
//				userIdSource = userIdSourceCandidate;
//				break;
//			}
//		}
//		Assert.notNull(userIdSource, "One configuration class must implement getUserIdSource from SocialConfigurer.");
//		return userIdSource;
//	}
//	
//	@Bean
//	public UsersConnectionRepository usersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
//		UsersConnectionRepository usersConnectionRepository = null;
//		for (SocialConfigurer socialConfigurer : socialConfigurers) {
//			UsersConnectionRepository ucrCandidate = socialConfigurer.getUsersConnectionRepository(connectionFactoryLocator);
//			if (ucrCandidate != null) {
//				usersConnectionRepository = ucrCandidate;
//				break;
//			}
//		}
//		Assert.notNull(usersConnectionRepository, "One configuration class must implement getUsersConnectionRepository from SocialConfigurer.");
//		return usersConnectionRepository;
//	}
//
//	@Bean
//	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
//	public ConnectionRepository connectionRepository(UsersConnectionRepository usersConnectionRepository) {
//		return usersConnectionRepository.createConnectionRepository(userIdSource().getUserId());
//	}
//
//	private static boolean isSocialSecurityAvailable() {
//		try {
//			Class.forName("org.springframework.social.security.SocialAuthenticationServiceLocator");
//			return true;
//		} catch (ClassNotFoundException cnfe) {
//			return false; 
//		}
//	}
//
//}
