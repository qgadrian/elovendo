package es.telocompro.service.item;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.ItemRepository;
import es.telocompro.model.item.category.CategoryRepository;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.item.category.subcategory.SubCategoryRepository;
import es.telocompro.model.province.Province;
import es.telocompro.model.user.User;
import es.telocompro.model.user.UserRepository;
import es.telocompro.rest.controller.exception.ProvinceNotFoundException;
import es.telocompro.rest.controller.exception.SubCategoryNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;
import es.telocompro.service.exception.InvalidItemNameMinLenghtException;
import es.telocompro.service.province.ProvinceService;
import es.telocompro.service.user.UserService;

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

import static es.telocompro.util.Constant.*;

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
    UserService userService;
    @Autowired
    CategoryRepository categoryRepository; // TODO usar un servicio
    @Autowired
    ProvinceService provinceService;

    @Override
    public Item addItem(String userName, String subCategoryName, String title, String description, 
    		String provinceName, double prize, byte[] imgHome) 
    				throws InvalidItemNameMinLenghtException, UserNotFoundException, SubCategoryNotFoundException, 
    				ProvinceNotFoundException {
    	
    	User user = userService.findUserByLogin(userName);
        SubCategory subCategory = categoryRepository.findSubCategoryByName(subCategoryName);
        Province province = provinceService.findProvinceByName(provinceName);
    	
    	if (title.length() < MIN_ITEM_TITLE_LENGHT) throw new InvalidItemNameMinLenghtException(title);
    	if (user == null) throw new UserNotFoundException(userName);
    	if (subCategory == null) throw new SubCategoryNotFoundException(subCategoryName);
    	if (province == null) throw new ProvinceNotFoundException(provinceName);
    	
        String base64 = Base64.encodeBase64String(imgHome); // Testing because json
        Item item = new Item(user, subCategory, title, description, province,
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
