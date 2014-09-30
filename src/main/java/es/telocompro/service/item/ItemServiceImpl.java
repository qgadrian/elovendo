package es.telocompro.service.item;

import static es.telocompro.util.Constant.MIN_ITEM_TITLE_LENGHT;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.ItemForm;
import es.telocompro.model.item.ItemRepository;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.user.User;
import es.telocompro.rest.exception.ItemNotFoundException;
import es.telocompro.rest.exception.NotUserItemException;
import es.telocompro.rest.exception.ProvinceNotFoundException;
import es.telocompro.rest.exception.SubCategoryNotFoundException;
import es.telocompro.rest.exception.UserNotFoundException;
import es.telocompro.service.exception.InvalidItemNameMinLenghtException;
import es.telocompro.service.item.category.CategoryService;
import es.telocompro.service.user.UserService;
import es.telocompro.util.Constant;
import es.telocompro.util.IOUtil;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */

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
    	SubCategory subCategory = categoryService.getSubCategoryBySubCategoryId(subCategoryId);
//        Province province = provinceService.findProvinceByName(provinceName);
    	
    	if (title.length() < MIN_ITEM_TITLE_LENGHT) throw new InvalidItemNameMinLenghtException(title);
    	if (user == null) throw new UserNotFoundException(userName);
    	if (subCategory == null) throw new SubCategoryNotFoundException(subCategoryId);
//    	if (province == null) throw new ProvinceNotFoundEx01ception(provinceName);
    	
    	double latitude = Double.parseDouble(_latitude);
    	double longitude = Double.parseDouble(_longitude);
    	
    	// Randomize exact latitude
    	Random random = new Random();
    	double rangeMax = 0.003;
    	double rangeMin = 0.001;
    	
    	if (random.nextInt(2) == 0)
    		latitude = latitude + (rangeMin + (rangeMax - rangeMin) * random.nextDouble());
    	else
    		latitude = latitude - (rangeMin + (rangeMax - rangeMin) * random.nextDouble());
    	if (random.nextInt(2) == 0)
    		longitude = longitude + (rangeMin + (rangeMax - rangeMin) * random.nextDouble());
    	else
    		longitude = longitude - (rangeMin + (rangeMax - rangeMin) * random.nextDouble());
    	
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
    
    /**
     * Saves an image to disk naming as unique filename
     * @param item
     * @param bytes
     * @return Image file path
     */
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
    	SubCategory subCategory = categoryService.getSubCategoryBySubCategoryId(subCategoryId);
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
    
    @Override
	public Item addItem(ItemForm itemForm, User user) {
		// TODO Auto-generated method stub
		return null;
	}
    
	@Override
	public Item updateItem(ItemForm itemForm, User user, long subCategoryId, MultipartFile mainImage,
			MultipartFile image1, MultipartFile image2, MultipartFile image3)
			throws ItemNotFoundException, NotUserItemException {
		
		if (!itemForm.getUser().equals(user)) 
			throw new NotUserItemException(itemForm.getItemId(), user.getUserId());
		
		Item item = getItemById(itemForm.getItemId());
		if (item == null) throw new ItemNotFoundException(itemForm.getItemId());
		
		// If subCategory is changed, get it
		if (subCategoryId != item.getSubCategory().getId()) {
			SubCategory subCategory = categoryService.getSubCategoryBySubCategoryId(subCategoryId);
			item.setSubCategory(subCategory);
		}
		
		// Save new images (produce an unique name for an item)
        if (!mainImage.isEmpty()) {
        	byte[] mainImageBytes = null;
        	try { mainImageBytes = mainImage.getBytes(); // Main image
    		} catch (IOException e) { logger.debug("Error converting image for user " + user.getUserId()); }
        	item.setMainImage(saveImage(item, mainImageBytes));
        }
        if (!image1.isEmpty()) {
        	byte[] image1Bytes = null;
        	try { image1Bytes = image1.getBytes(); // Image 1
    		} catch (IOException e) { logger.debug("Error converting image for user " + user.getUserId()); }
        	item.setImage1(saveImage(item, image1Bytes));
        }
        if (!image2.isEmpty()) {
        	byte[] image2Bytes = null;
        	try { image2Bytes = image2.getBytes(); // Image 2
    		} catch (IOException e) { logger.debug("Error converting image for user " + user.getUserId()); }
        	item.setImage2(saveImage(item, image2Bytes));
        }
        if (!image3.isEmpty()) {
        	byte[] image3Bytes = null;
        	try { image3Bytes = image3.getBytes(); // Image 3
    		} catch (IOException e) { logger.debug("Error converting image for user " + user.getUserId()); }
        	item.setImage3(saveImage(item, image3Bytes));
        }
        
		item.setTitle(itemForm.getTitle());
		item.setDescription(itemForm.getDescription());
		item.setPrize(itemForm.getPrize());
		item.setSubCategory(itemForm.getSubCategory());
		item.setYoutubeVideo(itemForm.getYoutubeVideo());
		item.setFeatured(itemForm.isFeatured());
		item.setHighlight(itemForm.isHighlight());
		item.setAutoRenew(itemForm.isAutoRenew());
		
		// If lat or lng are different from original, randomize the given ones
		if (itemForm.getLatitude() != item.getLatitude() || itemForm.getLongitude() != item.getLongitude()) {
			Double[] latLng = randomizeCoordinates(itemForm.getLatitude(), itemForm.getLongitude());
			item.setLatitude(latLng[0]);
			item.setLongitude(latLng[1]);
		}
		
		return itemRepository.save(item);
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
    public List<Item> getAllItemsByUserName(String userName) {
        return itemRepository.findByUserName(userName);
    }
    
    @Override
    public List<Item> getAllItemsByUser(User user) {
        return getAllItemsByUserId(user.getUserId());
    }
    
    @Override
    public List<Item> getAllItemsByUserId(Long userId) {
        return itemRepository.findByUserId(userId);
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
	
	@Override
    public Page<Item> getItemsByParams(String title, String subCategory, double dis, double lat, double lng,
    		int prizeMin, int prizeMax, int page, int size) {

		// Page Request
		PageRequest pageRequest = new PageRequest(page, size);

		BigDecimal bPrizeMin = prizeMin > 0 ? new BigDecimal(prizeMin) : new BigDecimal(0);
		BigDecimal bPrizeMax = prizeMin > prizeMax ? new BigDecimal(0) : new BigDecimal(prizeMax);
		
		if (dis <= 0) dis = Constant.DEFAULT_RADIUS_SEARCH;
		
		if (subCategory.equalsIgnoreCase(Constant.ALL_PATH)) subCategory = "";
		
		logger.error("dis : " + dis + " ; lat: " + lat + " ; lng: " + lng + " cat: " + subCategory
				+ " min " + bPrizeMin + " max " + bPrizeMax);
		
		return itemRepository.findByParams(title, subCategory, lat, lng, dis, 
				bPrizeMin.doubleValue(), bPrizeMax.doubleValue(), pageRequest);
		
	}
	
	public List<Item> getRandomFeaturedItems(int maxItems, String filter) {
		if (filter != null && !filter.equals(""))
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

	@Override
	public int getNumberUserItems(User user) {
		return itemRepository.findNumberUserItems(user.getUserId());
	}
	
	@Override
	public int getNumberUserItems(Long userId) {
		return itemRepository.findNumberUserItems(userId);
	}

	@Override
	public Iterable<Item> getAll(Iterable<Long> ids) {
		return itemRepository.findAll(ids);
	}
	
	/**
	 * Returns randomized coordinates by the given ones.
	 * @param latitude
	 * @param longitude
	 * @return Array of two elements, [LAT, LNG]
	 */
	private Double[] randomizeCoordinates(double latitude, double longitude) {
    	// Randomize exact latitude
    	Random random = new Random();
    	double rangeMax = 0.003;
    	double rangeMin = 0.001;
    	
    	if (random.nextInt(2) == 0)
    		latitude = latitude + (rangeMin + (rangeMax - rangeMin) * random.nextDouble());
    	else
    		latitude = latitude - (rangeMin + (rangeMax - rangeMin) * random.nextDouble());
    	if (random.nextInt(2) == 0)
    		longitude = longitude + (rangeMin + (rangeMax - rangeMin) * random.nextDouble());
    	else
    		longitude = longitude - (rangeMin + (rangeMax - rangeMin) * random.nextDouble());
    	
    	Double[] coords = {latitude, longitude};
    	
    	return coords;
	}
	
}
