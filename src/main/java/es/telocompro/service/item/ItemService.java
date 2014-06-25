package es.telocompro.service.item;

import es.telocompro.model.item.Item;
import es.telocompro.rest.util.RestItemObject;

import java.math.BigDecimal;
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
     */
    public Item addItem(Long userId, String subCategoryName, String title, String description, double prize);

    /**
     * Get all items
     * @return
     */
    public Iterable<Item> getAllItems();

    /**
     * Get all user items
     */
    public List<Item> getAllItemsByUserId(Long userId);

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
    public Iterable<Item> getItemByTitle(String title);

    /**
     * Finds all items from a subcategory
     * @param subCategoryName
     * @return
     */
    public Page<Item> getAllItemsBySubCategory(String subCategoryName, int page, int size);

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
