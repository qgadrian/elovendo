package es.telocompro.service.user;

import es.telocompro.model.item.Item;
import es.telocompro.model.province.Province;
import es.telocompro.model.user.User;
import es.telocompro.model.user.UserRepository;
import es.telocompro.model.user.role.Role;
import es.telocompro.model.user.role.RoleRepository;
import es.telocompro.model.vote.Vote;
import es.telocompro.rest.controller.exception.ItemNotFoundException;
import es.telocompro.rest.controller.exception.LoginNotAvailableException;
import es.telocompro.rest.controller.exception.ProvinceNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;
import es.telocompro.rest.controller.exception.VoteDuplicateException;
import es.telocompro.service.item.ItemService;
import es.telocompro.service.province.ProvinceService;
import es.telocompro.service.vote.VoteService;
import es.telocompro.util.RoleEnum;

import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	ProvinceService provinceService;
	@Autowired
	VoteService voteService;
	@Autowired
	ItemService itemService;

	@Override
	public Iterable<User> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User addUser(String login, String password, String firstName,
			String lastName, String address, String phone, String email, String provinceName) 
					throws LoginNotAvailableException, ProvinceNotFoundException {
		
		Province province = provinceService.findProvinceByName(provinceName);
		if (province == null) throw new ProvinceNotFoundException(provinceName);
		
		if (findUserByLogin(login) != null ) throw new LoginNotAvailableException(login); 
		
		// At this point, all new users always will be ROLE_USER
		Role role = roleRepository.findByRoleName(RoleEnum.ROLE_USER);
		
		User user = new User(login, password, firstName, lastName, address,
				phone, email, province, role, null);
		
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
			String address, String phone, String email) throws UserNotFoundException {
		User user = userRepository.findOne(userId);
		
		if (user == null) throw new UserNotFoundException(userId);
		
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setAddress(address);
		user.setPhone(phone);
		user.setEmail(email);

		return userRepository.save(user);
	}
	
	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public void removeUser(Long userId) throws UserNotFoundException {
		if (findUserById(userId) == null) throw new UserNotFoundException(userId);
		userRepository.delete(userId);
	}

	// TODO Dummy role added temporarily until next example
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
//		Collection<GrantedAuthority> authorities = new ArrayList<>();
//		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
//				"ROLE_ADMIN");
//		authorities.add(authority);
//
//		User userObject = findUserByLogin(username);
//
//		// TODO: Workaround
//		if (userObject == null)
//			throw new UsernameNotFoundException("Username not found => " + UserService.class.getName());
//		
//		else {
//			userObject.setAuthorities(authorities);
//			return userObject;
//		}
		User user = userRepository.findByLogin(username);
		
		if (user == null) 
			throw new UsernameNotFoundException("Username not found => " 
					+ UserService.class.getName());
		
		return user;
	}

	// TODO: If Paypal, increase the reability value for users
	@Override
	public Vote voteUser(Long userIdVote, Long userIdReceive, Long itemId, int voteType, String voteMessage) 
			throws UserNotFoundException, ItemNotFoundException, VoteDuplicateException {
		User userVote = userRepository.findOne(userIdVote);
		User userReceive = userRepository.findOne(userIdReceive);
		Item item = itemService.getItemById(itemId);
		
		if (userVote == null) throw new UserNotFoundException(userIdVote);
		if (userReceive == null) throw new UserNotFoundException(userIdReceive);
		if (item == null) throw new ItemNotFoundException(itemId);
		
		// Check if user already voted for this item
		Vote vote = voteService.getVote(userVote, userReceive, item);
		if (vote != null) throw new VoteDuplicateException(userIdVote, userIdReceive, itemId);
		
		
		// Check reability for user who is voting
		float reability;
		if (userReceive.getUserValue() >= 95) reability = 1.5f;
		else if (userReceive.getUserValue() >= 90) reability = 1f;
		else if (userReceive.getUserValue() >= 80) reability = 0.8f;
		else if (userReceive.getUserValue() >= 70) reability = 0.5f;
		else if (userReceive.getUserValue() >= 60) reability = 0.2f;
		else if (userReceive.getUserValue() >= 10) reability = 0.1f;
		else reability = 0f;
		
		return voteService.addVote(userIdVote, userIdReceive, itemId, voteType, reability, voteMessage);
		
	}
}
