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
import es.telocompro.service.item.category.CategoryService;
import es.telocompro.service.province.ProvinceService;
import es.telocompro.service.user.UserService;
import es.telocompro.util.IOUtil;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;

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
    CategoryService categoryService;
    @Autowired
    ProvinceService provinceService;

    @Override
    public Item addItem(String userName, String subCategoryName, String title, String description, 
    		String provinceName, double prize, byte[] image1) 
    				throws InvalidItemNameMinLenghtException, UserNotFoundException, SubCategoryNotFoundException, 
    				ProvinceNotFoundException, IOException {
    	
    	User user = userService.findUserByLogin(userName);
    	SubCategory subCategory = categoryService.getSubCategoryByName(subCategoryName);
        Province province = provinceService.findProvinceByName(provinceName);
    	
    	if (title.length() < MIN_ITEM_TITLE_LENGHT) throw new InvalidItemNameMinLenghtException(title);
    	if (user == null) throw new UserNotFoundException(userName);
    	if (subCategory == null) throw new SubCategoryNotFoundException(subCategoryName);
    	if (province == null) throw new ProvinceNotFoundException(provinceName);
    	
    	// create item
        Item item = new Item(user, subCategory, title, description, province,
        		new BigDecimal(prize), Calendar.getInstance(), null);
        
        // Produce an unique name for an item
        String imageFileName = itemHash(item);
        
        if (image1 != null) //FIXME: Temporal line for test
        try {
	        /** SAVE IMAGE IN A RESOURCE FOLDER **/
			// Create folder for /img/{userId}/{catId}/{subCatId}/{itemId}{1,2,3}.jpg
			File folderPath = new File(IOUtil.calculateFileName(item));
			folderPath.mkdirs();
			System.out.println("Creating folder " + folderPath.getAbsolutePath());
			
			// Get buffered image
			BufferedImage buffImg = ImageIO.read(new ByteArrayInputStream(image1));
			// Create file
			File imgFile = new File(folderPath.getAbsolutePath()+"/"+imageFileName+".jpg");
			// Write image in file
			ImageIO.write(buffImg, "jpg", imgFile);
	        
	        item.setImgHome(IOUtil.calculateFileName(item) +"/"+ imageFileName + ".jpg");
        } catch(NullPointerException e) { } catch (IOException e) {
        	throw new IOException();
        }
        
        // save item
        item = itemRepository.save(item);
        // update the item with the image path (if processed OK)
        return item;
    }
    
    @Override
    public Item addItem(Item item, String subCategoryName, String provinceName, byte[] imgBytes) 
    		throws InvalidItemNameMinLenghtException, UserNotFoundException, 
    		SubCategoryNotFoundException, ProvinceNotFoundException, IOException {
    	// FIXME: Check if user is loggued, apply security permissions
    	User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	item.setUser(user);
    	
    	return addItem(user.getLogin(), subCategoryName, item.getTitle(), 
    			item.getDescription(), provinceName, item.getPrize().doubleValue(), imgBytes);
    	
    }
    
    private String itemHash(Item item) {
    	return String.valueOf(
    			(item.getUser().getUserId() + Calendar.getInstance().getTimeInMillis()) );
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
    
    /** For simplicity I will work with integers, but passing to repository BigDecimals **/
    @Override
	public Page<Item> getAllItemsByCategory(String categoryName,
			int prizeMin, int prizeMax, int page, int size) {
		BigDecimal bPrizeMin = new BigDecimal(prizeMin);
		BigDecimal bPrizeMax = new BigDecimal(prizeMax);

		// Don't know if a negative number can break this, so I'm preventing
		if (prizeMin < 0)
			bPrizeMin = new BigDecimal(0);
		// Max prize its not present
		if (prizeMax == 0)
			return itemRepository.findItemsByCategoryName(categoryName,
					bPrizeMin, new PageRequest(page, size));
		// Take care of minimum number grater than maximum number, ignoring the request
		if (prizeMin > prizeMax)
			return itemRepository.findItemsByCategoryName(categoryName,
					new PageRequest(page, size));
		// Otherwise, use min and max prize for query
		return itemRepository.findItemsByCategoryName(categoryName,
				bPrizeMin, bPrizeMax, new PageRequest(page, size));
	}

    @Override
    @Deprecated
    public Page<Item> getAllItemsBySubCategory(String subCategoryName, int page, int size) {
        return itemRepository.findItemsBySubCategoryName(subCategoryName, 
        		new PageRequest(page, size));
    }
    
    /** For simplicity I will work with integers, but passing to repository BigDecimals **/
	@Override
	public Page<Item> getAllItemsBySubCategory(String subCategoryName,
			int prizeMin, int prizeMax, int page, int size) {
		BigDecimal bPrizeMin = new BigDecimal(prizeMin);
		BigDecimal bPrizeMax = new BigDecimal(prizeMax);

		// Don't know if a negative number can break this, so I'm preventing
		if (prizeMin < 0)
			bPrizeMin = new BigDecimal(0);
		// Max prize its not present
		if (prizeMax == 0)
			return itemRepository.findItemsBySubCategoryName(subCategoryName,
					bPrizeMin, new PageRequest(page, size));
		// Take care of minimum number grater than maximum number, ignoring the request
		if (prizeMin > prizeMax)
			return itemRepository.findItemsBySubCategoryName(subCategoryName,
					new PageRequest(page, size));
		// Otherwise, use min and max prize for query
		return itemRepository.findItemsBySubCategoryName(subCategoryName,
				bPrizeMin, bPrizeMax, new PageRequest(page, size));
	}

    @Override
    public Item updateItem(Long itemId, String title, String description, double prize) {
        Item item = itemRepository.findOne(itemId);
        if (title != null) item.setTitle(title);
        if (description != null) item.setDescription(description);
        if (prize != -1) item.setPrize(new BigDecimal(prize));
        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.delete(itemId);
    }
}
