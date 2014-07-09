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
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by @adrian on 14/06/14. All rights reserved.
 */

// TODO: Implement delete user and handle method
// TODO: Update user (params not required)

@RestController
@SuppressWarnings("unused")
@RequestMapping("/site/")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;

	/***************************************/
	/* USER STUFF */
	/***************************************/

	@RequestMapping(value = "users/all", method = RequestMethod.GET)
	public List<User> getUsers() {
		Iterable<User> all = userService.findAllUsers();
		List<User> list = new ArrayList<User>();
		for (User user : all)
			list.add(user);
		return list;
	}

	@RequestMapping(value = "users/{userid}", method = RequestMethod.GET)
	public User getUser(@PathVariable("userid") Long userId) {
		return userService.findUserById(userId);
	}

	// @RequestMapping(value="/users/findbylogin/{login}", method =
	// RequestMethod.GET)
	// public User getUserByLogin( @PathVariable("login") String login ) {
	// return userService.;
	// }

	@RequestMapping(value = "user", method = RequestMethod.POST)
	public User addUser(@RequestParam("login") String login,
			@RequestParam("password") String password,
			@RequestParam("firstname") String firstname,
			@RequestParam("lastname") String lastname,
			@RequestParam("address") String address,
			@RequestParam("phone") String phone,
			@RequestParam("email") String email,
			@RequestParam("province") String provinceName) 
					throws ProvinceNotFoundException, LoginNotAvailableException {
		
		return userService.addUser(login, password, firstname, lastname,
				address, phone, email, provinceName);
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
	 */
	@RequestMapping(value = "{username}/item", method = RequestMethod.POST)
	public Item addItem(@PathVariable("username") String userName,
			@RequestParam("subcategory") String subCategoryName,
			@RequestParam("title") String title,
			@RequestParam("description") String description,
			@RequestParam("province") String provinceName,
			@RequestParam("prize") double prize,
			@RequestParam(value = "image", required = true) MultipartFile file)
			throws InvalidItemNameMinLenghtException, ProvinceNotFoundException, UserNotFoundException, 
				SubCategoryNotFoundException {

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
			@RequestParam("title") String title,
			@RequestParam("description") String description,
			@RequestParam("prize") double prize) {
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

}
