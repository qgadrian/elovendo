package es.telocompro.service.user;

import java.io.IOException;

import org.springframework.security.core.userdetails.UserDetailsService;

import es.telocompro.model.user.User;
import es.telocompro.model.vote.Vote;
import es.telocompro.rest.controller.exception.InvalidVoteUsersException;
import es.telocompro.rest.controller.exception.ItemNotFoundException;
import es.telocompro.rest.controller.exception.LoginNotAvailableException;
import es.telocompro.rest.controller.exception.ProvinceNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;
import es.telocompro.rest.controller.exception.VoteDuplicateException;

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
     * @throws ProvinceNotFoundException
     * @throws IOException 
     */
    public User addUser(String login, String password, String firstName, String lastName, String address,
                        String phone, String email, String provinceName, byte[] avatar) 
                        		throws LoginNotAvailableException, ProvinceNotFoundException;
    
    /**
     * Adds an user
     * @param user
     * @param provinceName
     * @return
     * @throws LoginNotAvailableException
     * @throws ProvinceNotFoundException
     */
    public User addUser(User user, String provinceName, byte[] profilePicBytes) 
    		throws LoginNotAvailableException, ProvinceNotFoundException;

    /**
     * Finds a user by its id
     * @param userId
     * @return
     */
    public User findUserById(Long userId);
    
    /**
     * Finds a user by its login
     * @param login
     * @return
     */
    public User findUserByLogin(String login);

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
                           String address, String phone, String email, String provinceName, 
                           byte[] avatar) throws ProvinceNotFoundException, UserNotFoundException;
    
    /**
     * Updates an user
     * @param user
     * @return
     */
    public User updateUser(User user);

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
     */
    public Vote voteUser(Long userIdVote, Long userIdReceive, Long itemId, int voteType, String voteMessage) 
    		throws UserNotFoundException, ItemNotFoundException, VoteDuplicateException, InvalidVoteUsersException;
}
