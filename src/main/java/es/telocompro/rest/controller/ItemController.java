package es.telocompro.rest.controller;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.CategoryRepository;
import es.telocompro.rest.controller.exception.WrongItemSubCategoryRequestException;
import es.telocompro.rest.util.RestItemObject;
import es.telocompro.service.item.ItemService;
import es.telocompro.util.IOUtil;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import scala.annotation.meta.getter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */

@RestController
@SuppressWarnings("unused")
@RequestMapping(value = "/bazaar/c/")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * Get an items page for a desired subcategory
     * @param subCategoryName
     * @param page Page number
     * @param size Elements size of page
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value="{subcategoryname}", params = {"p","s"},
    		method = RequestMethod.GET)
//    public Page<Item> getItemsBySubCategory(@PathVariable("subcategoryname") String subCategoryName,
    public JSONObject getItemsBySubCategory(@PathVariable("subcategoryname") String subCategoryName,
    		@RequestParam("p") int page, @RequestParam( "s" ) int size,
    		HttpServletResponse response) {
//    	HttpServletResponse response appliction/json
    	
    	Page<Item> p = itemService.getAllItemsBySubCategory(subCategoryName, page, size);
    	List<Item> list = p.getContent();
    	
    	JSONObject jsonResponse = new JSONObject();
    	JSONArray jsonArray = new JSONArray();
    	JSONArray itemArray = new JSONArray();
    	for (Item i : list) {
        	JSONObject itemJsonObject = new JSONObject();
    		itemJsonObject.put("itemid", i.getItemId());
    		itemJsonObject.put("title", i.getTitle());
    		itemJsonObject.put("description", i.getDescription());
    		itemJsonObject.put("prize", i.getPrize());
    		itemJsonObject.put("username", i.getUser().getLogin());
    		itemJsonObject.put("subcategory", i.getSubCategory().getSubCategoryName());
    		itemJsonObject.put("category", i.getSubCategory().getCategory().getCategoryName());
//    		itemJsonObject.put("imghome", i.getImgHome());
				itemJsonObject.put("filename", 
						IOUtil.calculateFileName(i.getUser().getLogin(), i.getItemId().intValue()));
    		itemArray.add(itemJsonObject);
    	}
    	JSONObject pageNumber = new JSONObject();
    	JSONObject pageSize = new JSONObject();
    	JSONObject pageTotalPages = new JSONObject();
    	JSONObject pageSizeTotalElements = new JSONObject();
    	pageNumber.put("pageNumber", p.getNumber());
    	pageSize.put("pageElements", p.getNumberOfElements());
    	pageTotalPages.put("totalPages", p.getTotalPages());
    	pageSizeTotalElements.put("totalElements", p.getTotalElements());
    	jsonArray.add(pageNumber);
    	jsonArray.add(pageSize);
    	jsonArray.add(pageTotalPages);
    	jsonArray.add(pageSizeTotalElements);
    	
    	
    	jsonResponse.put("content", itemArray);
    	jsonResponse.put("page", jsonArray);
    	
    	response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
    	
//    	System.out.println("going to respond " + jsonResponse.toJSONString());
    	
//    	return itemService.getAllItemsBySubCategory(subCategoryName, page, size);
    	return jsonResponse;
    	
//    	Page<Item> pages = itemService.getAllItemsBySubCategory(subCategoryName, page, size);
//    	
//    	System.out.println("##########################################");
//		System.out.println("Te image received from database is...");
//		BufferedImage img = null;
//		try {
//			ByteArrayInputStream b = new ByteArrayInputStream(pages.getContent().get(2).getImgHome());
//			img = ImageIO.read(b);
//		} catch (IOException e) { e.printStackTrace(); }
//		Integer width = img.getWidth();
//    	Integer height = img.getHeight();
//    	System.out.println("Image height " + height);
//    	System.out.println("image width " + width);
//    	
//    	FileOutputStream fos = null;
//    	try {
//    		fos = new FileOutputStream("/home/adrian/Desktop/pruebaImage.jpg");
//    	    fos.write(pages.getContent().get(2).getImgHome());
//    	    fos.close();
//    	} catch (IOException e) {}
//    	
//    	return pages;
    	
    }
    
    @RequestMapping(value="{subcategory}/{itemid}/image", method = RequestMethod.GET)
    public String getItemImage( @PathVariable("subcategory") String subcategory,
    		@PathVariable("itemid") Long itemId ) throws WrongItemSubCategoryRequestException {
    	
    	Item item = itemService.getItemById(itemId);
    	
    	if (!item.getSubCategory().getSubCategoryName()
    			.equalsIgnoreCase(subcategory)) {
    		throw new WrongItemSubCategoryRequestException(subcategory, itemId);
    	}
    	// TODO check if item is receveided or launch exception or think about it
    	
//    	JSONObject jsonResponse = new JSONObject();
//    	jsonResponse.put("imghome", item.getImgHome());
    	    	
    	return item.getImgHome();
    	
    }
    
    /**
     * Get an item by its id. Asks for subcategory too to avoid increasing id search.
     * @throws WrongItemSubCategoryRequestException 
     */
//    @Secured("ROLE_USER")
//    @RequestMapping(value="/items/{itemid}", method = RequestMethod.GET)
//    public Item getItem( @PathVariable("itemid") Long itemId ) {
//        return itemService.getItemById(itemId);
//    }
    @RequestMapping(value="{subcategory}/{itemid}", method = RequestMethod.GET)
    public RestItemObject getItem( @PathVariable("subcategory") String subcategory,
    		@PathVariable("itemid") Long itemId ) throws WrongItemSubCategoryRequestException {
    	Item item = itemService.getItemById(itemId);
    	
    	if (!item.getSubCategory().getSubCategoryName()
    			.equalsIgnoreCase(subcategory)) {
    		throw new WrongItemSubCategoryRequestException(subcategory, itemId);
    	}
    	
    	RestItemObject restItemObject = new RestItemObject(item,
    			item.getUser().getLogin(), 
    			item.getSubCategory().getCategory().getCategoryName(), 
    			item.getSubCategory().getSubCategoryName());
        return restItemObject;
    }
    
    /**
     * Search for items by title
     */
    @RequestMapping(value="search{title}", method = RequestMethod.GET)
    public Page<Item> getItemsFindByTitle( @RequestParam("title") String title,
    		@RequestParam("p") int page, @RequestParam( "s" ) int size) {
        return itemService.getItemByTitle(title, page, size);
    }


    /**
     * Get items from a given user
     * @param userId
     * @return
     */
    @RequestMapping(value="items/user/{userName}", method = RequestMethod.GET)
    public Page<Item> getItemsByUserName(@PathVariable("userid") String userName,
    		@RequestParam("p") int page, @RequestParam( "s" ) int size) {
        return itemService.getAllItemsByUserName(userName, page, size);
    }

}