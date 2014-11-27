package es.elovendo.service.item;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import es.elovendo.model.item.Item;
import es.elovendo.model.item.ItemForm;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.InsufficientPointsException;
import es.elovendo.rest.exception.ItemNotFoundException;
import es.elovendo.rest.exception.NotUserItemException;
import es.elovendo.rest.exception.SubCategoryNotFoundException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.service.exception.InvalidItemNameMinLenghtException;
import es.elovendo.util.Constant;

/**
 * Created by @adrian on 17/06/14. All rights reserved.
 */

@SuppressWarnings({ "unused", "javadoc" })
public interface ItemService {

	/**
	 * Adds an item giving parameters
	 * @param userName
	 * @param subCategoryId
	 * @param title
	 * @param description
	 * @param prize
	 * @param mainImage
	 * @param image1
	 * @param image2
	 * @param image3
	 * @param youtubeVideo
	 * @param featured
	 * @param highlight
	 * @param autoRenew
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws InvalidItemNameMinLenghtException
	 * @throws UserNotFoundException
	 * @throws SubCategoryNotFoundException
	 * @throws ProvinceNotFoundException
	 */
	public Item addItem(String userName, long subCategoryId, String title, String description, String currency,
			double prize, byte[] mainImage, byte[] image1, byte[] image2, byte[] image3, String youtubeVideo,
			boolean featured, boolean highlight, boolean autoRenew, String latitude, String longitude)
			throws InvalidItemNameMinLenghtException, UserNotFoundException, SubCategoryNotFoundException;


	/**
	 * Adds an item using {@link ItemForm} and giving parameters
	 * @param itemForm
	 * @param user
	 * @param subCategoryId SubCategoryId for item's SubCategory
	 * @param mainImage Main image for item's page
	 * @param image1
	 * @param image2
	 * @param image3
	 * @return Item created
	 * @throws SubCategoryNotFoundException
	 * @throws InsufficientPointsException
	 */
	public Item addItem(ItemForm itemForm, User user, MultipartFile mainImage, MultipartFile image1,
			MultipartFile image2, MultipartFile image3) throws SubCategoryNotFoundException, InsufficientPointsException;

	/**
	 * Get all items
	 * 
	 * @return Items
	 */
	public Page<Item> getAllItems(int page, int size);

	/**
	 * Get all user items
	 */
	public Page<Item> getAllItemsByUserName(String userName, int page, int size);

	/**
	 * Get all user items
	 */
	public List<Item> getAllItemsByUserName(String userName);

	/**
	 * Returns all items, INCLUDED ended ones
	 * 
	 * @param user
	 * @return
	 */
	public List<Item> getAllItemsByUser(User user);

	public List<Item> getAllItemsByUserId(Long userId);

	/**
	 * Get an item given its id
	 * 
	 * @param id
	 * @return Item
	 * @throws ItemNotFoundException
	 */
	public Item getItemById(Long id) throws ItemNotFoundException;
	
	/**
	 * Returns {@link Constant}.MAX_LAST_ITEMS items from the given user
	 * @param user
	 * @return
	 */
	public List<Item> getLastItems(User user);

	/**
	 * Finds a list of items that match the title
	 * 
	 * @param title
	 * @return Item page
	 */
	// public Page<Item> getItemByTitle(String title, int page, int size);

	// /**
	// * Finds a list of items that match the title
	// * @param title
	// * @param subCategory
	// * @return Item page
	// */
	// public Page<Item> getItemByTitleAndSubCategory(String title, String
	// subCategory, int page, int size);

	/**
	 * Finds all items from a category
	 * 
	 * @param categoryName
	 *            Category name
	 * @param prizeMin
	 *            Minimum prize
	 * @param prizeMax
	 *            Maximum prize
	 * @param page
	 *            Page number
	 * @param size
	 *            Page size
	 * @return Item page
	 */
	public Page<Item> getAllItemsByCategory(String categoryName, int prizeMin, int prizeMax, int page, int size);

	/**
	 * Returns items with the given parameters
	 * @param title
	 * @param name Category or SubCategory name
	 * @param dis
	 * @param lat
	 * @param lng
	 * @param prizeMin
	 * @param prizeMax
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Item> getItemsByParams(String title, String name, double dis, double lat, double lng,
			int prizeMin, int prizeMax, int page, int size);
	
	/**
	 * Returns items with the given parameters
	 * @param title
	 * @param id Category or SubCategory id
	 * @param type Using {@link Constant} CATEGORY or SUBCATEGORY to indicate the id character
	 * @param dis
	 * @param lat
	 * @param lng
	 * @param prizeMin
	 * @param prizeMax
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Item> getItemsByParams(String title, long id, String type, double dis, double lat,
			double lng, int prizeMin, int prizeMax, int page, int size);
	
	/**
	 * Get items using parameters with localized prize's currency
	 * @param locale
	 * @param title
	 * @param id
	 * @param type
	 * @param dis
	 * @param lat
	 * @param lng
	 * @param prizeMin
	 * @param prizeMax
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Item> getLocaledItemsByParams(Locale locale, String title, long id, String type, double dis,
			double lat, double lng, int prizeMin, int prizeMax, int page, int size);

	/**
	 * Returns random 'maxItems' items
	 * @param maxItems
	 * @param filter SubCategory or category name. This parameter can be null to get all items
	 * @return
	 */
	public List<Item> getRandomFeaturedItems(int maxItems, @Nullable String filter);
	
	/**
	 * Returns random 'maxItems' items
	 * @param maxItems
	 * @param categoryId SubCategory or category name. This parameter can be null to get all items
	 * @return Random list
	 */
	public List<Item> getRandomFeaturedItems(int maxItems, long categoryId);

	/**
	 * Updates an item
	 * 
	 * @param title
	 * @param description
	 * @param prize
	 * @return Item updated
	 */
	public Item updateItem(Long itemId, String title, String description, double prize, boolean renew,
			boolean featured, boolean highlight);

	public Item updateItem(ItemForm itemForm, User user, MultipartFile mainImage,
			MultipartFile image1, MultipartFile image2, MultipartFile image3) throws ItemNotFoundException,
			NotUserItemException, SubCategoryNotFoundException, InsufficientPointsException;

	/**
	 * Deletes an item, including all favorite references to it
	 * 
	 * @param itemId
	 */
	public void deleteItem(Long itemId);
	
	/**
	 * Deletes an item, including all favorite references to it
	 * @param user Owner
	 * @param itemId Item to delete
	 * @throws NotUserItemException If item doesnt belong to user
	 */
	public void deleteItem(User user, Long itemId) throws NotUserItemException;
	
	/**
	 * Deletes all user items
	 * @param user
	 */
	public void deleteAllUserItems(User user);

	/**
	 * Returns number of active user items
	 * @param user
	 * @return Number of active user items
	 */
	public int getNumberUserItems(User user);

	/**
	 * Returns number of active user items
	 * @param userId
	 * @return Number of active user items
	 */
	public int getNumberUserItems(Long userId);

	public Iterable<Item> getAll(Iterable<Long> ids);
}
