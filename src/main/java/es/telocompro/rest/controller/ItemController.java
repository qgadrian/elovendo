package es.telocompro.rest.controller;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.CategoryRepository;
import es.telocompro.service.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */

@RestController
@SuppressWarnings("unused")
public class ItemController {

    @Autowired
    private ItemService itemService;

//    @RequestMapping(value="/items", method = RequestMethod.GET)
//    public List<Item> getAllItems() {
//        Iterable<Item> iterable = itemService.getAllItems();
//        return Lists.newArrayList(iterable);
//    }

    /**
     * Get an item by id
     */
    @RequestMapping(value="/bazar/items/{itemid}", method = RequestMethod.GET)
    public Item getItem( @PathVariable("itemid") Long itemId ) {
        return itemService.getItemById(itemId);
    }

    /**
     * Get items from a given user
     * @param userId
     * @return
     */
    @RequestMapping(value="/bazar/items/user/{userid}", method = RequestMethod.GET)
    public List<Item> getItemsByUserId(@PathVariable("userid") Long userId) {
        return itemService.getAllItemsByUserId(userId);
    }

    @RequestMapping(value="/bazar/c/{subcategoryname}", method = RequestMethod.GET)
    public Iterable<Item> getItemsBySubCategory(@PathVariable("subcategoryname") String subCategoryName) {
        return itemService.getAllItemsBySubCategory(subCategoryName);
    }

    /**
     * Search for items by title
     */
    @RequestMapping(value="/items/search{title}", method = RequestMethod.GET)
    public Iterable<Item> getItemsFindByTitle( @RequestParam("title") String title ) {
        return itemService.getItemByTitle(title);
    }

    /**
     * Add an item
     */
    @RequestMapping(value="/items/item", method=RequestMethod.POST)
    public Item addItem( @RequestParam("userid") Long userId, @RequestParam("subcategory") String subCategoryName,
                         @RequestParam("title") String title, @RequestParam("description") String description,
                         @RequestParam("prize") double prize) {
        return itemService.addItem(userId, subCategoryName, title, description, prize);
    }

    /**
     * Updates an item
     */
    @RequestMapping(value="/items/{userid}/{itemid}", method=RequestMethod.PUT)
    public Item updateItem(@PathVariable("itemid") Long userId, @PathVariable("itemid") Long itemId,
                           @RequestParam("title") String title, @RequestParam("description") String description,
                           @RequestParam("prize") double prize) {
        return itemService.updateItem(itemId, title, description, prize);
    }

    /**
     * Deletes an item
     */
    @RequestMapping(value="/items/{itemid}", method=RequestMethod.DELETE)
    public void deleteItem( @RequestParam("itemid") Long itemId) {
        itemService.deleteItem(itemId);
    }

}