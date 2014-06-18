package es.telocompro.rest.controller;

import es.telocompro.model.user.User;
import es.telocompro.model.user.UserRepository;
import es.telocompro.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
* Created by @adrian on 14/06/14.
* All rights reserved.
*/

@RestController
//@RequestMapping("/api/users")
@SuppressWarnings("unused")
public class UserController {

    @Autowired
    private UserService userService;

//    @RequestMapping("/")
//    public String index() {
//        return "Greetings from Spring Boot!";
//    }

    @RequestMapping(value="/users/all", method = RequestMethod.GET)
    public List<User> getUsers()
    {
        Iterable<User> all = userService.findAllUsers();
        List<User> list = new ArrayList<User>();
        for (User user: all) list.add(user);
        return list;
    }

    @RequestMapping(value="/users/{userid}", method = RequestMethod.GET)
    public User getUser( @PathVariable("userid") Long userId ) {
        return userService.findUserById(userId);
    }

//    @RequestMapping(value="/users/findbylogin/{login}", method = RequestMethod.GET)
//    public User getUserByLogin( @PathVariable("login") String login ) {
//        return userService.;
//    }

    @RequestMapping(value="/users/user", method=RequestMethod.POST)
    public User addUser( @RequestParam("login") String login, @RequestParam("password") String password,
                         @RequestParam("firstname") String firstname, @RequestParam("lastname") String lastname,
                         @RequestParam("address") String address, @RequestParam("phone") String phone,
                         @RequestParam("email") String email) {
        return userService.addUser(login, password, firstname, lastname, address, phone, email);
    }

}
