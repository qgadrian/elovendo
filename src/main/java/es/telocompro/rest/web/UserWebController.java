package es.telocompro.rest.web;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.telocompro.model.item.Item;
import es.telocompro.model.user.User;
import es.telocompro.rest.controller.exception.LoginNotAvailableException;
import es.telocompro.rest.controller.exception.ProvinceNotFoundException;
import es.telocompro.rest.controller.exception.SubCategoryNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;
import es.telocompro.service.exception.InvalidItemNameMinLenghtException;
import es.telocompro.service.item.ItemService;
import es.telocompro.service.user.UserService;

@Controller
@SuppressWarnings("unused")
@RequestMapping("/site/")
public class UserWebController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;
	
	/** Add a new user **/
	@RequestMapping(value = "user", method = RequestMethod.POST)
	public String processAddUserWeb(@ModelAttribute(value="user") User user, 
			@ModelAttribute(value="provinceName") String provinceName,
			@RequestParam("profilePic") MultipartFile profilePic) 
					throws ProvinceNotFoundException, LoginNotAvailableException {
		
		if (profilePic != null) System.out.println("profile pic IS NOT NULL");
    	else System.out.println("sadly, profile pic its null, i'm sorry dude....");
		
		byte[] profilePicBytes = null;
		try {
			profilePicBytes = profilePic.getBytes();
		} catch (IOException e) {
			System.out.println("Error converting to bytes image file");
		}
		
		userService.addUser(user, provinceName, profilePicBytes);
		
		return "elovendo/user/registered_successful";
	}
	
	/**
	 * ITEMS REFERREAL
	 */
	@RequestMapping(value = "items/item", method = RequestMethod.POST)
	public String processAddItemWeb(@ModelAttribute(value="item") Item item, 
			@ModelAttribute(value="provinceName") String provinceName,
			@ModelAttribute(value="categoryName") String categoryName,
			@ModelAttribute(value="subCategoryName") String subCategoryName,
			@RequestParam("image1") MultipartFile profilePic)
			throws InvalidItemNameMinLenghtException, ProvinceNotFoundException, UserNotFoundException, 
				SubCategoryNotFoundException, IOException {

		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
			System.out.println("User is not authenticated!!!!!");
		else
			System.out.println("user ocrrectly authenticated :)");
		
		byte[] imgBytes = null;

		try {
			imgBytes = profilePic.getBytes();
		} catch (IOException e) {
			System.out.println("Error converting to bytes image file");
		}
		
		itemService.addItem(item, subCategoryName, provinceName, imgBytes);
		
		return "elovendo/item/item_create_successful";
	}

}
