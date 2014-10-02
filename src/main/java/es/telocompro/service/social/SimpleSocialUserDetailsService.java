//package es.telocompro.service.social;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.social.security.SocialUserDetails;
//import org.springframework.social.security.SocialUserDetailsService;
//
//import es.telocompro.service.user.UserService;
//
//public class SimpleSocialUserDetailsService implements SocialUserDetailsService {
//
//	@Autowired
//	private UserService userService;
//    
//	@Override
//	public SocialUserDetails loadUserByUserId(String userId)
//			throws UsernameNotFoundException, DataAccessException {
//		UserDetails userDetails = userService.loadUserByUsername(userId);
//        return (SocialUserDetails) userDetails;
//	}
//
//}
