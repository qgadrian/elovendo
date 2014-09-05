package es.telocompro.rest.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import es.telocompro.util.Constant;

/**
 * Created by @adrian on 14/06/14. All rights reserved.
 */


// TODO: Update user (params not required)

@RestController
@SuppressWarnings("unused")
@RequestMapping(Constant.MOBILE_API_URL_PREFIX_V1 + "site/") 
//FIXME: Add /api/ to ALL REST url
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;

	/***************************************/
	/* USER STUFF */
	/***************************************/

	@RequestMapping(value = "users/{userid}", method = RequestMethod.GET)
	public User getUser(@PathVariable("userid") Long userId) {
		return userService.findUserById(userId);
	}

	// @RequestMapping(value="/users/findbylogin/{login}", method =
	// RequestMethod.GET)
	// public User getUserByLogin( @PathVariable("login") String login ) {
	// return userService.;
	// }

	/** Add a new user 
	 * @throws IOException **/
	@RequestMapping(value = "user", method = RequestMethod.POST)
	public User addUser(@RequestParam("login") String login,
			@RequestParam("password") String password,
			@RequestParam("firstname") String firstname,
			@RequestParam("lastname") String lastname,
			@RequestParam(value="address", required=false) String address,
			@RequestParam("phone") String phone,
			@RequestParam("email") String email,
			@RequestParam("province") String provinceName,
			@RequestParam(value="avatar", required=false) MultipartFile avatar) 
					throws ProvinceNotFoundException, LoginNotAvailableException {
		
		byte[] avatarBytes = null;
		try {
			avatarBytes = avatar.getBytes();
		} catch (IOException e) {
			System.out.println("Error converting to bytes image file");
		}
		
		return userService.addUser(login, password, firstname, lastname,
				address, phone, email, avatarBytes);
	}
	
	/** Add a new user 
	 * @throws UserNotFoundException 
	 * @throws IOException **/
	@RequestMapping(value = "update/{userId}", method = RequestMethod.POST)
	public User updateUser(@PathVariable("userId") long userId,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "firstname", required=false) String firstname,
			@RequestParam(value = "lastname", required=false) String lastname,
			@RequestParam(value="address", required=false) String address,
			@RequestParam(value = "phone", required=false) String phone,
			@RequestParam(value = "email", required=false) String email,
			@RequestParam(value = "province", required=false) String provinceName,
			@RequestParam(value="avatar", required=false) MultipartFile avatar) 
					throws ProvinceNotFoundException, LoginNotAvailableException, UserNotFoundException {
		
		byte[] avatarBytes = null;
		if (avatar != null) {
			String avatarBase64 = null;
			try {
				avatarBytes = avatar.getBytes();
			} catch (IOException e) {
				System.out.println("Error converting to bytes image file");
			}
		}
		
		return userService.updateUser(userId, password, firstname, lastname,
				address, phone, email, avatarBytes);
	}
	
	@RequestMapping(value = "delete/{userId}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable("userId") long userId) throws UserNotFoundException {
		userService.removeUser(userId);
	}

	/***************************************/
	/* ITEMS REFERREAL */
	/***************************************/
	
	/**
	 * Add an item
	 * 
	 * @throws InvalidItemNameMinLenghtException
	 * @throws ProvinceNotFoundException
	 * @throws SubCategoryNotFoundException 
	 * @throws UserNotFoundException 
	 * @throws IOException 
	 */
	// TODO: featured, highlight... etc...
	@RequestMapping(value = "items/{username}/item", method = RequestMethod.POST)
	public Item addItem(@PathVariable("username") String userName,
			@RequestParam("subcategory") double subCategoryId,
			@RequestParam("title") String title,
			@RequestParam("description") String description,
			@RequestParam(value="featured", required=false, defaultValue="false") boolean featured,
			@RequestParam(value="highlight", required=false, defaultValue="false") boolean highlight,
			@RequestParam(value="latitude", required=false, defaultValue="0") String latitude,
			@RequestParam(value="longitude", required=false, defaultValue="0") String longitude,
			@RequestParam("prize") double prize,
			@RequestParam("image") MultipartFile file)
			throws InvalidItemNameMinLenghtException, ProvinceNotFoundException, UserNotFoundException, 
				SubCategoryNotFoundException, IOException {

		byte[] imgBytes = null;

		try {
			imgBytes = file.getBytes();
		} catch (IOException e) {
			System.out.println("Error converting to bytes image file");
		}
		
//		Point location = null; //TODO
//				new Point(Double.valueOf(latitude), Double.valueOf(longitude));
		
		return itemService.addItem(userName, (long) subCategoryId, title, description, prize, 
				imgBytes, null, null, null, "youtubeVideo", featured, highlight, latitude, longitude);
	}

	/**
	 * Updates an item
	 */
	@RequestMapping(value = "items/{userid}/{itemid}", method = RequestMethod.PUT)
	public Item updateItem(@PathVariable("itemid") Long userId,
			@PathVariable("itemid") Long itemId,
			@RequestParam(value="title", required=false) String title,
			@RequestParam(value="description", required=false) String description,
			@RequestParam(value="prize", required=false, defaultValue="-1") double prize,
			@RequestParam(value="renew", required=false, defaultValue="false") boolean renew,
			@RequestParam(value="featured", required=false, defaultValue="false") boolean featured,
			@RequestParam(value="highlight", required=false, defaultValue="false") boolean highlight) {
		return itemService.updateItem(itemId, title, description, prize, renew, featured, highlight);
	}

	/**
	 * Deletes an item
	 */
	@RequestMapping(value = "items/{itemid}", method = RequestMethod.DELETE)
	public void deleteItem(@RequestParam("itemid") Long itemId) {
		itemService.deleteItem(itemId);
	}
	
	/***************************************/
	/* EXCEPTIONS HANDLERS */
	/***************************************/

	@ExceptionHandler({ProvinceNotFoundException.class, UserNotFoundException.class, 
		SubCategoryNotFoundException.class, InvalidItemNameMinLenghtException.class, 
		LoginNotAvailableException.class})
	protected String handleNoteNotFoundException(
			Exception exception, HttpServletResponse response) {
		try {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return exception.getMessage();
	}
	
	@ExceptionHandler({IOException.class})
	protected String handleNoteIOException(
			Exception exception, HttpServletResponse response) {
		try {
			response.sendError(HttpServletResponse.SC_NO_CONTENT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return exception.getMessage();
	}

}
