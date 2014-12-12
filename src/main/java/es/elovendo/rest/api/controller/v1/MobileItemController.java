package es.elovendo.rest.api.controller.v1;

import static es.elovendo.util.Constant.S_ITEMS_PER_PAGE;
import es.elovendo.model.item.Item;
import es.elovendo.model.item.category.Category;
import es.elovendo.model.item.category.CategoryRepository;
import es.elovendo.rest.exception.ItemNotFoundException;
import es.elovendo.rest.exception.WrongItemSubCategoryRequestException;
import es.elovendo.rest.util.RestItemObject;
import es.elovendo.service.item.ItemService;
import es.elovendo.util.Constant;
import es.elovendo.util.IOUtil;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import scala.annotation.meta.getter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by @author Adrián Quintás on 17/06/14. All rights reserved.
 */

@Controller
@SuppressWarnings("unused")
@RequestMapping(Constant.MOBILE_API_URL_PREFIX_V1 + "/bazaar/c/")
public class MobileItemController {

	@Autowired
	private ItemService itemService;

	/**
	 * Get an item by its id. Asks for subCategory to avoid "cheating" by
	 * increasing id search.
	 * 
	 * @throws WrongItemSubCategoryRequestException
	 * @throws ItemNotFoundException
	 */
	// @Secured("ROLE_USER")
	@RequestMapping(value = "{subcategory}/{itemid}", method = RequestMethod.GET)
	public RestItemObject getItem(@PathVariable("subcategory") String subcategory, @PathVariable("itemid") Long itemId)
			throws WrongItemSubCategoryRequestException, ItemNotFoundException {
		Item item = itemService.getItemById(itemId);

		if (!item.getSubCategory().getSubCategoryName().equalsIgnoreCase(subcategory)) {
			throw new WrongItemSubCategoryRequestException(subcategory, itemId);
		}

		RestItemObject restItemObject = new RestItemObject(item, item.getUser().getLogin(), item.getSubCategory()
				.getCategory().getCategoryName(), item.getSubCategory().getSubCategoryName());
		return restItemObject;
	}

	/**
	 * Search for items by title
	 */
	@RequestMapping(value = "search", method = RequestMethod.GET)
	public Page<Item> getItemsFindByTitle(@RequestParam("title") String title,
			@RequestParam(value = "subcategory", required = false, defaultValue = "") String subCategory,
			@RequestParam("p") int page, @RequestParam("s") int size) {
		// return itemService.getItemByTitleAndSubCategory(title, subCategory,
		// page, size);
		return null;
	}

	/**
	 * Get items from a given user
	 * 
	 * @param userId
	 * @return Item page
	 */
	@RequestMapping(value = "items/user/{userName}", method = RequestMethod.GET)
	public Page<Item> getItemsByUserName(@PathVariable("userName") String userName, @RequestParam("p") int page,
			@RequestParam("s") int size) {
		return itemService.getAllItemsByUserName(userName, page, size);
	}

	/**
	 * Get an items page for a desired subCategory
	 * 
	 * @param filter
	 *            Fields to retrieve from item
	 * @param subCategoryName
	 * @param page
	 *            Page number
	 * @param size
	 *            Elements size of page
	 * @return JSON with item information requested by filter
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/2/{subcategoryname}", params = { "p", "s" }, method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody JSONObject getItemsBySubCategory(@PathVariable("subcategoryname") String subCategoryName,
			@RequestParam(value = "filter", required = false) String filter,
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "dis", required = false, defaultValue = "0") double dis,
			@RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
			@RequestParam(value = "lng", required = false, defaultValue = "0") double lng,
			@RequestParam(value = "min", required = false, defaultValue = "0") int prizeMin,
			@RequestParam(value = "max", required = false, defaultValue = "0") int prizeMax,
			@RequestParam(value = "p", required = false, defaultValue = "0") int page,
			@RequestParam(value = "s", required = false, defaultValue = S_ITEMS_PER_PAGE) int size,
			HttpServletResponse response) {

		String[] filterParams = filter.split(",");

		// TODO: Maybe could be better a query with the params in filter?
		// Page<Item> p = itemService.getAllItemsBySubCategory(subCategoryName,
		// prizeMin, prizeMax, page, size);

		Page<Item> p = itemService.getItemsByParams(title, subCategoryName, dis, lat, lng, prizeMin, prizeMax, page,
				size);
		List<Item> list = p.getContent();

		// Obtain the desired page and format a JSON with data
		JSONObject jsonResponse = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray itemArray = new JSONArray();
		for (Item item : list) {
			JSONObject itemJsonObject = new JSONObject();
			if (filterParams != null)
				itemJsonObject = getJSONObjectFromFilter(filterParams, item);
			else
				itemJsonObject = getJSONObjectFromItem(item);
			itemArray.add(itemJsonObject);
		}
		JSONObject pageJSONObject = new JSONObject();
		pageJSONObject.put("pageNumber", p.getNumber());
		pageJSONObject.put("pageElements", p.getNumberOfElements());
		pageJSONObject.put("totalPages", p.getTotalPages());
		pageJSONObject.put("totalElements", p.getTotalElements());
		jsonArray.add(pageJSONObject);

		jsonResponse.put("content", itemArray);
		jsonResponse.put("page", jsonArray);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);

		return jsonResponse;
	}

	@SuppressWarnings("unchecked")
	private JSONObject getJSONObjectFromFilter(String[] filterParams, Item item) {

		JSONObject itemJsonObject = new JSONObject();

		for (String field : filterParams) {
			switch (field) {
			case "itemId":
				itemJsonObject.put("itemid", item.getItemId());
				break;
			case "title":
				itemJsonObject.put("title", item.getTitle());
				break;
			case "description":
				itemJsonObject.put("description", item.getDescription());
				break;
			case "prize":
				itemJsonObject.put("prize", item.getPrize());
				break;
			case "distance":
				itemJsonObject.put("distance", item.getDistance());
				break;
			case "lat":
				itemJsonObject.put("lat", item.getLatitude());
				break;
			case "lng":
				itemJsonObject.put("lng", item.getLongitude());
				break;
			case "userName":
				itemJsonObject.put("userName", item.getUser().getLogin());
				break;
			case "profilePic":
				if (item.getUser().isSocialUser())
					itemJsonObject.put("profilePic", item.getUser().getLargeSocialAvatar());
				else
					itemJsonObject.put("profilePic", "http://www.elovendo.com/" + item.getUser().getAvatar());
				break;
			case "userValue":
				itemJsonObject.put("userValue", item.getUser().getUserValue());
				break;
			case "subCategory":
				itemJsonObject.put("subcategory", item.getSubCategory().getSubCategoryName());
				break;
			case "category":
				itemJsonObject.put("category", item.getSubCategory().getCategory().getCategoryName());
				break;
			case "imageHome":
				itemJsonObject.put("imageHome", "http://www.elovendo.com/" + item.getMainImage200h());
				break;
			}
		}

		return itemJsonObject;

	}

	@SuppressWarnings("unchecked")
	private JSONObject getJSONObjectFromItem(Item item) {

		JSONObject itemJsonObject = new JSONObject();

		itemJsonObject.put("itemid", item.getItemId());
		itemJsonObject.put("title", item.getTitle());
		itemJsonObject.put("description", item.getDescription());
		itemJsonObject.put("distance", item.getDistance());
		itemJsonObject.put("lat", item.getLatitude());
		itemJsonObject.put("lng", item.getLongitude());
		itemJsonObject.put("prize", item.getPrize());
		itemJsonObject.put("userName", item.getUser().getLogin());
		itemJsonObject.put("profilePic", "http://www.elovendo.com/" + item.getUser().getAvatar200h());
		itemJsonObject.put("userValue", item.getUser().getUserValue());
		itemJsonObject.put("subcategory", item.getSubCategory().getSubCategoryName());
		itemJsonObject.put("category", item.getSubCategory().getCategory().getCategoryName());
		itemJsonObject.put("imageHome", "http://www.elovendo.com/" + item.getMainImage200h());

		return itemJsonObject;
	}

}
