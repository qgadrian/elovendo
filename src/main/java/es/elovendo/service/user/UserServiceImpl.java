package es.elovendo.service.user;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import es.elovendo.model.item.Item;
import es.elovendo.model.user.EditUserForm;
import es.elovendo.model.user.User;
import es.elovendo.model.user.UserForm;
import es.elovendo.model.user.UserRepository;
import es.elovendo.model.user.role.Role;
import es.elovendo.model.user.role.RoleRepository;
import es.elovendo.model.vote.Vote;
import es.elovendo.rest.exception.EmailNotAvailableException;
import es.elovendo.rest.exception.InvalidSelfVoteException;
import es.elovendo.rest.exception.InvalidVoteUsersException;
import es.elovendo.rest.exception.ItemNotFoundException;
import es.elovendo.rest.exception.LoginNotAvailableException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.rest.exception.VoteDuplicateException;
import es.elovendo.service.item.ItemService;
import es.elovendo.service.item.ItemServiceImpl;
import es.elovendo.service.vote.VoteService;
import es.elovendo.util.IOUtil;
import es.elovendo.util.RoleEnum;

/**
 * Created by @adrian on 17/06/14. All rights reserved.
 */

@Service("userService")
@Transactional(readOnly = false)
public class UserServiceImpl implements UserService {
	
	Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	VoteService voteService;
	@Autowired
	ItemService itemService;

	@Override
	public Iterable<User> findAllUsers() {
		return userRepository.findAll();
	}

	//TODO: Both clases should be reversed, pass an user and do stuff there, and the below function just create 
	// an user and pass it
	// BE CAREFULL WITH USER DISABLED BY DEFAULT
	@Override
	public User addUser(String login, String password, String cmpKey, String firstName, String lastName, 
			String address, String phone, boolean whatssapUser, String email, byte[] avatar) 
					throws LoginNotAvailableException, EmailNotAvailableException {
		
		User user = userRepository.findByLogin(login); 
		if (user != null ) throw new LoginNotAvailableException(login);
		else if (userRepository.findByEmail(email) != null )
			throw new EmailNotAvailableException(login);
		
		// At this point, all new users always will be ROLE_USER
		Role role = roleRepository.findByRoleName(RoleEnum.ROLE_USER);
		
		// Create user (notice the null for avatar path)
		user = new User(login, password, cmpKey, firstName, lastName, address,
				phone, whatssapUser, email, null, role, null);

		user = userRepository.save(user);
		user.setAvatar(saveUserImage(user, avatar));
//		/** SAVE AVATAR IMAGE IN THE RESOURCE FOLDER **/
//		if (avatar != null) try {
//			// Save user for get an userId
//			user = userRepository.save(user);
//			// Create folder (if not created) for /img/avatars/{userId}.jpg
//			File folderPath = new File(IOUtil.calculateAvatarFilePath());
//			folderPath.mkdirs();
//			// Get buffered image
//			BufferedImage buffImg = ImageIO.read(new ByteArrayInputStream(avatar));
//			// Create file
//			String filePath = folderPath.getAbsolutePath()+"/"+user.getUserId()+".jpg";
//			File imgFile = new File(filePath);
//			// Write image in file
//			ImageIO.write(buffImg, "jpg", imgFile);
//			
//			/* IMAGE RESIZED */
//			BufferedImage resizedImage = Scalr.resize(buffImg, 500);
//			// Create file
//			File imgResizedFile = new File(IOUtil.calculateAvatarFilePath()+"/"+user.getUserId()+"-200h.jpg");
//			// Write image in file
//			ImageIO.write(resizedImage, "jpg", imgResizedFile);
//			
//			// Assign the avatar path to the user and save it
//			String urlImagePath = IOUtil.calculateAvatarFilePath() + "/" + user.getUserId();
//			user.setAvatar(urlImagePath);
//		} catch(NullPointerException e) { } catch (IOException e) {
//			System.out.println("ERROR: Creating avatar resource for user " + user.getUserId());
//			e.printStackTrace();
//		}
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		if (password != null) user.setPassword(encoder.encode(password));
		
		// return the updated user with the avatar path asigned
		return userRepository.save(user);
	}
	
	@Override
	public User addUser(User user, byte[] profilePicBytes) throws LoginNotAvailableException, EmailNotAvailableException {
		
//		String login = user.getLogin();
//		
//		if (provinceName == null)
//			throw new ProvinceNotFoundException("empty province");
//		Province province = provinceService.findProvinceByName(provinceName);
//		
//		System.out.println("searched for " + login + " as login and province " + provinceName + "!");
//		
//		if (findUserByLogin(login) != null ) 
//			throw new LoginNotAvailableException(login);
//		if (province == null ) 
//			throw new ProvinceNotFoundException(provinceName);
//		
//		Role role = roleRepository.findByRoleName(RoleEnum.ROLE_USER);
//		
//		user.setRole(role);
//		user.setProvince(province);
//		
//		return userRepository.save(user);
		
		return addUser(user.getLogin(), user.getPassword(), user.getSocialCompositeKey(),
				user.getFirstName(), user.getLastName(), user.getAddress(), user.getPhone(), 
				user.isWhatssapUser(), user.getEmail(), profilePicBytes);
	}

	@Override
	public User addUser(UserForm userForm, MultipartFile userPic) throws LoginNotAvailableException,
			EmailNotAvailableException, UserNotFoundException {
		
		try { 
			findUserByLogin(userForm.getLogin());
			throw new LoginNotAvailableException(userForm.getLogin());
		} catch (UserNotFoundException e) {}
		
		try {
			findUserByEmail(userForm.getEmail());
			throw new EmailNotAvailableException(userForm.getEmail());
		} catch (UserNotFoundException e) {}
		
		// At this point, all new users always will be ROLE_USER
		Role role = roleRepository.findByRoleName(RoleEnum.ROLE_USER);
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = userForm.getPassword();
		if (password != null) userForm.setPassword(encoder.encode(password));
		
		User user = new User(userForm.getLogin(), userForm.getPassword(), null, userForm.getFirstName(),
				userForm.getLastName(), userForm.getAddress(), userForm.getPhone(), userForm.isWhatssapUser(),
				userForm.getEmail(), null, role, null);
		
		user = userRepository.save(user);
		saveMultiPartFileImage(user, userPic);
		
		return user;
	}
	
	@Override
	public User updateUser(EditUserForm userForm, long userId, MultipartFile userPic) throws UserNotFoundException {
		User user = findUserById(userId);
		
		// Password might be empty (unchanged). If not, change it
		if (!userForm.getPassword().isEmpty()) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String password = userForm.getPassword();
			user.setPassword(encoder.encode(password));
		}
		
		user.setFirstName(userForm.getFirstName());
		user.setLastName(userForm.getLastName());
		user.setPhone(userForm.getPhone());
		user.setWhatssapUser(userForm.isWhatssapUser());
		user.setEmail(userForm.getEmail());
		user.setAddress(userForm.getAddress());
		
		saveMultiPartFileImage(user, userPic);
		
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public User updateUser(long userId, String password, String firstName, String lastName,
			String address, String phone, String email, byte[] avatar) throws  UserNotFoundException {
		
		User user = userRepository.findOne(userId);
		
		if (user == null) throw new UserNotFoundException(userId);
		
		if (password != null)
			user.setPassword(password);
		if (firstName != null)
			user.setFirstName(firstName);
		if (lastName != null)
			user.setLastName(lastName);
		if (address != null)
			user.setAddress(address);
		if (phone != null)
			user.setPhone(phone);
		if (email != null)
			user.setEmail(email);
		
		// TODO: Maybe check if something changed, and if not, do not call database write
		return userRepository.save(user);
	}
	
	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}
	
	@Override
	public User updateUser(User user, byte[] profilePic) {
		if (profilePic != null) user.setAvatar(saveUserImage(user, profilePic));
		return userRepository.save(user);
	}

	@Override
	public void removeUser(Long userId) throws UserNotFoundException {
		if (findUserById(userId) == null) throw new UserNotFoundException(userId);
		userRepository.delete(userId);
	}

	// TODO Dummy role added temporarily until next example
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByLogin(username);
		
		if (user == null) 
			throw new UsernameNotFoundException("Username not found => " 
					+ UserService.class.getName());
		
		return user;
	}

	// TODO: If Paypal, increase the reability value for users
	@Override
	public Vote voteUser(Long userIdVote, Long userIdReceive, Long itemId, int voteType, String voteMessage) 
			throws UserNotFoundException, ItemNotFoundException, VoteDuplicateException, InvalidVoteUsersException, 
			InvalidSelfVoteException {
		User userVote = userRepository.findOne(userIdVote);
		User userReceive = userRepository.findOne(userIdReceive);
		Item item = itemService.getItemById(itemId);
		
		if (userVote == null) throw new UserNotFoundException(userIdVote);
		if (userReceive == null) throw new UserNotFoundException(userIdReceive);
		if (userVote.equals(userReceive)) throw new InvalidSelfVoteException(userVote.getUserId());
		if (item == null) throw new ItemNotFoundException(itemId);
		
		// Check if user already voted for this item
		Vote vote = voteService.getVote(userVote, userReceive, item);
		if (vote != null) throw new VoteDuplicateException(userIdVote, userIdReceive, itemId);
		
		
		// Check reability for user who is voting
		float reability;
		if (userVote.getUserValue() >= 95) reability = 1.5f;
		else if (userVote.getUserValue() >= 90) reability = 1f;
		else if (userVote.getUserValue() >= 80) reability = 0.8f;
		else if (userVote.getUserValue() >= 70) reability = 0.5f;
		else if (userVote.getUserValue() >= 60) reability = 0.2f;
		else if (userVote.getUserValue() >= 10) reability = 0.1f;
		else reability = 0f;
		
		// TODO: Antique users will have a plus
		// TODO: If users talk by messaging will have a plus
		
		return voteService.addVote(userIdVote, userIdReceive, itemId, voteType, reability, voteMessage);
		
	}

	@Override
	public int getVotesPositive(User user) {
		return voteService.getNumberVotesPositive(user.getUserId());
	}

	@Override
	public int getVotesNegative(User user) {
		return voteService.getNumberVotesNegative(user.getUserId());
	}


	@Override
	public int getVotesQueued(User user) {
		return voteService.getNumberVotesQueued(user.getUserId());
	}

	@Override
	public User findUserByEmail(String email) throws UserNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null) throw new UserNotFoundException(email);
		return user;
	}
	
	@Override
	public User findUserById(Long userId) throws UserNotFoundException {
		User user = userRepository.findOne(userId);
		if (user == null) throw new UserNotFoundException(userId);
		return user;
	}

	@Override
	public User findUserByLogin(String login) throws UserNotFoundException {
		User user = userRepository.findByLogin(login);
		if (user == null) throw new UserNotFoundException(login);
		return user;
	}

	@Override
	public User findUserBySocialUserKey(String compositeKey)
			throws UserNotFoundException {
		User user = userRepository.findUserBySocialUserKey(compositeKey);
		if (user == null) throw new UserNotFoundException(compositeKey);
		
		return user;
	}
	
	/**
	 * Gets {@link MultipartFile} image data, saves it to a file and updates {@link User} information with file path
	 * @param user
	 * @param file
	 */
	private void saveMultiPartFileImage(User user, MultipartFile file) {
		if (!file.isEmpty()) {
			byte[] bytes = null;
			try {
				bytes = file.getBytes(); // Main image
			} catch (IOException e) {
				logger.debug("Error converting image");
			}
			user.setAvatar(saveUserImage(user, bytes));
		}
	}
	
	/**
	 * Saves image into a user's image folders, and returns the file path.
	 * @param user
	 * @param profilePic
	 * @return File path for image bytes saved.
	 */
	private String saveUserImage(User user, byte[] profilePic) {
		if (profilePic != null) try {
			// Create folder (if not created) for /img/avatars/{userId}.jpg
			File folderPath = new File(IOUtil.calculateAvatarFilePath());
			folderPath.mkdirs();
			// Get buffered image
			BufferedImage buffImg = ImageIO.read(new ByteArrayInputStream(profilePic));
			// Create file
			String filePath = folderPath.getAbsolutePath()+"/"+user.getUserId()+".jpg";
			File imgFile = new File(filePath);
			// Write image in file
			ImageIO.write(buffImg, "jpg", imgFile);
			
			/* IMAGE RESIZED */
			BufferedImage resizedImage = Scalr.resize(buffImg, 500);
			// Create file
			File imgResizedFile = new File(IOUtil.calculateAvatarFilePath()+"/"+user.getUserId()+"-200h.jpg");
			// Write image in file
			ImageIO.write(resizedImage, "jpg", imgResizedFile);
			
			// Assign the avatar path to the user and save it
			String urlImagePath = IOUtil.calculateAvatarFilePath() + "/" + user.getUserId();
			return urlImagePath;
			
		} catch(NullPointerException e) { } catch (IOException e) {
			System.out.println("ERROR: Creating avatar resource for user " + user.getUserId());
			e.printStackTrace();
		}
		return null;
	}
}
