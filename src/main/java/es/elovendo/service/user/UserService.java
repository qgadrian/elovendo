package es.elovendo.service.user;

import java.io.IOException;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.Connection;
import org.springframework.web.multipart.MultipartFile;

import es.elovendo.model.user.EditUserForm;
import es.elovendo.model.user.User;
import es.elovendo.model.user.UserForm;
import es.elovendo.model.vote.Vote;
import es.elovendo.rest.exception.EmailNotAvailableException;
import es.elovendo.rest.exception.InvalidSelfVoteException;
import es.elovendo.rest.exception.InvalidVoteException;
import es.elovendo.rest.exception.InvalidVoteUsersException;
import es.elovendo.rest.exception.ItemNotFoundException;
import es.elovendo.rest.exception.LoginNotAvailableException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.rest.exception.VoteDuplicateException;
import es.elovendo.service.exception.EmailNotFoundException;
import es.elovendo.service.exception.social.NoEmailProvidedException;
import es.elovendo.service.exception.social.NotKnownProviderException;

/**
 * Created by @adrian on 17/06/14. All rights reserved.
 */
public interface UserService extends UserDetailsService {

	// TODO: TEMPORAL
	public Iterable<User> findAllUsers();

	/**
	 * Adds an user
	 * 
	 * @param login
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param address
	 * @param phone
	 * @param email
	 * @param provinceName
	 * @return
	 * @throws LoginNotAvailableException
	 * @throws EmailNotAvailableException
	 * @throws ProvinceNotFoundException
	 * @throws IOException
	 */
	public User addUser(String login, String password, String cmpKey, String firstName, String lastName, String phone,
			boolean whatssapUser, String email, byte[] avatar) throws LoginNotAvailableException,
			EmailNotAvailableException;

	/**
	 * Adds an user
	 * 
	 * @param user
	 * @param provinceName
	 * @return
	 * @throws LoginNotAvailableException
	 * @throws EmailNotAvailableException
	 * @throws ProvinceNotFoundException
	 */
	public User addUser(User user, byte[] profilePicBytes) throws LoginNotAvailableException,
			EmailNotAvailableException;

	public User addUser(UserForm userForm, MultipartFile userPic) throws LoginNotAvailableException,
			EmailNotAvailableException;

	/* Add a social user */
	public User addSocialUser(Connection<?> connection) throws NotKnownProviderException, NoEmailProvidedException;

	/**
	 * Get ONLY an email from the given user
	 * 
	 * @param userId
	 * @return User's email
	 * @throws EmailNotFoundException
	 */
	public String getUserEmailFromUserId(long userId) throws EmailNotFoundException;

	/**
	 * Finds a user by its id
	 * 
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 */
	public User findUserById(Long userId) throws UserNotFoundException;

	/**
	 * Finds a user by its login
	 * 
	 * @param login
	 * @return
	 * @throws UserNotFoundException
	 */
	public User findUserByLogin(String login) throws UserNotFoundException;

	public User findUserByEmail(String email) throws UserNotFoundException;

	public User findUserBySocialUserKey(String compositeKey) throws UserNotFoundException;

	/**
	 * Updates an user
	 * 
	 * @param userId
	 * @param firstName
	 * @param lastName
	 * @param address
	 * @param phone
	 * @param email
	 * @return
	 * @throws UserNotFoundException
	 */
	public User updateUser(long userId, String password, String firstName, String lastName, String address,
			String phone, String email, byte[] avatar) throws UserNotFoundException;

	/**
	 * Updates an user
	 * 
	 * @param user
	 * @return
	 */
	public User updateUser(User user);

	public User updateUser(User user, byte[] profilePic);

	public User updateUser(EditUserForm userForm, long userId, MultipartFile userPic) throws UserNotFoundException;

	/**
	 * Disables an user, but don't delete it
	 * 
	 * @param userId
	 * @throws UserNotFoundException
	 */
	public void disableUser(Long userId) throws UserNotFoundException;

	/**
	 * Enable an user who is actually "disabled"
	 * 
	 * @param userId
	 * @throws UserNotFoundException
	 */
	public void enableUser(Long userId) throws UserNotFoundException;

	/**
	 * Deletes an user
	 * 
	 * @param userId
	 * @throws UserNotFoundException
	 */
	public void deleteUser(Long userId);

	/**
	 * Deletes an user
	 * 
	 * @param user
	 */
	public User deleteUser(User user);

	/**
	 * Emits a vote from an user to another
	 * 
	 * @param userIdVote
	 * @param userIdReceive
	 * @param itemId
	 * @param voteType
	 * @param voteMessage
	 * @throws UserNotFoundException
	 * @throws ItemNotFoundException
	 * @throws VoteDuplicateException
	 * @throws InvalidVoteUsersException
	 * @throws InvalidSelfVoteException
	 * @throws InvalidVoteException 
	 */
	public Vote voteUser(Long userIdVote, Long userIdReceive, Long itemId, int voteType, String voteMessage)
			throws UserNotFoundException, ItemNotFoundException, VoteDuplicateException, InvalidVoteUsersException,
			InvalidSelfVoteException, InvalidVoteException;

	public int getVotesPositive(User user);

	public int getVotesNegative(User user);

	public int getVotesQueued(User user);

	/** Helpers **/
	public boolean isEmailAvailable(String email);
}
