package es.telocompro.service.user;

import es.telocompro.model.user.User;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */
public interface UserService {

    // TODO: TEMPORAL
    public Iterable<User> findAllUsers();

    /**
     * Adds an user
     */
    public User addUser(String login, String password, String firstName, String lastName, String address,
                        String phone, String email);

    /**
     * Finds a user by its id
     */
    public User findUserById(Long userId);

    /**
     * Updates an user
     */
    public User updateUser(Long userId, String firstName, String lastName,
                           String address, String phone, String email);

    /**
     * Deletes an user
     */
    public void removeUser(Long userId);
}
