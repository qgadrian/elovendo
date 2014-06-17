package es.telocompro.rest;

import es.telocompro.model.user.User;
import es.telocompro.model.user.UserRepository;
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
@SuppressWarnings("unused")
public class UserController {

    @Autowired
    private UserRepository userRepository;

//    @RequestMapping("/")
//    public String index() {
//        return "Greetings from Spring Boot!";
//    }

//    @RequestMapping(value="/users", method = RequestMethod.GET)
//    public List<User> getUsers()
//    {
//        Iterable<User> all = userRepository.findAll();
//        List<User> list = new ArrayList<User>();
//        for (User user: all) list.add(user);
//        return list;
//    }

    @RequestMapping(value="/users/{userid}", method = RequestMethod.GET)
    public User getUser( @PathVariable("userid") long userId ) {
        return userRepository.findOne(userId);
    }

    @RequestMapping(value="/users/findbylogin/{login}", method = RequestMethod.GET)
    public User getUserByLogin( @PathVariable("login") String login ) {
        return userRepository.findByLogin(login);
    }

    @RequestMapping(value="/users/add", method=RequestMethod.POST)
    public User addUser( @RequestParam("login") String login, @RequestParam("firstname") String firstname,
                         @RequestParam("lastname") String lastname, @RequestParam("address") String address,
                         @RequestParam("phone") String phone, @RequestParam("email") String email) {
        return userRepository.save( new User(login, firstname, lastname, address, phone, email) );
    }

}
