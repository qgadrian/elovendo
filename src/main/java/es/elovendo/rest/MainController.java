package es.elovendo.rest;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import es.elovendo.model.item.category.Category;
import es.elovendo.model.item.category.subcategory.SubCategory;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.service.exception.social.NoEmailProvidedException;
import es.elovendo.service.exception.social.NotKnownProviderException;
import es.elovendo.service.item.ItemService;
import es.elovendo.service.item.category.CategoryService;
import es.elovendo.service.user.UserService;
import es.elovendo.util.Constant;
import es.elovendo.util.RoleEnum;
import es.elovendo.util.SessionUserObtainer;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */

@Controller
public class MainController implements ErrorController {
	
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private Facebook facebook;
    
    static Logger logger = Logger.getLogger(MainController.class.getName());
	
	/**
	 * 
	 * USER SESSION MANAGEMENT
	 * 
	 */
    
	@RequestMapping(value = "/login")
	public String loginPage(HttpSession session, HttpServletRequest request, Device device) {
		
//		BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
//		System.out.println("~~~~~~~~~~~~~~~~~~");
//		System.out.println("~~~~~~~~~~~~~~~~~~");
//		System.out.println("~~~~~~~~~~~~~~~~~~");
//		System.out.println("~~~~~~~~~~~~~~~~~~");
//		System.out.println(enc.encode("admin"));
//		System.out.println("~~~~~~~~~~~~~~~~~~");
//		System.out.println("~~~~~~~~~~~~~~~~~~");
//		System.out.println("~~~~~~~~~~~~~~~~~~");
//		System.out.println("~~~~~~~~~~~~~~~~~~");

		if (device.isNormal()) {
			String referrer = request.getParameter("referrer");
			if (referrer != null) session.setAttribute("referrer", referrer);
			return "elovendo/login";
		}
		
		return "";
	}
	
//	@RequestMapping(value = "/login-error")
//	public String loginErrorPage(HttpSession session, HttpServletRequest request, Model model, Device device) {
//		
//		model.addAttribute("error", true);
//		
//		return "elovendo/login";
//	}

	@RequestMapping(value = "/loginRedirect")
	@ResponseStatus(HttpStatus.OK)
	// TODO Testear esta notación
	public String login(Model model, Device device, Principal principal,
			HttpSession session) {

		if (device.isNormal()) {
			String referrer = (String) session.getAttribute("referrer");
			// logger.debug("Referrer is " + referrer);
			if (referrer != null) {
				session.removeAttribute("referrer");
				return "redirect:" + referrer;
			}
			else {
				User user = SessionUserObtainer.getInstance().getSessionUser();
				if (user.getRole().equals(RoleEnum.ROLE_ADMIN))
					return "redirect:private/admin/home";
				return "redirect:elovendo/index";
			}
				
		} 
		
		return "";
    }
    
    @RequestMapping(value="/logout")
    public String logout(HttpServletResponse response, Device device) {
    	System.out.println("logout controller");
    	
    	SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    	Cookie cookie = new Cookie("JSESSIONID", null);
    	response.addCookie(cookie);
    	
    	if (device.isNormal()) return "redirect:/elovendo/index";
    	
    	return "";
    }
    
    @RequestMapping(value={"/signup", "/signin"})
    public String signupRedirect(WebRequest request) throws UserNotFoundException, NotKnownProviderException {
    	
//    	User user = null;
//		SecurityContext context = SecurityContextHolder.getContext();
//		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
//			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils();
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
//		UserProfile socialMediaProfile = connection.fetchUserProfile();
		
		logger.error("received login " + connection.getProfileUrl());
		
		try {
			
			ConnectionKey connectionKey = connection.getKey();
			String compositeKey = connectionKey.getProviderUserId() + connectionKey.getProviderId();
			User socialUser = userService.findUserBySocialUserKey(compositeKey);
			if (socialUser != null) {
				Authentication authentication = new UsernamePasswordAuthenticationToken(socialUser, null,
						socialUser.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			
			return "redirect:/index";
		// Catch if user doesn't exists and register a new one
		} catch(UserNotFoundException e) {
			
			try {
				if (connection != null && connection.test()) {
					User socialUser = userService.addSocialUser(connection);
					Authentication authentication = new UsernamePasswordAuthenticationToken(socialUser, null,
							socialUser.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (NoEmailProvidedException err) {
				logger.info("Social user not allowed email access");
			}
			
			return "redirect:/index";
		}
    }
    
    /** 
     * 
     * WEB PAGES
     * 
     */
    
    @RequestMapping(value={"/elovendo/index", "/", "/index"}, method = {RequestMethod.GET,  RequestMethod.HEAD})
    public String indexPage(Model model, HttpSession session) {
    	
    	model.addAttribute("featuredItems", itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, null));
    	
    	// User
    	User user = null;
    	SecurityContext context = SecurityContextHolder.getContext();
    	if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken)) {
    		user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		session.setAttribute("userName", user.getLogin());
    		session.setAttribute("socialUser", user.isSocialUser());
    		if (user.isSocialUser()) {
    			session.setAttribute("socialAvatar", user.getSocialAvatar());
    			session.setAttribute("largeSocialAvatar", user.getLargeSocialAvatar());
    		}
    		else session.setAttribute("userProfilePicMin", user.getAvatar200h());
    	}
//    	model.addAttribute("user", user);
    	
    	@SuppressWarnings("unchecked")
		List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
		model.addAttribute("categories", categories);
    	
    	// SubCategories
    	@SuppressWarnings("unchecked")
		List<SubCategory> subCategories = IteratorUtils.toList(
				categoryService.getAllSubCategories().iterator());
    	model.addAttribute("subCategories", subCategories);
    	
        return "elovendo/index";
    }
    
	@RequestMapping(value="/about")
    public String aboutPage() {
		return "/elovendo/about";
    }
    
    /** ERRORS */
    
    @RequestMapping(value="/error")
    public String errorPage() {
    	return "elovendo/error/error";
    }
    
    @RequestMapping(value="/error404")
    public String error404Page() {
    	return "elovendo/error/error404";
    }
    
    @RequestMapping(value="/error401")
    public String error401Page() {
    	return "elovendo/error/error401";
    }
    
    @RequestMapping(value="/error403")
    public String error403Page() {
    	return "elovendo/error/error403";
    }

	@Override
	public String getErrorPath() {
		return "/elovendo/error";
	}

}