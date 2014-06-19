package es.telocompro.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */

//@RestController
@Controller
public class MainController {

//    @RequestMapping(value="/hello/{name}", method = RequestMethod.GET)
//    public String getGreeting(@PathVariable("name") String name) {
//        return "Hello " + name;
//    }

//    @RequestMapping("/hello")
//    public String hello(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
//        model.addAttribute("name", name);
//        return "helloworld";
//    }

    @RequestMapping("/hello")
    public String hello(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "helloworld";
    }

    @RequestMapping("/login")
    public ModelAndView getLoginPage() {
        System.out.println("Login called here");
        return new ModelAndView("hello");
    }

//    @Override
//    @RequestMapping("/login")
//    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
//        System.out.println("handleRequestInternal");
//        return new ModelAndView("hello");
//    }

//    @RequestMapping("/listUsers")
//    public ModelAndView listUsers() {
//        RestTemplate restTemplate = new RestTemplate();
//        String url="http://localhost:8080/users/all";
//        List<LinkedHashMap> users = restTemplate.getForObject(url, List.class);
//        return new ModelAndView("listUsers", "users", users);
//    }
//
//    @RequestMapping("/dispUser/{userid}")
//    public ModelAndView dispUser(@PathVariable("userid") Long userid) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url="http://localhost:8080/users/{userid}";
//        User user = restTemplate.getForObject(url, User.class, userid);
//        return new ModelAndView("dispUser", "user", user);
//    }
//
//
//    @RequestMapping("/dispUser2")
//    public ModelAndView dispUser() {
//        RestTemplate restTemplate = new RestTemplate();
//        String url="http://localhost:8080/users/{userid}";
//        User user = restTemplate.getForObject(url, User.class, (long) 2);
//        return new ModelAndView("dispUser", "user", user);
//    }

}
