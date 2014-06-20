package es.telocompro.service.user;

import java.util.ArrayList;
import java.util.Collection;

import es.telocompro.model.user.User;
import es.telocompro.model.user.UserRepository;
import es.telocompro.util.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by @adrian on 17/06/14. All rights reserved.
 */

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public Iterable<User> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User addUser(String login, String password, String firstName,
			String lastName, String address, String phone, String email) {
		User user = new User(login, password, firstName, lastName, address,
				phone, email, 0, 0, Role.ROLE_USER, null);
		return userRepository.save(user);
	}

	@Override
	public User findUserById(Long userId) {
		return userRepository.findOne(userId);
	}

	@Override
	public User findUserByLogin(String login) {
		return userRepository.findByLogin(login);
	}

	@Override
	public User updateUser(Long userId, String firstName, String lastName,
			String address, String phone, String email) {
		User user = userRepository.findOne(userId);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setAddress(address);
		user.setPhone(phone);
		user.setEmail(email);

		return userRepository.save(user);
	}

	@Override
	public void removeUser(Long userId) {
		userRepository.delete(userId);
	}

	// TODO Dummy role added temporarily until next example
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
				"ROLE_ADMIN");
		authorities.add(authority);

		User userObject = findUserByLogin(username);

		// TODO: Workaround
		if (userObject == null)
			throw new UsernameNotFoundException("Username not found => " + UserService.class.getName());
		
		else {
			userObject.setAuthorities(authorities);
			return userObject;
		}
	}
}
