package es.telocompro.rest;

import es.telocompro.model.item.Item;
import es.telocompro.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */

@RestController
@SuppressWarnings("unused")
public class ItemController {

    @Autowired
    ItemService itemService;

//    @RequestMapping(value="/items", method = RequestMethod.GET)
//    public List<Item> getAllItems() {
//        Iterable<Item> iterable = itemService.getAllItems();
//        return Lists.newArrayList(iterable);
//    }

    /**
     * Get an item by id
     */
    @RequestMapping(value="/items/{itemid}", method = RequestMethod.GET)
    public Item getItem( @PathVariable("itemid") Long itemId ) {
        return itemService.getItemById(itemId);
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
    public Item addItem( @RequestParam("title") String title, @RequestParam("description") String description,
                         @RequestParam("prize") double prize) {
        return itemService.addItem(title, description, prize);
    }

    /**
     * Updates an item
     */
    @RequestMapping(value="/items/{itemid}", method=RequestMethod.PUT)
    public Item updateItem(@PathVariable("itemid") Long itemId, @RequestParam("title") String title, @RequestParam("description") String description,
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