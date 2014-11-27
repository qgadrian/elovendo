package es.elovendo.rest.api.controller.v1;

import org.springframework.core.env.Environment;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.elovendo.util.Constant;

@Controller
@RequestMapping(Constant.MOBILE_API_URL_PREFIX_V1 + "login/")
public class MobileLoginController {
	
	@RequestMapping(value="facebook", method = RequestMethod.POST)
    public @ResponseBody void facebookLogin( @RequestParam("token") String token) {
		System.out.println("RECEIVED TOKEN " + token);
		
//		FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(
//				environment.getProperty("facebook.appKey"), environment.getProperty("facebook.appSecret"));
//        OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
//        OAuth2Parameters params = new OAuth2Parameters();
//        params.setRedirectUri("http://facebook.com");
//        String authorizeUrl = oauthOperations.buildAuthorizeUrl(GrantType.IMPLICIT_GRANT, params);
//        AccessGrant accessGrant = new AccessGrant(token);
//        System.out.println(accessGrant.getAccessToken());
//        System.out.println(accessGrant.getExpireTime());
//        System.out.println(accessGrant.getScope());
//        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
//        registry.addConnectionFactory(connectionFactory);
        Facebook facebook = new FacebookTemplate(token);
        
        System.out.println("lalalalalala " + facebook.userOperations().getUserProfile().getEmail());
    }

}
