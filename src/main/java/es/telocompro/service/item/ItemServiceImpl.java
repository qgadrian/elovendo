package es.telocompro.service.item;

import static es.telocompro.util.Constant.MIN_ITEM_TITLE_LENGHT;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.ItemRepository;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.user.User;
import es.telocompro.rest.controller.exception.ItemNotFoundException;
import es.telocompro.rest.controller.exception.ProvinceNotFoundException;
import es.telocompro.rest.controller.exception.SubCategoryNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;
import es.telocompro.service.exception.InvalidItemNameMinLenghtException;
import es.telocompro.service.item.category.CategoryService;
import es.telocompro.service.user.UserService;
import es.telocompro.util.Constant;
import es.telocompro.util.IOUtil;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */

@SuppressWarnings(value = "unused")
@Service("itemService")
public class ItemServiceImpl implements ItemService {
	
	Logger logger = Logger.getLogger(ItemServiceImpl.class);

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;

    @Override
    public Item addItem(String userName, long subCategoryId, String title, String description, 
    		double prize, byte[] mainImage, byte[] image1, byte[] image2,
    		byte[] image3, String youtubeVideo, boolean featured, boolean highlight, 
    		String _latitude, String _longitude)
    				throws InvalidItemNameMinLenghtException, UserNotFoundException, SubCategoryNotFoundException, 
    				ProvinceNotFoundException {
    	
    	User user = userService.findUserByLogin(userName);
//    	SubCategory subCategory = categoryService.getSubCategoryByName(subCategoryId);
    	SubCategory subCategory = categoryService.findSubCategoryBySubCategoryId(subCategoryId);
//        Province province = provinceService.findProvinceByName(provinceName);
    	
    	if (title.length() < MIN_ITEM_TITLE_LENGHT) throw new InvalidItemNameMinLenghtException(title);
    	if (user == null) throw new UserNotFoundException(userName);
    	if (subCategory == null) throw new SubCategoryNotFoundException(subCategoryId);
//    	if (province == null) throw new ProvinceNotFoundEx01ception(provinceName);
    	
    	double latitude = Double.parseDouble(_latitude);
    	double longitude = Double.parseDouble(_longitude);
    	
    	// create item
        Item item = new Item(user, subCategory, title, description,
        		new BigDecimal(prize), null, null, null, null, youtubeVideo,
        		featured, highlight, latitude, longitude);
        
        // Produce an unique name for an item
        if (mainImage != null) item.setMainImage(saveImage(item, mainImage));
        if (image1 != null) item.setImage1(saveImage(item, image1));
        if (image2 != null) item.setImage2(saveImage(item, image2));
        if (image3 != null) item.setImage3(saveImage(item, image3));
        
        // SAVE ITEM
        item = itemRepository.save(item);
        // update the item with the image path (if processed OK)
        return item;
    }
    
    private String saveImage(Item item, byte[] bytes) {
    	String imageFileName = itemHash(item);
    	
    	if (bytes != null) //FIXME: Temporal line for test
            try {
    	        /** SAVE IMAGE IN A RESOURCE FOLDER **/
    			// Create folder for /img/{userId}/{catId}/{subCatId}/{itemId}{1,2,3}.jpg
    			File folderPath = new File(IOUtil.calculateFileName(item));
    			folderPath.mkdirs();
    			System.out.println("Creating folder " + folderPath.getAbsolutePath());
    			
    			// Get buffered image
    			BufferedImage buffImg = ImageIO.read(new ByteArrayInputStream(bytes));
    			// Create file
    			File imgFile = new File(folderPath.getAbsolutePath()+"/"+imageFileName+".jpg");
    			// Write image in file
    			ImageIO.write(buffImg, "jpg", imgFile);
    			
    			/* IMAGE RESIZED */
    			BufferedImage resizedImage = Scalr.resize(buffImg, 800);
    			// Create file
    			File imgResizedFile = new File(folderPath.getAbsolutePath()+"/"+imageFileName+"-200h.jpg");
    			// Write image in file
    			ImageIO.write(resizedImage, "jpg", imgResizedFile);
    			
    	        
//    	        item.setImgHome(IOUtil.calculateFileName(item) +"/"+ imageFileName + ".jpg");
//    			item.setMainImage(IOUtil.calculateFileName(item) +"/"+ imageFileName);
    			
    			logger.warn("going to return " + IOUtil.calculateFileName(item) +"/"+ imageFileName);
    			String absolutePath = IOUtil.calculateFileName(item) +"/"+ imageFileName; 
    			return absolutePath;
    			
            } catch(NullPointerException | IOException | IllegalArgumentException e)  { 
            	logger.error("Error writing image '" + imageFileName + "' !");
            }
    	
    	return null;
    }

    @Override
    public Item addItem(Item item, long subCategoryId, byte[] mainImage,
    		byte[] image1, byte[] image2, byte[] image3, boolean featured, boolean highlight) 
    		throws InvalidItemNameMinLenghtException, UserNotFoundException, 
    		SubCategoryNotFoundException, ProvinceNotFoundException {
    	
    	// FIXME: Check if user is loggued, apply security permissions
    	User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	item.setUser(user);
    	
//    	SubCategory subCategory = categoryService.getSubCategoryByName(subCategoryName);
    	SubCategory subCategory = categoryService.findSubCategoryBySubCategoryId(subCategoryId);
//        Province province = provinceService.findProvinceByName(provinceName);
        
        // Validation
    	if (item.getTitle().length() < MIN_ITEM_TITLE_LENGHT) 
    		throw new InvalidItemNameMinLenghtException(item.getTitle());
    	if (user == null) throw new UserNotFoundException(item.getUser().getLogin());
    	if (subCategory == null) throw new SubCategoryNotFoundException(subCategoryId);
//    	if (province == null) throw new ProvinceNotFoundException(provinceName);
        
        item.setSubCategory(subCategory);
//        item.setProvince(province);
        item.setFeatured(featured);
        item.setHighlight(highlight);
        
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, Constant.ITEM_DEFAULT_DURATION);
        item.setEndDate(endDate);
        item.setStartDate(Calendar.getInstance());
        
        // Produce an unique name for an item
        if (mainImage != null) item.setMainImage(saveImage(item, mainImage));
        if (image1 != null) item.setImage1(saveImage(item, image1));
        if (image2 != null) item.setImage2(saveImage(item, image2));
        if (image3 != null) item.setImage3(saveImage(item, image3));
        
        // SAVE ITEM
        item = itemRepository.save(item);
        // update the item with the image path (if processed OK)
        return item;
    	
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
    public Item getItemById(Long itemId) throws ItemNotFoundException {
    	Item item = itemRepository.findOne(itemId);
    	if (item == null)
    		throw new ItemNotFoundException(itemId);
        return item;
    }

//    @Override
//    public Page<Item> getItemByTitleAndSubCategory(String title, String subCategory, int page, int size) {
//        if (subCategory != null && !subCategory.equalsIgnoreCase(""))
//        	return itemRepository.findByTitleAndSubCategory(title, subCategory, new PageRequest(page, size));
//        else
//        	return itemRepository.findByTitle(title, new PageRequest(page, size));
//    }
    
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
			return itemRepository.findItemsByCategoryNameMin(categoryName,
					bPrizeMin, new PageRequest(page, size));
		// Take care of minimum number grater than maximum number, ignoring the request
		if (prizeMin > prizeMax)
			return itemRepository.findItemsByCategoryName(categoryName,
					new PageRequest(page, size));
		// Otherwise, use min and max prize for query
		return itemRepository.findItemsByCategoryNameMinMax(categoryName,
				bPrizeMin, bPrizeMax, new PageRequest(page, size));
	}

//    @Override
//    @Deprecated
//    public Page<Item> getAllItemsBySubCategory(String subCategoryName, int page, int size) {
//        return itemRepository.findBySubCategoryName(subCategoryName, 
//        		new PageRequest(page, size));
//    }
//    
//    /** For simplicity I will work with integers, but passing to repository BigDecimals **/
//    @Deprecated
//	@Override
//	public Page<Item> getAllItemsBySubCategory(String subCategoryName,
//			int prizeMin, int prizeMax, int page, int size) {
//		BigDecimal bPrizeMin = new BigDecimal(prizeMin);
//		BigDecimal bPrizeMax = new BigDecimal(prizeMax);
//
//		// Don't know if a negative number can break this, so I'm preventing
//		if (prizeMin < 0)
//			bPrizeMin = new BigDecimal(0);
//		// Max prize its not present
//		if (prizeMax == 0)
//			return itemRepository.findBySubCategoryNameMin(subCategoryName,
//					bPrizeMin, new PageRequest(page, size));
//		// Take care of minimum number grater than maximum number, ignoring the request
//		if (prizeMin > prizeMax)
//			return itemRepository.findBySubCategoryName(subCategoryName,
//					new PageRequest(page, size));
//		// Otherwise, use min and max prize for query
//		return itemRepository.findBySubCategoryNameMinMax(subCategoryName,
//				bPrizeMin, bPrizeMax, new PageRequest(page, size));
//	}
	
//	@Override
//	@Deprecated
//    public Page<Item> getItemByParams(String title, String subCategory, String province,
//    		int prizeMin, int prizeMax, int page, int size) {
//
//		// Page Request
//        PageRequest pageRequest = new PageRequest(page, size);
//        
//        BigDecimal bPrizeMin = prizeMin > 0 ? new BigDecimal(prizeMin) : null;
//		BigDecimal bPrizeMax = new BigDecimal(prizeMax);
//		
//		if (prizeMin > prizeMax || (prizeMin == 0 && prizeMax == 0))
//			if (province.equals(""))
//				return itemRepository.findByParamsWithSubCat(title, subCategory, pageRequest);
//			else 
//				if (subCategory.equals(""))
//					return itemRepository.findByParamsWithProvince(title, province, pageRequest);
//				else 
//					return itemRepository.findByParams(title, subCategory, province, pageRequest);
//		else // using minimum prize
//			if (prizeMax == 0)
//				if (province.equals(""))
//					return itemRepository.findByParamsWithSubCatMin(title, subCategory, bPrizeMin, pageRequest);
//				else 
//					if (subCategory.equals(""))
//						return itemRepository.findByParamsWithProvinceMin(title, province, bPrizeMin, pageRequest);
//					else 
//						return itemRepository.findByParamsMin(title, subCategory, province, bPrizeMin, pageRequest);
////				return itemRepository.findByParamsMin(title, subCategory, province, bPrizeMin, pageRequest);
//			else
//				if (province.equals(""))
//					return itemRepository.findByParamsWithSubCatMinMax(title, subCategory, bPrizeMin, bPrizeMax, pageRequest);
//				else 
//					if (subCategory.equals(""))
//						return itemRepository.findByParamsWithProvinceMinMax(title, province, bPrizeMin, bPrizeMax, pageRequest);
//					else 
//						return itemRepository.findByParamsMinMax(title, subCategory, province, bPrizeMin, bPrizeMax, pageRequest);
////				return itemRepository
////						.findByParamsMinMax(title, subCategory, province, bPrizeMin, bPrizeMax, pageRequest);
//     			
////     			if (prizeMax == 0)
////     				return itemRepository.findBySubCategoryNameMin(subCategory, bPrizeMin, pageRequest);
////     			// Take care of minimum number grater than maximum number, ignoring the request
////     			// or if there is no prize min assigned
////     			if (prizeMin > prizeMax || bPrizeMin == null)
////     				if (prizeMax == 0) // Max prize its not present, do not use prize filter
////	     				if (!subCategory.equals(""))
////	     					if (!title.equals(""))
////	     						if (!province.equals(""))
////	     							return itemRepository.findByTitleAndSubCategoryAndProvince(
////	     									title, subCategory, province, new PageRequest(page, size));
////	     						else return itemRepository.findByTitleAndSubCategory(title, subCategory, pageRequest);
////	     					else 
////	     						if (!province.equals(""))
////         							return itemRepository.findBySubCategoryAndProvince(
////         									subCategory, province, pageRequest);
////         						else return itemRepository.findBySubCategoryName(subCategory, pageRequest);
////	     				else return itemRepository.findAll(pageRequest);
////     				else // use max prize
////     					if (!subCategory.equals(""))
////         					if (!title.equals(""))
////         						if (!province.equals(""))
////         							return itemRepository.findByTitleAndSubCategoryAndProvinceMax(
////         									title, subCategory, province, bPrizeMax, pageRequest);
////         						else return itemRepository.findByTitleAndSubCategoryMax(
////         								title, subCategory, bPrizeMax, pageRequest);
////         					else
////         						if (!province.equals(""))
////         							return itemRepository.findBySubCategoryAndProvinceMax(
////         									subCategory, province, bPrizeMax, pageRequest);
////         						else return itemRepository.findBySubCategoryNameMax(subCategory, bPrizeMax, pageRequest);
////     					else return itemRepository.findAllItemsMax(bPrizeMax, pageRequest);
////     			// Minimum prize is present
////     			else
////     				if (prizeMax == 0) // Max prize its not present, use minimum only
////     					if (!subCategory.equals(""))
////         					if (!title.equals(""))
////         						if (!province.equals(""))
////         							return itemRepository.findByTitleAndSubCategoryAndProvinceMin(
////         									title, subCategory, province, bPrizeMin, pageRequest);
////         						else return itemRepository.findByTitleAndSubCategoryMin(
////         								title, subCategory, bPrizeMin, pageRequest);
////         					else
////         						if (!province.equals(""))
////         							return itemRepository.findBySubCategoryAndProvinceMin(
////         									subCategory, province, bPrizeMin, pageRequest);
////         						else return itemRepository.findBySubCategoryNameMin(subCategory, bPrizeMin, pageRequest);
////     					else return itemRepository.findAllItemsMin(bPrizeMin, pageRequest);
////	 				else // use min and max prize 
////	 					if (!subCategory.equals(""))
////         					if (!title.equals(""))
////         						if (!province.equals("")) 
////         							return itemRepository.findByTitleAndSubCategoryAndProvinceMinMax(
////     									title, subCategory, province, bPrizeMin, bPrizeMax, pageRequest);
////         						else return itemRepository.findByTitleAndSubCategoryMinMax(
////         								title, subCategory, bPrizeMin, bPrizeMax, pageRequest);
////         					else 
////         						if (!province.equals(""))
////         							return itemRepository.findBySubCategoryAndProvinceMinMax(
////         									subCategory, province, bPrizeMin, bPrizeMax, pageRequest);
////         						else 
////         							return itemRepository.findBySubCategoryNameMinMax(
////         									subCategory, bPrizeMin, bPrizeMax, pageRequest);
////	 					else return itemRepository.findAllItemsMinMax(bPrizeMin, bPrizeMax, pageRequest);
//    }
	
	@Override
    public Page<Item> getItemByParams2(String title, String subCategory, double dis, float[] location,
    		int prizeMin, int prizeMax, int page, int size) {
		
		double lat = (double) location[0];
		double lng = (double) location[1];

		// Page Request
        PageRequest pageRequest = new PageRequest(page, size);
        
        BigDecimal bPrizeMin = prizeMin > 0 ? new BigDecimal(prizeMin) : null;
		BigDecimal bPrizeMax = new BigDecimal(prizeMax);
		
		logger.error("dis : " + dis + " ; lat: " + lat + " ; lng: " + lng);
		
		if (prizeMin > prizeMax || (prizeMin == 0 && prizeMax ==0)) {
//			if (dis == 0) {
//				return itemRepository.findByParamsWithSubCat(title, subCategory, pageRequest);
//			}
//			else {
				if (dis == 0) dis = Constant.DEFAULT_RADIUS_SEARCH;
				if (subCategory.equals("")) {
					logger.error("using this with dis " + dis);
					return itemRepository.findByParamsWithDistance(title, lat, lng, dis, pageRequest);
				}
				else {
					logger.error("using subcat with dis " + dis);
					return itemRepository.findByParams(title, subCategory, lat, lng, dis, pageRequest);
				}
//			}
		}
		else { // using minimum prize
			if (prizeMax == 0) {
				if (dis == 0) {
					return itemRepository.findByParamsWithSubCatMin(title, subCategory, bPrizeMin, pageRequest);
				}
				else { 
					if (subCategory.equals("")) {
						return itemRepository.findByParamsWithDistanceMin(title, lat, lng, dis, bPrizeMin, pageRequest);
					}
					else { 
						return itemRepository.findByParamsMin(title, subCategory, lat, lng, dis, bPrizeMin, pageRequest);
					}
				}
			}
			else {
				if (dis == 0) {
					return itemRepository.findByParamsWithSubCatMinMax(title, subCategory, bPrizeMin, bPrizeMax, pageRequest);
				}
				else { 
					if (subCategory.equals("")) {
						return itemRepository.findByParamsWithDistanceMinMax(title, lat, lng, dis, bPrizeMin, bPrizeMax, pageRequest);
					}
					else { 
						return itemRepository.findByParamsMinMax(title, subCategory, lat, lng, dis, bPrizeMin, bPrizeMax, pageRequest);
					}
				}
			}
		}
	}
	
	public List<Item> getRandomFeaturedItems(int maxItems, String filter) {
		if (filter != null && filter != "")
			return itemRepository.findRandomFeaturedItemsByFilter(new PageRequest(0, maxItems), filter);
		else
			return itemRepository.findRandomFeaturedItems(new PageRequest(0, maxItems));
	}

    @Override
    public Item updateItem(Long itemId, String title, String description, double prize, boolean renew,
    		boolean featured, boolean highlight) {
        Item item = itemRepository.findOne(itemId);
        if (title != null) item.setTitle(title);
        if (description != null) item.setDescription(description);
        if (prize != -1) item.setPrize(new BigDecimal(prize));
        
        if (renew != false) {
        	Calendar newEndDate = Calendar.getInstance();
        	newEndDate.add(Calendar.DATE, Constant.DEFAULT_RENEW_DAYS);
        	item.setEndDate(newEndDate);
        }
        
        item.setFeatured(featured);
        item.setHighlight(highlight);
        
        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.delete(itemId);
    }
}
