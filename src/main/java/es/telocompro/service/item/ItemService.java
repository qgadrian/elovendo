package es.telocompro.service.item;

import es.telocompro.model.item.Item;
import es.telocompro.rest.controller.exception.ItemNotFoundException;
import es.telocompro.rest.controller.exception.ProvinceNotFoundException;
import es.telocompro.rest.controller.exception.SubCategoryNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;
import es.telocompro.service.exception.InvalidItemNameMinLenghtException;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.List;

import org.springframework.data.domain.Page;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */

@SuppressWarnings({"unused", "javadoc"})
public interface ItemService {

    /**
     * Adds an item
     * @param userName
     * @param subCategoryName
     * @param title
     * @param description
     * @param provinceName
     * @param prize
     * @param mainImage
     * @param featured
     * @param highlight
     * @param location Coordinates (lat, long)
     * @return
     * @throws InvalidItemNameMinLenghtException
     * @throws UserNotFoundException
     * @throws SubCategoryNotFoundException
     * @throws ProvinceNotFoundException
     */
    public Item addItem(String userName, long subCategoryId, String title, String description, 
    		double prize, byte[] mainImage, byte[] image1, byte[] image2,
    		byte[] image3, String youtubeVideo, boolean featured, boolean highlight, String latitude, String longitude)
    				throws InvalidItemNameMinLenghtException, UserNotFoundException, SubCategoryNotFoundException, 
    				ProvinceNotFoundException;
    
    /**
     * Adds an item with external values
     * @param item
     * @param subCategoryName
     * @param provinceName
     * @param imgBytes
     * @param featured
     * @param hightlight
     * @return Item added
     * @throws InvalidItemNameMinLenghtException
     * @throws UserNotFoundException
     * @throws SubCategoryNotFoundException
     * @throws ProvinceNotFoundException
     */
    public Item addItem(Item item, long subCategoryName, byte[] mainImage,
    		byte[] image1, byte[] image2, byte[] image3, boolean featured, boolean highlight) 
    		throws InvalidItemNameMinLenghtException, UserNotFoundException, 
    		SubCategoryNotFoundException, ProvinceNotFoundException;

    /**
     * Get all items
     * @return Items
     */
    public Iterable<Item> getAllItems();

    /**
     * Get all user items
     */
    public Page<Item> getAllItemsByUserName(String userName, int page, int size);

    /**
     * Get an item given its id
     * @param id
     * @return Item
     * @throws ItemNotFoundException 
     */
    public Item getItemById(Long id) throws ItemNotFoundException;

    /**
     * Finds a list of items that match the title
     * @param title
     * @return Item page
     */
//    public Page<Item> getItemByTitle(String title, int page, int size);
    
//    /**
//     * Finds a list of items that match the title
//     * @param title
//     * @param subCategory
//     * @return Item page
//     */
//    public Page<Item> getItemByTitleAndSubCategory(String title, String subCategory, int page, int size);
    
    /**
     * Finds all items from a category
     * @param categoryName Category name
     * @param prizeMin Minimum prize
     * @param prizeMax Maximum prize
     * @param page Page number
     * @param size Page size
     * @return Item page
     */
    public Page<Item> getAllItemsByCategory(String categoryName, int prizeMin, int prizeMax, 
    		int page, int size);

//    /**
//     * Finds all items from a subCategory
//     * @param subCategoryName
//     * @deprecated Use getAllItemsBySubCategory(String subCategoryName, int prizeMin, 
//     * int prizeMax, int page, int size) instead
//     * @return
//     */
//    @Deprecated
//    public Page<Item> getAllItemsBySubCategory(String subCategoryName, int page, int size);
//    
//    /**
//     * Finds all items from a subCategory
//     * @param subCategoryName SubCategory name
//     * @param prizeMin Minimum prize
//     * @param prizeMax Maximum prize
//     * @param page Page number
//     * @param size Page size
//     * @return Item page
//     */
//    @Deprecated
//    public Page<Item> getAllItemsBySubCategory(String subCategoryName, int prizeMin, int prizeMax, 
//    		int page, int size);
//    
//    /**
//     * Get all items using parameters
//     * @param title
//     * @param subCategory
//     * @param province
//     * @param dis Distance to item
//     * @param location {LATITUDE, LONGITUDE}
//     * @param prizeMin
//     * @param prizeMax
//     * @param page
//     * @param size
//     * @return
//     */
//    @Deprecated
//    public Page<Item> getItemByParams(String title, String subCategory, String province,
//    		int prizeMin, int prizeMax, int page, int size);
    
    /**
     * Get all items using parameters
     * @param title
     * @param subCategory
     * @param province
     * @param dis Distance to item
     * @param location {LATITUDE, LONGITUDE}
     * @param prizeMin
     * @param prizeMax
     * @param page
     * @param size
     * @return
     */
    public Page<Item> getItemByParams2(String title, String subCategory, double dis, float[] location,
    		int prizeMin, int prizeMax, int page, int size);
    
    /**
     * Returns random 'maxItems' items 
     * @param maxItems Max items to retrieve
     * @param subCategory SubCategory to find items
     * @return Randomized items
     */
    public List<Item> getRandomFeaturedItems(int maxItems, String filter);

    /**
     * Updates an item
     * @param title
     * @param description
     * @param prize
     * @return Item updated
     */
    public Item updateItem(Long itemId, String title, String description, double prize, boolean renew, 
    		boolean featured, boolean highlight);

    /**
     * Deletes an item
     * @param itemId
     */
    public void deleteItem(Long itemId);
}
