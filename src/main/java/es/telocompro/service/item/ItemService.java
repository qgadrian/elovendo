package es.telocompro.service.item;

import es.telocompro.model.item.Item;
import es.telocompro.model.province.Province;
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
     * @param userId
     * @param title
     * @param description
     * @param prize
     * @return
     * @throws UserNotFoundException 
     * @throws SubCategoryNotFoundException 
     * @throws ProvinceNotFoundException
     * @throws IOException 
     */
    public Item addItem(String userName, String subCategoryName, String title, String description, 
    		String provinceName, double prize, byte[] image1)
    				throws InvalidItemNameMinLenghtException, UserNotFoundException, SubCategoryNotFoundException, 
    				ProvinceNotFoundException, IOException;

    /**
     * Get all items
     * @return
     */
    public Iterable<Item> getAllItems();

    /**
     * Get all user items
     */
    public Page<Item> getAllItemsByUserName(String userName, int page, int size);

    /**
     * Get an item given its id
     * @param id
     * @return
     */
    public Item getItemById(Long id);

    /**
     * Finds a list of items that match the title
     * @param title
     * @return
     */
    public Page<Item> getItemByTitle(String title, int page, int size);

    /**
     * Finds all items from a subCategory
     * @param subCategoryName
     * @return
     */
    @Deprecated
    public Page<Item> getAllItemsBySubCategory(String subCategoryName, int page, int size);
    
    /**
     * Finds all items from a subCategory
     * @param subCategoryName
     * @param prizeMin Minimum prize
     * @param prizeMax Maximum prize
     * @param page Page number
     * @param size Page size
     * @return
     */
    public Page<Item> getAllItemsBySubCategory(String subCategoryName, int prizeMin, int prizeMax, 
    		int page, int size);

    /**
     * Updates an item
     * @param title
     * @param description
     * @param prize
     * @return
     */
    public Item updateItem(Long itemId, String title, String description, double prize);

    /**
     * Deletes an item
     * @param itemId
     */
    public void deleteItem(Long itemId);
}
