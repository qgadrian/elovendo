package es.telocompro.rest.controller;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.ItemRepository;
import es.telocompro.model.province.Province;
import es.telocompro.model.user.User;
import es.telocompro.model.user.UserRepository;
import es.telocompro.rest.controller.exception.LoginNotAvailableException;
import es.telocompro.rest.controller.exception.ProvinceNotFoundException;
import es.telocompro.rest.controller.exception.SubCategoryNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;
import es.telocompro.service.exception.InvalidItemNameMinLenghtException;
import es.telocompro.service.item.ItemService;
import es.telocompro.service.province.ProvinceService;
import es.telocompro.service.user.UserService;
import es.telocompro.util.Constant;
import es.telocompro.util.IOUtil;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.LobCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.orm.hibernate3.SpringSessionContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.core.encoder.EncoderBase;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassPath;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by @adrian on 14/06/14. All rights reserved.
 */


// TODO: Update user (params not required)

@RestController
@SuppressWarnings("unused")
@RequestMapping("/api/zorg/site/") //FIXME: Add /api/ to ALL REST url
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
				address, phone, email, provinceName, avatarBytes);
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
				address, phone, email, provinceName, avatarBytes);
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
	@RequestMapping(value = "items/{username}/item", method = RequestMethod.POST)
	public Item addItem(@PathVariable("username") String userName,
			@RequestParam("subcategory") String subCategoryName,
			@RequestParam("title") String title,
			@RequestParam("description") String description,
			@RequestParam("province") String provinceName,
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
		
		return itemService.addItem(userName, subCategoryName, title,
				description, provinceName, prize, imgBytes);
	}

	/**
	 * Updates an item
	 */
	@RequestMapping(value = "items/{userid}/{itemid}", method = RequestMethod.PUT)
	public Item updateItem(@PathVariable("itemid") Long userId,
			@PathVariable("itemid") Long itemId,
			@RequestParam(value="title", required=false) String title,
			@RequestParam(value="description", required=false) String description,
			@RequestParam(value="prize", required=false, defaultValue="-1") double prize) {
		return itemService.updateItem(itemId, title, description, prize);
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
