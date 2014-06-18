package es.telocompro.service.user;

import es.telocompro.model.user.User;
import es.telocompro.model.user.UserRepository;
import es.telocompro.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */

@SuppressWarnings(value = "unused")
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User addUser(String login, String password, String firstName, String lastName, String address,
                        String phone, String email) {
        User user = new User(login, password, firstName, lastName, address, phone, email, Role.ROLE_USER, null);
        return userRepository.save(user);
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public User updateUser(Long userId, String firstName, String lastName, String address,
                           String phone, String email) {
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
}
