package es.telocompro.service;

import es.telocompro.model.item.Item;

import java.math.BigDecimal;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */

@SuppressWarnings({"unused", "JavaDoc"})
public interface ItemService {

    /**
     * Adds an item
     * @param title
     * @param description
     * @param prize
     * @return
     */
    public Item addItem(String title, String description, double prize);

    /**
     * Get all items
     * @return
     */
    public Iterable<Item> getAllItems();

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
