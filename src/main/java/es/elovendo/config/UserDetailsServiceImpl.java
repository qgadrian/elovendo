package es.elovendo.config;
//package es.telocompro.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import es.telocompro.rest.controller.exception.UserNotFoundException;
//import es.telocompro.service.user.UserService;
//
//@Service("userDetailsService")
//public class UserDetailsServiceImpl implements UserDetailsService {
//	
//	@Autowired
//	UserService userService;
//
//	@Override
//	public UserDetails loadUserByUsername(String username)
//			throws UsernameNotFoundException {
//		
//		try {
//			return userService.findUserByLogin(username);
//		} catch (UserNotFoundException e) {
//			throw new UsernameNotFoundException(username);
//		}
//	}
//
//}
