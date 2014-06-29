package es.telocompro.service.item;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.ItemRepository;
import es.telocompro.model.item.category.CategoryRepository;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.item.category.subcategory.SubCategoryRepository;
import es.telocompro.model.user.User;
import es.telocompro.model.user.UserRepository;
import es.telocompro.rest.util.RestItemObject;
import es.telocompro.service.exception.WrongItemNameException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Calendar;
import java.util.List;

import static es.telocompro.util.Constants.*;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */

@SuppressWarnings(value = "unused")
@Service("itemService")
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;

    // TODO: Exceptions if user or so is not found
    @Override
    public Item addItem(String userName, String subCategoryName, String title, 
    		String description, double prize, byte[] imgHome) throws WrongItemNameException {
    	
    	if (title.length() < MIN_ITEM_TITLE_LENGHT)
    		throw new WrongItemNameException(title);
    	
        User user = userRepository.findByLogin(userName);
        SubCategory subCategory = categoryRepository.findSubCategoryByName(subCategoryName);
        String base64 = Base64.encodeBase64String(imgHome); // Testing because json
        Item item = new Item(user, subCategory, title, description, 
        		new BigDecimal(prize), Calendar.getInstance(), base64);
        return itemRepository.save(item);
    }

    @Override
    public Iterable<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Page<Item> getAllItemsByUserName(String userName, int page, int size) {
        return itemRepository.findByUserName(userName, new PageRequest(page, size));
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.findOne(id);
    }

    @Override
    public Page<Item> getItemByTitle(String title, int page, int size) {
        return itemRepository.findByTitle(title, new PageRequest(page, size));
    }

    @Override
    public Page<Item> getAllItemsBySubCategory(String subCategoryName, int page, int size) {
        return itemRepository.findItemsBySubCategoryName(subCategoryName, 
        		new PageRequest(page, size));
    }

    @Override
    public Item updateItem(Long itemId, String title, String description, double prize) {
        Item item = itemRepository.findOne(itemId);
        item.setTitle(title);
        item.setDescription(description);
        item.setPrize(new BigDecimal(prize));
        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.delete(itemId);
    }
}
