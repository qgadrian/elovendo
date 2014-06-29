package es.telocompro.rest.controller;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.ItemRepository;
import es.telocompro.model.user.User;
import es.telocompro.model.user.UserRepository;
import es.telocompro.service.exception.WrongItemNameException;
import es.telocompro.service.item.ItemService;
import es.telocompro.service.user.UserService;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.LobCreator;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
* Created by @adrian on 14/06/14.
* All rights reserved.
*/

@RestController
@SuppressWarnings("unused")
@RequestMapping("/site/")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    /*
     * USER STUFF
     */
    
    @RequestMapping(value="users/all", method = RequestMethod.GET)
    public List<User> getUsers()
    {
        Iterable<User> all = userService.findAllUsers();
        List<User> list = new ArrayList<User>();
        for (User user: all) list.add(user);
        return list;
    }

    @RequestMapping(value="users/{userid}", method = RequestMethod.GET)
    public User getUser( @PathVariable("userid") Long userId ) {
        return userService.findUserById(userId);
    }

//    @RequestMapping(value="/users/findbylogin/{login}", method = RequestMethod.GET)
//    public User getUserByLogin( @PathVariable("login") String login ) {
//        return userService.;
//    }

    @RequestMapping(value="user", method=RequestMethod.POST)
    public User addUser( @RequestParam("login") String login, @RequestParam("password") String password,
                         @RequestParam("firstname") String firstname, @RequestParam("lastname") String lastname,
                         @RequestParam("address") String address, @RequestParam("phone") String phone,
                         @RequestParam("email") String email) {
        return userService.addUser(login, password, firstname, lastname, address, phone, email);
    }
    
    /*
     * ITEMS REFERREAL
     */
    /**
     * Add an item
     * @throws WrongItemNameException 
     */
    @RequestMapping(value="{username}/item", method=RequestMethod.POST)
    public Item addItem( @PathVariable("username") String userName, @RequestParam("subcategory") String subCategoryName,
                         @RequestParam("title") String title, @RequestParam("description") String description,
                         @RequestParam("prize") double prize,
                         @RequestParam("image") MultipartFile file) throws WrongItemNameException {
//    	System.out.println("##########################################");
//    	System.out.println("##########################################");
//    	System.out.println("##########################################");
//    	System.out.println("File received name is " + file.getName());
//    	System.out.println("File received raw orig name: " + file.getOriginalFilename());
//    	System.out.println("File received raw empty?: " + file.isEmpty());
//    	System.out.println("File size: " + file.getSize());
    	
//    	BufferedImage image;
//		try {
//			image = ImageIO.read(file.getInputStream());
//			Integer width = image.getWidth();
//	    	Integer height = image.getHeight();
//	    	System.out.println("Image height " + height);
//	    	System.out.println("image width " + width);
//		} catch (IOException e1) {
//			System.out.println("Error getting images details");
//		}
    	
    	byte[] imgBytes = null;
		try { imgBytes = file.getBytes(); } 
		catch (IOException e) { System.out.println("Error converting to bytes image file"); }
		
//		System.out.println("##########################################");
//		System.out.println("Te image to the database will be");
//		BufferedImage img = null;
//		try {
//			img = ImageIO.read(new ByteArrayInputStream(imgBytes));
//		} catch (IOException e) { e.printStackTrace(); }
//		
//		System.out.println("And.... we have an image which is");
//		Integer width = img.getWidth();
//    	Integer height = img.getHeight();
//    	System.out.println("Image height " + height);
//    	System.out.println("image width " + width);
//    	
//    	System.out.println("##########################################");
//    	System.out.println("Finally, images as string is");
//    	StringBuilder stringBuilder = new StringBuilder(imgBytes.length);
//    	for(byte byteChar : imgBytes) 
//    	{
//    	   stringBuilder.append((char) byteChar);
//    	}
		
        return itemService.addItem(userName, subCategoryName, title, description, prize, imgBytes);
    }

    /**
     * Updates an item
     */
    @RequestMapping(value="items/{userid}/{itemid}", method=RequestMethod.PUT)
    public Item updateItem(@PathVariable("itemid") Long userId, @PathVariable("itemid") Long itemId,
                           @RequestParam("title") String title, @RequestParam("description") String description,
                           @RequestParam("prize") double prize) {
        return itemService.updateItem(itemId, title, description, prize);
    }

    /**
     * Deletes an item
     */
    @RequestMapping(value="items/{itemid}", method=RequestMethod.DELETE)
    public void deleteItem( @RequestParam("itemid") Long itemId) {
        itemService.deleteItem(itemId);
    }

}
