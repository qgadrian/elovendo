package es.telocompro.service.user;

import java.io.IOException;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import es.telocompro.model.user.EditUserForm;
import es.telocompro.model.user.User;
import es.telocompro.model.user.UserForm;
import es.telocompro.model.vote.Vote;
import es.telocompro.rest.exception.EmailNotAvailableException;
import es.telocompro.rest.exception.InvalidSelfVoteException;
import es.telocompro.rest.exception.InvalidVoteUsersException;
import es.telocompro.rest.exception.ItemNotFoundException;
import es.telocompro.rest.exception.LoginNotAvailableException;
import es.telocompro.rest.exception.ProvinceNotFoundException;
import es.telocompro.rest.exception.UserNotFoundException;
import es.telocompro.rest.exception.VoteDuplicateException;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */
public interface UserService extends UserDetailsService {

    // TODO: TEMPORAL
    public Iterable<User> findAllUsers();

    /**
     * Adds an user
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
	public User addUser(String login, String password, String cmpKey,
			String firstName, String lastName, String address, String phone,
			boolean whatssapUser, String email, byte[] avatar)
			throws LoginNotAvailableException, EmailNotAvailableException;
    
    /**
     * Adds an user
     * @param user
     * @param provinceName
     * @return
     * @throws LoginNotAvailableException
     * @throws EmailNotAvailableException 
     * @throws ProvinceNotFoundException
     */
    public User addUser(User user, byte[] profilePicBytes) 
    		throws LoginNotAvailableException, EmailNotAvailableException;
    
    public User addUser(UserForm userForm, MultipartFile userPic) 
    		throws LoginNotAvailableException, EmailNotAvailableException, UserNotFoundException;

    /**
     * Finds a user by its id
     * @param userId
     * @return
     * @throws UserNotFoundException 
     */
    public User findUserById(Long userId) throws UserNotFoundException;
    
    /**
     * Finds a user by its login
     * @param login
     * @return
     * @throws UserNotFoundException 
     */
    public User findUserByLogin(String login) throws UserNotFoundException;
    
    public User findUserByEmail(String email) throws UserNotFoundException;
    
    public User findUserBySocialUserKey(String compositeKey) throws UserNotFoundException;

    /**
     * Updates an user
     * @param userId
     * @param firstName
     * @param lastName
     * @param address
     * @param phone
     * @param email
     * @return
     * @throws UserNotFoundException
     */
    public User updateUser(long userId, String password, String firstName, String lastName,
                           String address, String phone, String email, byte[] avatar) 
                        		   throws UserNotFoundException;
    
    /**
     * Updates an user
     * @param user
     * @return
     */
    public User updateUser(User user);
    
    public User updateUser(User user, byte[] profilePic);
    
    public User updateUser(EditUserForm userForm, long userId, MultipartFile userPic) throws UserNotFoundException;
    /**
     * Deletes an user
     * @param userId
     * @throws UserNotFoundException
     */
    public void removeUser(Long userId) throws UserNotFoundException;
    
    /**
     * Emits a vote from an user to another
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
     */
	public Vote voteUser(Long userIdVote, Long userIdReceive, Long itemId,
			int voteType, String voteMessage) throws UserNotFoundException,
			ItemNotFoundException, VoteDuplicateException,
			InvalidVoteUsersException, InvalidSelfVoteException;

	public int getVotesPositive(User user);

	public int getVotesNegative(User user);

	public int getVotesQueued(User user);
	
}
