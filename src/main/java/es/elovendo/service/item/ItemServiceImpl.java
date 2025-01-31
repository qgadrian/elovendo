package es.elovendo.service.item;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.elasticsearch.common.Strings;
import org.imgscalr.Scalr;
import org.json.simple.parser.ParseException;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.elovendo.model.item.Item;
import es.elovendo.model.item.ItemForm;
import es.elovendo.model.item.ItemRepository;
import es.elovendo.model.item.category.subcategory.SubCategory;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.InsufficientPointsException;
import es.elovendo.rest.exception.ItemNotFoundException;
import es.elovendo.rest.exception.NotUserItemException;
import es.elovendo.rest.exception.RenewItemAfterEndDateException;
import es.elovendo.rest.exception.SubCategoryNotFoundException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.service.admin.report.item.ItemReportService;
import es.elovendo.service.exception.InvalidItemNameMinLenghtException;
import es.elovendo.service.item.category.CategoryService;
import es.elovendo.service.item.favorite.FavoriteService;
import es.elovendo.service.user.UserService;
import es.elovendo.util.Constant;
import es.elovendo.util.IOUtil;
import es.elovendo.util.LocaleHelper;
import es.elovendo.util.currency.CurrencyConverter;
import es.elovendo.util.currency.CurrencyLocaler;

/**
 * Created by @adrian on 17/06/14. All rights reserved.
 */

@Service("itemService")
public class ItemServiceImpl implements ItemService {

	private Logger logger = Logger.getLogger(ItemServiceImpl.class);

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private FavoriteService favoriteService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ItemReportService itemReportService;

	@Override
	public Item addItem(String userName, long subCategoryId, String title, String description, String currency,
			double prize, byte[] mainImage, byte[] image1, byte[] image2, byte[] image3, String youtubeVideo,
			boolean featured, boolean highlight, boolean autoRenew, String _latitude, String _longitude)
			throws InvalidItemNameMinLenghtException, UserNotFoundException, SubCategoryNotFoundException,
			FileNotFoundException, IOException, ParseException {

		User user = userService.findUserByLogin(userName);
		// SubCategory subCategory =
		// categoryService.getSubCategoryByName(subCategoryId);
		SubCategory subCategory = categoryService.getSubCategoryBySubCategoryId(subCategoryId);
		// Province province = provinceService.findProvinceByName(provinceName);

		if (title.length() < Constant.MIN_ITEM_TITLE_LENGHT)
			throw new InvalidItemNameMinLenghtException(title);
		if (user == null)
			throw new UserNotFoundException(userName);
		if (subCategory == null)
			throw new SubCategoryNotFoundException(subCategoryId);

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

		BigDecimal bdPrize = new BigDecimal(prize);
		BigDecimal normalizedPrize = normalizePrize(bdPrize, currency);

		// create item
		Item item = new Item(user, subCategory, title, description, currency, bdPrize, normalizedPrize, null, null,
				null, null, youtubeVideo, featured, highlight, autoRenew, latitude, longitude);

		// Produce an unique name for an item
		if (mainImage != null)
			item.setMainImage(saveImage(item, mainImage));
		if (image1 != null)
			item.setImage1(saveImage(item, image1));
		if (image2 != null)
			item.setImage2(saveImage(item, image2));
		if (image3 != null)
			item.setImage3(saveImage(item, image3));

		// SAVE ITEM
		item = itemRepository.save(item);
		// update the item with the image path (if processed OK)
		return item;
	}

	@Override
	public Item addItem(ItemForm itemForm, User user, MultipartFile mainImage, MultipartFile image1,
			MultipartFile image2, MultipartFile image3) throws SubCategoryNotFoundException,
			InsufficientPointsException, FileNotFoundException, IOException, ParseException {

		SubCategory subCategory = categoryService.getSubCategoryBySubCategoryId(itemForm.getSubCategory());

		// Check if user's points are enough to purchase premium options
		// selected
		int totalPoints = 0;
		if (itemForm.isFeatured())
			totalPoints += Constant.OP_FEATURE_PRIZE;
		if (itemForm.isHighlight())
			totalPoints += Constant.OP_HIGLIGHT_PRIZE;
		if (itemForm.isAutoRenew())
			totalPoints += Constant.OP_AUTORENEW_PRIZE;

		if (user.getPoints() < totalPoints)
			throw new InsufficientPointsException();
		else {
			user.setPoints(user.getPoints() - totalPoints);
			user = userService.updateUser(user);
			// Update session user with the new points balance
			Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
					user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		// Sanitize item description HTML string
		// TODO: more check on tags used
		PolicyFactory policy = new HtmlPolicyBuilder()
				.allowElements("b", "u", "i", "p", "br", "h1", "h2", "h3", "h4", "h5", "h6", "span", "ul", "li")
				.allowAttributes("class")
				.onElements("b", "u", "i", "p", "br", "h1", "h2", "h3", "h4", "h5", "h6", "span", "ul", "li")
				.requireRelNofollowOnLinks().toFactory();

		String safeDescription = policy.sanitize(itemForm.getDescription());
		itemForm.setDescription(safeDescription);

		// If YouTube video format it's not embed
		if (!itemForm.getYoutubeVideo().equals("")) {
			String wellYoutubeVideo = itemForm.getYoutubeVideo();
			wellYoutubeVideo = Strings.replace(wellYoutubeVideo, "watch?v=", "embed/");
			itemForm.setYoutubeVideo(wellYoutubeVideo);
		}

		BigDecimal normalizedPrize = normalizePrize(itemForm.getPrize(), itemForm.getCurrency());

		Item item = new Item(user, subCategory, itemForm.getTitle(), itemForm.getDescription(), itemForm.getCurrency(),
				itemForm.getPrize(), normalizedPrize, itemForm.getYoutubeVideo(), itemForm.isFeatured(),
				itemForm.isHighlight(), itemForm.isAutoRenew(), itemForm.getLatitude(), itemForm.getLongitude());

		// Save new images (produce an unique name for an item)
		item = saveMultiPartFileImage(item, mainImage, image1, image2, image3);

		// Randomize coordinates
		Double[] latLng = randomizeCoordinates(itemForm.getLatitude(), itemForm.getLongitude());
		item.setLatitude(latLng[0]);
		item.setLongitude(latLng[1]);

		return itemRepository.save(item);
	}

	@Override
	public Item updateItem(ItemForm itemForm, User user, MultipartFile mainImage, MultipartFile image1,
			MultipartFile image2, MultipartFile image3) throws ItemNotFoundException, NotUserItemException,
			SubCategoryNotFoundException, InsufficientPointsException {

		if (!itemForm.getUser().equals(user))
			throw new NotUserItemException(itemForm.getItemId(), user.getUserId());

		Item item = getItemById(itemForm.getItemId());
		if (item == null)
			throw new ItemNotFoundException(itemForm.getItemId());

		// If subCategory is changed, get it
		SubCategory subCategory = categoryService.getSubCategoryBySubCategoryId(itemForm.getSubCategory());

		// Check if user's points are enough to purchase premium options
		// selected
		int totalPoints = 0;
		if (itemForm.isFeatured())
			totalPoints += Constant.OP_FEATURE_PRIZE;
		if (itemForm.isHighlight())
			totalPoints += Constant.OP_HIGLIGHT_PRIZE;
		if (itemForm.isAutoRenew())
			totalPoints += Constant.OP_AUTORENEW_PRIZE;

		if (user.getPoints() < totalPoints) {
			throw new InsufficientPointsException();
		} else {
			user.setPoints(user.getPoints() - totalPoints);
			user = userService.updateUser(user);
			// Update session user with the new points balance
			Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
					user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		// Set main image if the given is empty but there is other images sent
		if (mainImage.isEmpty()) {
			if (!image1.isEmpty()) {
				mainImage = image1;
				image1 = null;
			} else if (!image2.isEmpty()) {
				mainImage = image2;
				image2 = null;
			} else if (!image3.isEmpty()) {
				mainImage = image3;
				image3 = null;
			}
		}

		// Save new images (produce an unique name for an item)
		item = saveMultiPartFileImage(item, mainImage, image1, image2, image3);

		item.setTitle(itemForm.getTitle());
		item.setDescription(itemForm.getDescription());
		item.setPrize(itemForm.getPrize());
		item.setSubCategory(subCategory);
		item.setYoutubeVideo(itemForm.getYoutubeVideo());
		item.setFeatured(itemForm.isFeatured());
		item.setHighlight(itemForm.isHighlight());
		item.setAutoRenew(itemForm.isAutoRenew());

		// If latitude or longitude are different from original, randomize the
		// given ones
		if (itemForm.getLatitude() != item.getLatitude() || itemForm.getLongitude() != item.getLongitude()) {
			Double[] latLng = randomizeCoordinates(itemForm.getLatitude(), itemForm.getLongitude());
			item.setLatitude(latLng[0]);
			item.setLongitude(latLng[1]);
		}

		return itemRepository.save(item);
	}

	@Override
	public Page<Item> getAllItems(int page, int size) {
		PageRequest request = new PageRequest(page, size);
		return itemRepository.findAll(request);
	}

	@Override
	public Page<Item> getAllItemsByUserName(String userName, int page, int size) {

		// Protect page size
		if (size > Constant.MAX_PAGE_SIZE)
			size = Constant.MAX_PAGE_SIZE;

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
	public List<Item> getLastItems(User user) {
		return itemRepository.findLastItems(user.getUserId(), new PageRequest(0, Constant.MAX_LAST_ITEMS));
	}

	@Override
	public Item getItemById(Long itemId) throws ItemNotFoundException {
		Item item = itemRepository.findOne(itemId);
		if (item == null)
			throw new ItemNotFoundException(itemId);
		return item;
	}

	/**
	 * For simplicity I will work with integers, but passing to repository
	 * BigDecimals
	 **/
	@Override
	public Page<Item> getAllItemsByCategory(String categoryName, int prizeMin, int prizeMax, Locale locale, int page, int size) {
		
		BigDecimal bPrizeMin;
		BigDecimal bPrizeMax;
		try {
			// Get currency locale to calculate and get the normalized prize
			LocaleHelper localeHelper = LocaleHelper.getInstance();
			Locale localed = localeHelper.getFixedLocale(locale);
			CurrencyLocaler currencyLocaler = CurrencyLocaler.getInstance();
			Currency currencyLocaled = currencyLocaler.getCurrencyLocaled(localed);

			bPrizeMin = prizeMin > 0 ? normalizePrize(new BigDecimal(prizeMin), currencyLocaled) : new BigDecimal(0);
			bPrizeMax = prizeMin > prizeMax ? new BigDecimal(0) : new BigDecimal(prizeMax);
		} catch (Exception e) {
			logger.error("Error normalizing prizes");
			bPrizeMin = new BigDecimal(0);
			bPrizeMax = new BigDecimal(0);
		}

		// Protect page size
		if (size > Constant.MAX_PAGE_SIZE)
			size = Constant.MAX_PAGE_SIZE;

		// Don't know if a negative number can break this, so I'm preventing
		if (prizeMin < 0)
			bPrizeMin = new BigDecimal(0);
		// Max prize its not present
		if (prizeMax == 0)
			return itemRepository.findItemsByCategoryNameMin(categoryName, bPrizeMin, new PageRequest(page, size));
		// Take care of minimum number grater than maximum number, ignoring the
		// request
		if (prizeMin > prizeMax)
			return itemRepository.findItemsByCategoryName(categoryName, new PageRequest(page, size));
		// Otherwise, use min and max prize for query
		return itemRepository.findItemsByCategoryNameMinMax(categoryName, bPrizeMin, bPrizeMax, new PageRequest(page,
				size));
	}

	@Override
	public Page<Item> getItemsByParams(String title, String name, double dis, double lat, double lng, int prizeMin,
			int prizeMax, Locale locale, int page, int size) {

		// Protect page size
		if (size > Constant.MAX_PAGE_SIZE)
			size = Constant.MAX_PAGE_SIZE;

		// Page Request
		PageRequest pageRequest = new PageRequest(page, size);

		BigDecimal bPrizeMin;
		BigDecimal bPrizeMax;
		try {
			// Get currency locale to calculate and get the normalized prize
			LocaleHelper localeHelper = LocaleHelper.getInstance();
			Locale localed = localeHelper.getFixedLocale(locale);
			CurrencyLocaler currencyLocaler = CurrencyLocaler.getInstance();
			Currency currencyLocaled = currencyLocaler.getCurrencyLocaled(localed);

			bPrizeMin = prizeMin > 0 ? normalizePrize(new BigDecimal(prizeMin), currencyLocaled) : new BigDecimal(0);
			bPrizeMax = prizeMin > prizeMax ? new BigDecimal(0) : new BigDecimal(prizeMax);
		} catch (Exception e) {
			logger.error("Error normalizing prizes");
			bPrizeMin = new BigDecimal(0);
			bPrizeMax = new BigDecimal(0);
		}
		
		if (dis <= 0)
			dis = Constant.DEFAULT_RADIUS_SEARCH;

		if (name.equalsIgnoreCase(Constant.ALL_PATH))
			name = "";

		// logger.debug("dis : " + dis + " ; lat: " + lat + " ; lng: " + lng +
		// " cat: " + subCategory + " min "
		// + bPrizeMin + " max " + bPrizeMax);

		return itemRepository.findByParams(title, name, lat, lng, dis, bPrizeMin.doubleValue(),
				bPrizeMax.doubleValue(), pageRequest);
	}

	@Override
	public Page<Item> getItemsByParams(String title, long id, String type, double dis, double lat, double lng,
			int prizeMin, int prizeMax, Locale locale, int page, int size) {

		// Protect page size
		if (size > Constant.MAX_PAGE_SIZE)
			size = Constant.MAX_PAGE_SIZE;

		// Page Request
		PageRequest pageRequest = new PageRequest(page, size);

		// Normalize the min and max prize to search in DB

		BigDecimal bPrizeMin;
		BigDecimal bPrizeMax;
		try {
			// Get currency locale to calculate and get the normalized prize
			LocaleHelper localeHelper = LocaleHelper.getInstance();
			Locale localed = localeHelper.getFixedLocale(locale);
			CurrencyLocaler currencyLocaler = CurrencyLocaler.getInstance();
			Currency currencyLocaled = currencyLocaler.getCurrencyLocaled(localed);

			bPrizeMin = prizeMin > 0 ? normalizePrize(new BigDecimal(prizeMin), currencyLocaled) : new BigDecimal(0);
			bPrizeMax = prizeMin > prizeMax ? new BigDecimal(0) : new BigDecimal(prizeMax);
		} catch (Exception e) {
			logger.error("Error normalizing prizes");
			bPrizeMin = new BigDecimal(0);
			bPrizeMax = new BigDecimal(0);
		}

		if (dis <= 0)
			dis = Constant.DEFAULT_RADIUS_SEARCH;

		if (type.equalsIgnoreCase(Constant.CATEGORY)) {
			return itemRepository.findByParams(title, id, true, lat, lng, dis, bPrizeMin.doubleValue(),
					bPrizeMax.doubleValue(), pageRequest);
		} else {
			return itemRepository.findByParams(title, id, false, lat, lng, dis, bPrizeMin.doubleValue(),
					bPrizeMax.doubleValue(), pageRequest);
		}

	}

	@Deprecated
	@Override
	public Page<Item> getLocaledItemsByParams(Locale locale, String title, long id, String type, double dis,
			double lat, double lng, int prizeMin, int prizeMax, int page, int size) {
		// Protect page size
		if (size > Constant.MAX_PAGE_SIZE)
			size = Constant.MAX_PAGE_SIZE;

		// Page Request
		PageRequest pageRequest = new PageRequest(page, size);

		BigDecimal bPrizeMin = prizeMin > 0 ? new BigDecimal(prizeMin) : new BigDecimal(0);
		BigDecimal bPrizeMax = prizeMin > prizeMax ? new BigDecimal(0) : new BigDecimal(prizeMax);

		if (dis <= 0)
			dis = Constant.DEFAULT_RADIUS_SEARCH;

		// Get currency locale
		// try {
		// CurrencyLocaler localer = CurrencyLocaler.getInstance();
		// CurrencyConverter converter = CurrencyConverter.getInstance();
		// converter.getConvertRate(localer.getCurrencyLocaled(locale),
		// toCurrency)
		// } catch (IOException | ParseException e) {
		// logger.error("Error " + CurrencyConverter.class.getCanonicalName() +
		// " parsing file");
		// }

		Page<Item> items;
		if (type.equalsIgnoreCase(Constant.CATEGORY)) {
			items = itemRepository.findByParams(title, id, true, lat, lng, dis, bPrizeMin.doubleValue(),
					bPrizeMax.doubleValue(), pageRequest);
		} else {
			items = itemRepository.findByParams(title, id, false, lat, lng, dis, bPrizeMin.doubleValue(),
					bPrizeMax.doubleValue(), pageRequest);
		}

		// Convert every item for the current locale
		for (Item item : items) {
			logger.warn("Locale prized currency is " + item.getExchangeCurrencyPrize(locale));
		}

		return items;
	}

	public List<Item> getRandomFeaturedItems(int maxItems, String filter) {
		if (filter != null && !filter.equals(""))
			return itemRepository.findRandomFeaturedItemsByFilter(new PageRequest(0, maxItems), filter);
		else
			return itemRepository.findRandomFeaturedItems(new PageRequest(0, maxItems));
	}

	@Override
	public List<Item> getRandomFeaturedItems(int maxItems, long categoryId) {
		PageRequest request = new PageRequest(0, maxItems);
		if (categoryId == 0)
			return itemRepository.findRandomFeaturedItems(request);
		else
			return itemRepository.findRandomFeaturedItemsByCategoryId(categoryId, request);
	}

	@Override
	public Item updateItem(Long itemId, String title, String description, double prize, boolean renew,
			boolean featured, boolean highlight) {
		Item item = itemRepository.findOne(itemId);
		if (title != null)
			item.setTitle(title);
		if (description != null)
			item.setDescription(description);
		if (prize != -1)
			item.setPrize(new BigDecimal(prize));

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
		favoriteService.removeAllItemFavs(itemId);
		itemRepository.delete(itemId);
	}

	@Override
	public void deleteItem(User user, Long itemId) throws NotUserItemException {
		Item item = itemRepository.findOne(itemId);
		if (item != null && item.getUser().equals(user)) {
			favoriteService.removeAllItemFavs(item);
			itemReportService.deleteAllItemReports(itemId);
			itemRepository.delete(itemId);
		} else
			throw new NotUserItemException(itemId, user.getUserId());
	}

	@Override
	public void deleteAllUserItems(User user) {
		List<Item> items = itemRepository.findByUserId(user.getUserId());
		for (Item item : items) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1); // Set past date to ensure it will
												// be not shown in item list
			item.setEndDate(calendar);
			favoriteService.removeAllItemFavs(item);
		}
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

	@Override
	public Item renewItem(User user, Long itemId) throws ItemNotFoundException, NotUserItemException,
			RenewItemAfterEndDateException {
		Item item = getItemById(itemId);
		if (!item.getUser().equals(user)) {
			throw new NotUserItemException(itemId, user.getUserId());
		}

		Calendar cal = Calendar.getInstance();
		if (item.getEndDate().after(cal)) {
			throw new RenewItemAfterEndDateException("Manually renew of item " + itemId + " before expiring");
		}

		cal.add(Calendar.DATE, Constant.DEFAULT_RENEW_DAYS);
		item.setEndDate(cal);

		return itemRepository.save(item);
	}

	/********************************************************************************************/
	/**
	 * PRIVATE METHODS
	 */
	/********************************************************************************************/

	/**
	 * Returns randomized coordinates by the given ones.
	 * 
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

		Double[] coords = { latitude, longitude };

		return coords;
	}

	/**
	 * Gets @MultiPartFile data from the image list, saves it to a file and
	 * updates item information
	 * 
	 * @param item
	 * @param files
	 * @return
	 */
	private Item saveMultiPartFileImage(Item item, MultipartFile... files) {
		int count = 0;
		for (MultipartFile file : files) {
			if (file != null && !file.isEmpty()) {
				byte[] bytes = null;
				try {
					bytes = file.getBytes(); // Main image
				} catch (IOException e) {
					logger.debug("Error getting bytes from image");
				}
				switch (count) {
				case 0:
					item.setMainImage(saveImage(item, bytes));
					break;
				case 1:
					item.setImage1(saveImage(item, bytes));
					break;
				case 2:
					item.setImage2(saveImage(item, bytes));
					break;
				case 3:
					item.setImage3(saveImage(item, bytes));
					break;
				default:
					break;
				}
				count++;
			}
		}

		// Check if no image where provided, and set a default one
		if (count == 0)
			item.setMainImage(Constant.ITEM_IMG_DEFAULT);

		return item;
	}

	/**
	 * Uses item's user Id among the current timestamp to generate an unique
	 * String
	 * 
	 * @param item
	 * @return Generated String with user Id and timestamp
	 */
	private String itemHash(Item item) {
		return String.valueOf((item.getUser().getUserId() + Calendar.getInstance().getTimeInMillis()));
	}

	/**
	 * Saves an image to disk naming as unique filename
	 * 
	 * @param item
	 * @param bytes
	 * @return Image file path
	 */
	private String saveImage(Item item, byte[] bytes) {
		String imageFileName = itemHash(item);

		if (bytes != null) {
			try {
				/** SAVE IMAGE IN A RESOURCE FOLDER **/
				// Create folder for
				// /img/{userId}/{catId}/{subCatId}/{itemId}{1,2,3}.jpg
				File folderPath = new File(IOUtil.calculateFileName(item));
				folderPath.mkdirs();
				System.out.println("Creating folder " + folderPath.getAbsolutePath());

				// Get buffered image
				BufferedImage buffImg = ImageIO.read(new ByteArrayInputStream(bytes));
				// Resize if image it's too big
				if (buffImg.getHeight() > 1200 || buffImg.getWidth() > 1200) {
					buffImg = Scalr.resize(buffImg, 1200);
				}
				// Create file
				File imgFile = new File(folderPath.getAbsolutePath() + "/" + imageFileName + ".jpg");
				// Write image in file
				ImageIO.write(buffImg, "jpg", imgFile);

				/* IMAGE RESIZED */
				BufferedImage resizedImage = buffImg;
				// Do not resize if image it's small
				if (!(resizedImage.getHeight() < 200 || resizedImage.getWidth() < 200)) {
					resizedImage = Scalr.resize(buffImg, 320);
				}
				// Create file
				File imgResizedFile = new File(folderPath.getAbsolutePath() + "/" + imageFileName + "-200h.jpg");
				// Write image in file
				ImageIO.write(resizedImage, "jpg", imgResizedFile);

				// item.setImgHome(IOUtil.calculateFileName(item) +"/"+
				// imageFileName + ".jpg");
				// item.setMainImage(IOUtil.calculateFileName(item) +"/"+
				// imageFileName);

				// logger.debug("Saving image with path " +
				// IOUtil.calculateFileName(item) + "/" + imageFileName);
				String absolutePath = IOUtil.calculateFileName(item) + "/" + imageFileName;

				// Remember to call Image.flush() on the src to free up native
				// resources
				// and make it easier for the GC to collect the unused image.
				resizedImage.flush();

				return absolutePath;

			} catch (NullPointerException | IOException | IllegalArgumentException e) {
				logger.error("Error writing image '" + imageFileName + "' !");
			}
		}

		return null;
	}

	private BigDecimal normalizePrize(BigDecimal prize, String currency) throws FileNotFoundException, IOException,
			ParseException {
		return normalizePrize(prize, Currency.getInstance(currency));
	}

	private BigDecimal normalizePrize(BigDecimal prize, Currency currency) throws FileNotFoundException, IOException,
			ParseException {
		CurrencyConverter converter = CurrencyConverter.getInstance(CurrencyConverter.MAIN_CONVERT_FILE);
		float convertRate = converter.getConvertRate(currency, CurrencyConverter.USD);
		float floatPrize = prize.floatValue();
		float normalizedPrize = floatPrize / convertRate;
		BigDecimal fixedPrize = new BigDecimal(normalizedPrize);
		fixedPrize = fixedPrize.setScale(6, BigDecimal.ROUND_HALF_UP);

		return fixedPrize;
	}

}
