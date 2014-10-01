package es.telocompro.service.item;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.ItemForm;
import es.telocompro.model.user.User;
import es.telocompro.rest.exception.InsufficientPointsException;
import es.telocompro.rest.exception.ItemNotFoundException;
import es.telocompro.rest.exception.NotUserItemException;
import es.telocompro.rest.exception.ProvinceNotFoundException;
import es.telocompro.rest.exception.SubCategoryNotFoundException;
import es.telocompro.rest.exception.UserNotFoundException;
import es.telocompro.service.exception.InvalidItemNameMinLenghtException;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by @adrian on 17/06/14. All rights reserved.
 */

@SuppressWarnings({ "unused", "javadoc" })
public interface ItemService {

	/**
	 * Adds an item
	 * 
	 * @param userName
	 * @param subCategoryName
	 * @param title
	 * @param description
	 * @param provinceName
	 * @param prize
	 * @param mainImage
	 * @param featured
	 * @param highlight
	 * @param location
	 *            Coordinates (lat, long)
	 * @return
	 * @throws InvalidItemNameMinLenghtException
	 * @throws UserNotFoundException
	 * @throws SubCategoryNotFoundException
	 * @throws ProvinceNotFoundException
	 */
	public Item addItem(String userName, long subCategoryId, String title, String description, double prize,
			byte[] mainImage, byte[] image1, byte[] image2, byte[] image3, String youtubeVideo, boolean featured,
			boolean highlight, boolean autoRenew, String latitude, String longitude) throws InvalidItemNameMinLenghtException,
			UserNotFoundException, SubCategoryNotFoundException, ProvinceNotFoundException;
//
//	/**
//	 * Adds an item with external values
//	 * 
//	 * @param item
//	 * @param subCategoryName
//	 * @param provinceName
//	 * @param imgBytes
//	 * @param featured
//	 * @param hightlight
//	 * @return Item added
//	 * @throws InvalidItemNameMinLenghtException
//	 * @throws UserNotFoundException
//	 * @throws SubCategoryNotFoundException
//	 * @throws ProvinceNotFoundException
//	 */
//	public Item addItem(Item item, long subCategoryName, byte[] mainImage, byte[] image1, byte[] image2, byte[] image3,
//			boolean featured, boolean highlight) throws InvalidItemNameMinLenghtException, UserNotFoundException,
//			SubCategoryNotFoundException, ProvinceNotFoundException;

	public Item addItem(ItemForm itemForm, User user,long subCategoryId, MultipartFile mainImage, MultipartFile image1,
			MultipartFile image2, MultipartFile image3) throws SubCategoryNotFoundException, InsufficientPointsException;

	/**
	 * Get all items
	 * 
	 * @return Items
	 */
	public Iterable<Item> getAllItems();

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

	public Page<Item> getItemsByParams(String title, String subCategory, double dis, double lat, double lng,
			int prizeMin, int prizeMax, int page, int size);

	/**
	 * Returns random 'maxItems' items
	 * 
	 * @param maxItems
	 *            Max items to retrieve
	 * @param subCategory
	 *            @Nullable SubCategory to find items.
	 * @return Randomized items
	 */
	public List<Item> getRandomFeaturedItems(int maxItems, String filter);

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

	public Item updateItem(ItemForm itemForm, User user, long subCategoryId, MultipartFile mainImage,
			MultipartFile image1, MultipartFile image2, MultipartFile image3) throws ItemNotFoundException,
			NotUserItemException, SubCategoryNotFoundException;

	/**
	 * Deletes an item
	 * 
	 * @param itemId
	 */
	public void deleteItem(Long itemId);

	public int getNumberUserItems(User user);

	public int getNumberUserItems(Long userId);

	public Iterable<Item> getAll(Iterable<Long> ids);
}
