package es.telocompro.service.item;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.ItemRepository;
import es.telocompro.model.item.category.CategoryRepository;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.item.category.subcategory.SubCategoryRepository;
import es.telocompro.model.user.User;
import es.telocompro.model.user.UserRepository;
import es.telocompro.rest.util.RestItemObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

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

    @Override
    public Item addItem(Long userId, String subCategoryName, String title, String description, double prize) {
        User user = userRepository.findOne(userId);
        SubCategory subCategory = categoryRepository.findSubCategoryByName(subCategoryName);
        Item item = new Item(user, subCategory, title, description, new BigDecimal(prize), Calendar.getInstance());
        return itemRepository.save(item);
    }

    @Override
    public Iterable<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getAllItemsByUserId(Long userId) {
        return itemRepository.findByUserId(userId);
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.findOne(id);
    }

    @Override
    public Iterable<Item> getItemByTitle(String title) {
        return itemRepository.findByTitle(title);
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
