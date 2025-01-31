package es.elovendo.rest.web;

import static es.elovendo.util.Constant.S_ITEMS_PER_PAGE;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import es.elovendo.model.item.Item;
import es.elovendo.model.item.ItemForm;
import es.elovendo.model.item.category.Category;
import es.elovendo.model.item.category.subcategory.SubCategory;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.CategoryNotFoundException;
import es.elovendo.rest.exception.InsufficientPointsException;
import es.elovendo.rest.exception.ItemNotFoundException;
import es.elovendo.rest.exception.NotUserItemException;
import es.elovendo.rest.exception.RenewItemAfterEndDateException;
import es.elovendo.rest.exception.SubCategoryNotFoundException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.service.exception.InvalidItemNameMinLenghtException;
import es.elovendo.service.item.ItemService;
import es.elovendo.service.item.category.CategoryService;
import es.elovendo.service.item.favorite.FavoriteService;
import es.elovendo.service.user.UserService;
import es.elovendo.service.vote.VoteService;
import es.elovendo.util.Constant;
import es.elovendo.util.PageWrapper;
import es.elovendo.util.sessionHelper.SessionUserObtainer;
import es.elovendo.util.sessionHelper.exception.AnonymousUserAuthenticationException;

@Controller
@RequestMapping(value = "/bazaar/")
public class ItemWebController {

	Logger logger = Logger.getLogger(ItemWebController.class);

	@Autowired
	private ItemService itemService;
	@Autowired
	private VoteService voteService;
	@Autowired
	private UserService userService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private FavoriteService favoriteService;
	@Autowired
	private MessageSource messageSource;

	/**
	 * FIND BY TITLE
	 * 
	 * @throws CategoryNotFoundException
	 */
	@RequestMapping(value = "search", method = RequestMethod.GET)
	public String itemListByTitleSearchPage(Model model, Locale locale,
			@RequestParam(value = "category", required = false, defaultValue = "0") long categoryId,
			@RequestParam("title") String title,
			@RequestParam(value = "dis", required = false, defaultValue = "0") double dis,
			@RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
			@RequestParam(value = "lng", required = false, defaultValue = "0") double lng,
			@RequestParam(value = "min", required = false, defaultValue = "0") int prizeMin,
			@RequestParam(value = "max", required = false, defaultValue = "0") int prizeMax,
			@RequestParam(value = "p", required = false, defaultValue = "0") int page,
			@RequestParam(value = "s", required = false, defaultValue = S_ITEMS_PER_PAGE) int size)
			throws CategoryNotFoundException {

		model.addAttribute("featuredItems", itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, categoryId));

		String urlLocation = "search";
		model.addAttribute("url", urlLocation);

		@SuppressWarnings("unchecked")
		List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
		model.addAttribute("categories", categories);

		model.addAttribute("categoryId", categoryId);

		// Category category =
		// categoryService.getCategoryByCategoryId(categoryId);
		Page<Item> p = itemService.getItemsByParams(title, categoryId, Constant.CATEGORY, dis, lat, lng, prizeMin,
				prizeMax, locale, page, size);

		// Quick workaround for manage pagination with searches
		PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, fixSearchPaginationUrl(categoryId, title, dis, lat,
				lng, prizeMin, prizeMax));

		List<Item> items = p.getContent();

		model.addAttribute("hasLatLng", lat != 0 && lng != 0); // For show a
																// message if
																// there is no
																// GPS info
		model.addAttribute("page", pageWrapper);
		model.addAttribute("itemsList", items);

		return "elovendo/item/list/item_list";
	}

	/**
	 * FIND ALL ITEMS
	 */

	@RequestMapping(value = "category/all", method = RequestMethod.GET)
	public String allItemList(Model model, Locale locale,
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "dis", required = false, defaultValue = "0") double dis,
			@RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
			@RequestParam(value = "lng", required = false, defaultValue = "0") double lng,
			@RequestParam(value = "min", required = false, defaultValue = "0") int prizeMin,
			@RequestParam(value = "max", required = false, defaultValue = "0") int prizeMax,
			@RequestParam(value = "p", required = false, defaultValue = "0") int page,
			@RequestParam(value = "s", required = false, defaultValue = S_ITEMS_PER_PAGE) int size)
			throws CategoryNotFoundException {

		model.addAttribute("hasLatLng", lat != 0 && lng != 0); // For show a
																// message if
																// there is no
																// GPS info

		model.addAttribute("featuredItems", itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, null));

		String urlLocation = "category/all";
		model.addAttribute("url", urlLocation);

		model.addAttribute("categoryId", 0);
		model.addAttribute("subCategoryId", 0);

		@SuppressWarnings("unchecked")
		List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
		model.addAttribute("categories", categories);

		if (prizeMin != 0)
			model.addAttribute("prizeMin", prizeMin);
		if (prizeMax != 0)
			model.addAttribute("prizeMax", prizeMax);

		Page<Item> p = itemService.getItemsByParams(title, Constant.ALL_PATH, dis, lat, lng, prizeMin, prizeMax, locale, page,
				size);

		String fixedUrl = fixPaginationUrl(Constant.ALL_PATH, title, dis, lat, lng, prizeMin, prizeMax);

		PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, fixedUrl);
		List<Item> items = p.getContent();

		model.addAttribute("page", pageWrapper);
		model.addAttribute("itemsList", items);

		return "elovendo/item/list/item_list";
	}

	/**
	 * FIND BY CATEGORY
	 * 
	 * @throws CategoryNotFoundException
	 */

	@RequestMapping(value = "category/{categoryId}", method = RequestMethod.GET)
	public String itemListByCategoryPage(Model model, Locale locale, @PathVariable("categoryId") long categoryId,
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "dis", required = false, defaultValue = "0") double dis,
			@RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
			@RequestParam(value = "lng", required = false, defaultValue = "0") double lng,
			@RequestParam(value = "min", required = false, defaultValue = "0") int prizeMin,
			@RequestParam(value = "max", required = false, defaultValue = "0") int prizeMax,
			@RequestParam(value = "p", required = false, defaultValue = "0") int page,
			@RequestParam(value = "s", required = false, defaultValue = S_ITEMS_PER_PAGE) int size)
			throws CategoryNotFoundException {

		model.addAttribute("hasLatLng", lat != 0 && lng != 0); // For show a
																// message if
																// there is no
																// GPS info

		model.addAttribute("featuredItems", itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, categoryId));

		String urlLocation = "category/" + categoryId;
		model.addAttribute("url", urlLocation);

		Category category = categoryService.getCategoryByCategoryId(categoryId);
		// model.addAttribute("category", category);
		model.addAttribute("categoryId", category.getCategoryId());
		model.addAttribute("subCategoryId", 0);

		@SuppressWarnings("unchecked")
		List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
		model.addAttribute("categories", categories);

		if (prizeMin != 0)
			model.addAttribute("prizeMin", prizeMin);
		if (prizeMax != 0)
			model.addAttribute("prizeMax", prizeMax);

		// Page<Item> p = itemService.getItemsByParams(title, categoryId,
		// Constant.CATEGORY, dis, lat, lng, prizeMin,
		// prizeMax, page, size);
		Page<Item> p = itemService.getLocaledItemsByParams(locale, title, categoryId, Constant.CATEGORY, dis, lat, lng,
				prizeMin, prizeMax, page, size);

		String fixedUrl = fixPaginationUrl(String.valueOf(categoryId), title, dis, lat, lng, prizeMin, prizeMax);

		PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, fixedUrl);
		List<Item> items = p.getContent();

		model.addAttribute("page", pageWrapper);
		model.addAttribute("itemsList", items);

		return "elovendo/item/list/item_list";
	}

	/**
	 * FIND BY SUBCATEGORY
	 * 
	 * @throws SubCategoryNotFoundException
	 * @throws CategoryNotFoundException
	 */

	@RequestMapping(value = "sub/{subcategoryId}", method = RequestMethod.GET)
	public String itemListPageBySubcategoryId(Model model, Locale locale, HttpServletRequest request,
			@PathVariable("subcategoryId") long subCategoryId,
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "dis", required = false, defaultValue = "0") double dis,
			@RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
			@RequestParam(value = "lng", required = false, defaultValue = "0") double lng,
			@RequestParam(value = "min", required = false, defaultValue = "0") int prizeMin,
			@RequestParam(value = "max", required = false, defaultValue = "0") int prizeMax,
			@RequestParam(value = "p", required = false, defaultValue = "0") int page,
			@RequestParam(value = "s", required = false, defaultValue = S_ITEMS_PER_PAGE) int size)
			throws CategoryNotFoundException, SubCategoryNotFoundException {

		model.addAttribute("hasLatLng", lat != 0 && lng != 0); // For show a
																// message if
																// there is no
																// GPS info

		model.addAttribute("featuredItems",
				itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, subCategoryId));

		String urlLocation = "sub/" + subCategoryId;
		model.addAttribute("url", urlLocation);
		Category category = categoryService.getCategoryBySubCategoryId(subCategoryId);
		// model.addAttribute("category", category);
		model.addAttribute("categoryId", category.getCategoryId());
		SubCategory subCategory = categoryService.getSubCategoryBySubCategoryId(subCategoryId);
		model.addAttribute("subCategoryId", subCategory.getId());

		Page<Item> p = itemService.getItemsByParams(title, subCategoryId, Constant.SUBCATEGORY, dis, lat, lng,
				prizeMin, prizeMax, locale, page, size);
		PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, fixPaginationUrl(String.valueOf(subCategoryId), title,
				dis, lat, lng, prizeMin, prizeMax));
		List<Item> items = p.getContent();

		@SuppressWarnings("unchecked")
		List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
		model.addAttribute("categories", categories);
		@SuppressWarnings("unchecked")
		List<SubCategory> subCategories = IteratorUtils.toList(categoryService.getAllSubCategoriesFromSubCategoryId(
				subCategoryId).iterator());
		model.addAttribute("subCategories", subCategories);

		model.addAttribute("page", pageWrapper);
		model.addAttribute("itemsList", items);

		return "elovendo/item/list/item_list";
	}

	@RequestMapping(value = Constant.ALL_PATH, method = RequestMethod.GET)
	public String allItemListPage(Model model, Locale locale,
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "dis", required = false, defaultValue = "0") double dis,
			@RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
			@RequestParam(value = "lng", required = false, defaultValue = "0") double lng,
			@RequestParam(value = "min", required = false, defaultValue = "0") int prizeMin,
			@RequestParam(value = "max", required = false, defaultValue = "0") int prizeMax,
			@RequestParam(value = "p", required = false, defaultValue = "0") int page,
			@RequestParam(value = "s", required = false, defaultValue = S_ITEMS_PER_PAGE) int size) {

		model.addAttribute("hasLatLng", lat != 0 && lng != 0); // For show a
																// message if
																// there is no
																// GPS info

		model.addAttribute("featuredItems", itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, null));

		Page<Item> p = itemService.getItemsByParams(title, null, dis, lat, lng, prizeMin, prizeMax, locale, page, size);

		PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, fixPaginationUrl(Constant.ALL_PATH, title, dis, lat,
				lng, prizeMin, prizeMax));

		List<Item> items = p.getContent();

		@SuppressWarnings("unchecked")
		List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
		model.addAttribute("categories", categories);

		model.addAttribute("page", pageWrapper);
		model.addAttribute("itemsList", items);

		return "elovendo/item/list/item_list";
	}

	/**
	 * FIND RANDOM
	 */
	@SuppressWarnings("unused")
	private List<Item> getRandomItems(int maxItems, String subCategory) {
		return itemService.getRandomFeaturedItems(maxItems, subCategory);
	}

	/**
	 * VIEW ITEM
	 */
	@RequestMapping(value = "item/{itemId}", method = RequestMethod.GET)
	public String itemViewPage(Model model, @PathVariable("itemId") long itemId) {

		Item item;
		try {
			item = itemService.getItemById(itemId);
		} catch (ItemNotFoundException e) {
			return "elovendo/error/error";
		}

		model.addAttribute("images", item.getAllImages200h()); // Add other
																// images (other
																// ones than
																// main image)
		model.addAttribute("item", item);
		model.addAttribute("votesPositive", voteService.getNumberVotesPositive(item.getUser().getUserId()));
		model.addAttribute("votesNegative", voteService.getNumberVotesNegative(item.getUser().getUserId()));
		model.addAttribute("totalItems", itemService.getNumberUserItems(item.getUser().getUserId()));

		return "elovendo/item/itemView";
	}

	/** STUFF **/

	private String fixPaginationUrl(String baseUrl, String title, double dis, double lat, double lng, int prizeMin,
			int prizeMax) {

		String tmp = baseUrl + "?lat=" + lat + "&lng=" + lng;

		if (!title.equals(""))
			tmp = tmp.concat("&title=" + title);
		if (dis > 0)
			tmp = tmp.concat("&dis=" + dis);
		if (prizeMin > 0)
			tmp = tmp.concat("&min=" + prizeMin);
		if (prizeMax > 0)
			tmp = tmp.concat("&max=" + prizeMax);

		return tmp;
	}

	private String fixSearchPaginationUrl(long categoryId, String title, double dis, double lat, double lng,
			int prizeMin, int prizeMax) {

		String tmp = "search?category=" + categoryId + "&lat=" + lat + "&lng=" + lng;

		if (!title.equals(""))
			tmp = tmp.concat("&title=" + title);
		if (dis > 0)
			tmp = tmp.concat("&dis=" + dis);
		if (prizeMin > 0)
			tmp = tmp.concat("&min=" + prizeMin);
		if (prizeMax > 0)
			tmp = tmp.concat("&max=" + prizeMax);

		return tmp;
	}

	/**
	 * ADD ITEMS
	 * 
	 * @throws UserNotFoundException
	 * @throws AnonymousUserAuthenticationException
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add/item", method = RequestMethod.GET)
	public String addItemPage(Model model) throws UserNotFoundException, AnonymousUserAuthenticationException {

		model.addAttribute("item", new ItemForm());

		User user = SessionUserObtainer.getInstance().getSessionUser();

		model.addAttribute("user", user);

		List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
		model.addAttribute("categories", categories);

		return "elovendo/item/add_item";
	}

	@RequestMapping(value = "add/item", method = RequestMethod.POST)
	public String processAddItemWeb(@Valid @ModelAttribute(value = "item") ItemForm itemForm, BindingResult result,
			@RequestParam(value = "mI", required = false) MultipartFile mainImage,
			@RequestParam(value = "i1", required = false) MultipartFile image1,
			@RequestParam(value = "i2", required = false) MultipartFile image2,
			@RequestParam(value = "i3", required = false) MultipartFile image3, Model model, Locale locale)
			throws InvalidItemNameMinLenghtException, UserNotFoundException, SubCategoryNotFoundException, IOException,
			InsufficientPointsException, ParseException {

		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// Validate category and subCategory
		// if (itemForm.getCategory() == 0) {
		// result.addError(new FieldError("item", "category",
		// messageSource.getMessage("Item.add.Error.category", null, locale)));
		// }

		// if (itemForm.getSubCategory() == 0) {
		// result.addError(new FieldError("item", "subCategory",
		// messageSource.getMessage("Item.add.Error.subCategory", null,
		// locale)));
		// }

		if (result.hasErrors()) {

			@SuppressWarnings("unchecked")
			List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
			model.addAttribute("categories", categories);

			// model.addAttribute("categoryId", itemForm.getCategory());

			model.addAttribute("user", user);

			return "elovendo/item/add_item";

		} else {

			try {
				itemService.addItem(itemForm, user, mainImage, image1, image2, image3);
			} catch (InsufficientPointsException e) {
				@SuppressWarnings("unchecked")
				List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
				model.addAttribute("categories", categories);
				model.addAttribute("user", user);
				model.addAttribute("insPoints", true);
				return "elovendo/item/add_item";
			}

			return "elovendo/item/item_create_successful";
		}
	}

	/**
	 * EDIT ITEM
	 * 
	 * @throws ItemNotFoundException
	 * @throws NotUserItemException
	 * @throws AnonymousUserAuthenticationException
	 **/

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "edit/item/{itemId}", method = RequestMethod.GET)
	public String editItemPage(Model model, @PathVariable long itemId) throws ItemNotFoundException,
			NotUserItemException, AnonymousUserAuthenticationException {

		User user = SessionUserObtainer.getInstance().getSessionUser();

		Item item = itemService.getItemById(itemId);
		ItemForm itemForm = new ItemForm(item);

		if (!item.getUser().equals(user))
			throw new NotUserItemException(itemId, user.getUserId());

		model.addAttribute("user", user);
		model.addAttribute("item", itemForm);

		List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
		model.addAttribute("categories", categories);
		List<SubCategory> subCategories = IteratorUtils.toList(categoryService.getAllSubCatByCategoryId(
				itemForm.getCategory()).iterator());
		model.addAttribute("subCategories", subCategories);

		return "elovendo/item/edit/edit_item";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "edit/item/{itemId}", method = RequestMethod.POST)
	public String processEditItemPage(Model model, @PathVariable long itemId,
			@Valid @ModelAttribute(value = "item") ItemForm itemForm, BindingResult result,
			@RequestParam("mI") MultipartFile mainImage, @RequestParam("i1") MultipartFile image1,
			@RequestParam("i2") MultipartFile image2, @RequestParam("i3") MultipartFile image3)
			throws ItemNotFoundException, NotUserItemException, SubCategoryNotFoundException,
			AnonymousUserAuthenticationException {

		User user = SessionUserObtainer.getInstance().getSessionUser();

		if (result.hasErrors()) {
			for (FieldError fe : result.getFieldErrors()) {
				logger.debug("Edit " + itemId + " error: " + fe.toString());
			}

			model.addAttribute("user", user);
			model.addAttribute("item", itemForm);

			List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
			model.addAttribute("categories", categories);
			List<SubCategory> subCategories = IteratorUtils.toList(categoryService.getAllSubCatByCategoryId(
					itemForm.getCategory()).iterator());
			model.addAttribute("subCategories", subCategories);

			return "elovendo/item/edit/edit_item";
		} else {
			logger.debug("Item " + itemId + "succesfully edited");
			itemForm.setUser(user);
			itemForm.setItemId(itemId);

			try {
				itemService.updateItem(itemForm, user, mainImage, image1, image2, image3);
			} catch (InsufficientPointsException e) {
				List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
				model.addAttribute("categories", categories);
				List<SubCategory> subCategories = IteratorUtils.toList(categoryService.getAllSubCatByCategoryId(
						itemForm.getCategory()).iterator());
				model.addAttribute("subCategories", subCategories);

				model.addAttribute("user", user);
				model.addAttribute("item", itemForm);
				model.addAttribute("insPoints", true);
				return "elovendo/item/add_item";
			}

			return "elovendo/item/item_create_successful";
		}
	}

	@RequestMapping(value = "item/delete", method = RequestMethod.POST)
	public @ResponseBody int deleteItem(@RequestParam(value = "id", required = true, defaultValue = "0") long itemId)
			throws NotUserItemException, AnonymousUserAuthenticationException {
		User user = SessionUserObtainer.getInstance().getSessionUser();
		itemService.deleteItem(user, itemId);
		return (int) itemId;
	}

	@RequestMapping(value = "item/renew", method = RequestMethod.POST)
	public @ResponseBody int renewItem(@RequestParam(value = "id", required = true, defaultValue = "0") long itemId)
			throws AnonymousUserAuthenticationException, ItemNotFoundException, NotUserItemException {
		User user = SessionUserObtainer.getInstance().getSessionUser();
		try {
			itemService.renewItem(user, itemId);
		} catch (RenewItemAfterEndDateException e) {
			return 0;
		}
		return (int) 1;
	}

	/**
	 * SET ITEM FAVORITE
	 * 
	 * @throws ItemNotFoundException
	 * @throws AnonymousUserAuthenticationException
	 */

	@RequestMapping(value = "item/fav", method = RequestMethod.POST)
	public @ResponseBody boolean setItemFavorite(
			@RequestParam(value = "id", required = true, defaultValue = "") long itemId) throws ItemNotFoundException,
			AnonymousUserAuthenticationException {

		User user = null;
		try {
			user = SessionUserObtainer.getInstance().getSessionUser();
		} catch (AnonymousUserAuthenticationException e) {
			return false;
		}

		return favoriteService.setFavorite(user, itemId) != null;
	}

	@RequestMapping(value = "item/unfav", method = RequestMethod.POST)
	public @ResponseBody boolean unsetItemFavorite(
			@RequestParam(value = "id", required = true, defaultValue = "") long itemId) throws ItemNotFoundException,
			AnonymousUserAuthenticationException {

		User user = SessionUserObtainer.getInstance().getSessionUser();

		if (user == null)
			return false;
		favoriteService.unsetFavorite(user, itemId);
		return true;
	}

}
