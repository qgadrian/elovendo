package es.telocompro.rest.web;

import static es.telocompro.util.Constant.S_ITEMS_PER_PAGE;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
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

import es.telocompro.model.item.Item;
import es.telocompro.model.item.ItemForm;
import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.user.User;
import es.telocompro.rest.exception.InsufficientPointsException;
import es.telocompro.rest.exception.ItemNotFoundException;
import es.telocompro.rest.exception.NotUserItemException;
import es.telocompro.rest.exception.SubCategoryNotFoundException;
import es.telocompro.rest.exception.UserNotFoundException;
import es.telocompro.service.exception.InvalidItemNameMinLenghtException;
import es.telocompro.service.item.ItemService;
import es.telocompro.service.item.category.CategoryService;
import es.telocompro.service.item.favorite.FavoriteService;
import es.telocompro.service.user.UserService;
import es.telocompro.service.vote.VoteService;
import es.telocompro.util.Constant;
import es.telocompro.util.PageWrapper;

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
	 */
	@RequestMapping(value = "search", method = RequestMethod.GET)
	public String itemListByTitleSearchPage(Model model,
			@RequestParam(value = "subcategory", required = false, defaultValue = "") String subCategory,
			@RequestParam("title") String title,
			@RequestParam(value = "dis", required = false, defaultValue = "0") double dis,
			@RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
			@RequestParam(value = "lng", required = false, defaultValue = "0") double lng,
			@RequestParam(value = "min", required = false, defaultValue = "0") int prizeMin,
			@RequestParam(value = "max", required = false, defaultValue = "0") int prizeMax,
			@RequestParam(value = "p", required = false, defaultValue = "0") int page,
			@RequestParam(value = "s", required = false, defaultValue = S_ITEMS_PER_PAGE) int size) {

		model.addAttribute("featuredItems", itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, subCategory));

		@SuppressWarnings("unchecked")
		List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
		model.addAttribute("categories", categories);

		// FIXME: Broken search
		Page<Item> p = itemService.getItemsByParams(title, subCategory, dis, lat, lng, prizeMin, prizeMax, page, size);

		// Quick workaround for manage pagination with searches
		PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, fixPaginationUrl(subCategory, title, dis, lat, lng,
				prizeMin, prizeMax));

		List<Item> items = p.getContent();

		model.addAttribute("page", pageWrapper);
		model.addAttribute("itemsList", items);

		return "elovendo/item/list/item_list";
	}

	/**
	 * FIND BY CATEGORY
	 */

	@RequestMapping(value = "category/{categoryName}", method = RequestMethod.GET)
	public String itemListByCategoryPage(Model model, @PathVariable("categoryName") String categoryName,
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "dis", required = false, defaultValue = "0") double dis,
			@RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
			@RequestParam(value = "lng", required = false, defaultValue = "0") double lng,
			@RequestParam(value = "min", required = false, defaultValue = "0") int prizeMin,
			@RequestParam(value = "max", required = false, defaultValue = "0") int prizeMax,
			@RequestParam(value = "p", required = false, defaultValue = "0") int page,
			@RequestParam(value = "s", required = false, defaultValue = S_ITEMS_PER_PAGE) int size) {

		model.addAttribute("featuredItems", itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, categoryName));

		String urlLocation = "category/" + categoryName;
		model.addAttribute("url", urlLocation);

		@SuppressWarnings("unchecked")
		List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
		model.addAttribute("categories", categories);

		if (prizeMin != 0)
			model.addAttribute("prizeMin", prizeMin);
		if (prizeMax != 0)
			model.addAttribute("prizeMax", prizeMax);

		Page<Item> p = itemService.getItemsByParams(title, categoryName, dis, lat, lng, prizeMin, prizeMax, page, size);

		String fixedUrl = fixPaginationUrl(categoryName, title, dis, lat, lng, prizeMin, prizeMax);

		PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, fixedUrl);
		List<Item> items = p.getContent();

		model.addAttribute("page", pageWrapper);
		model.addAttribute("itemsList", items);

		return "elovendo/item/list/item_list";
	}

	/**
	 * FIND BY SUBCATEGORY
	 */

	@RequestMapping(value = "sub/{subcategoryname}", method = RequestMethod.GET)
	public String itemListPage(Model model, @PathVariable("subcategoryname") String subCategoryName,
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "dis", required = false, defaultValue = "0") double dis,
			@RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
			@RequestParam(value = "lng", required = false, defaultValue = "0") double lng,
			@RequestParam(value = "min", required = false, defaultValue = "0") int prizeMin,
			@RequestParam(value = "max", required = false, defaultValue = "0") int prizeMax,
			@RequestParam(value = "p", required = false, defaultValue = "0") int page,
			@RequestParam(value = "s", required = false, defaultValue = S_ITEMS_PER_PAGE) int size) {

		model.addAttribute("featuredItems",
				itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, subCategoryName));

		Page<Item> p = itemService.getItemsByParams(title, subCategoryName, dis, lat, lng, prizeMin, prizeMax, page,
				size);
		PageWrapper<Item> pageWrapper = new PageWrapper<Item>(p, fixPaginationUrl(subCategoryName, title, dis, lat,
				lng, prizeMin, prizeMax));
		List<Item> items = p.getContent();

		@SuppressWarnings("unchecked")
		List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
		model.addAttribute("categories", categories);

		model.addAttribute("page", pageWrapper);
		model.addAttribute("itemsList", items);

		return "elovendo/item/list/item_list";
	}

	@RequestMapping(value = Constant.ALL_PATH, method = RequestMethod.GET)
	public String allItemListPage(Model model,
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "dis", required = false, defaultValue = "0") double dis,
			@RequestParam(value = "lat", required = false, defaultValue = "0") double lat,
			@RequestParam(value = "lng", required = false, defaultValue = "0") double lng,
			@RequestParam(value = "min", required = false, defaultValue = "0") int prizeMin,
			@RequestParam(value = "max", required = false, defaultValue = "0") int prizeMax,
			@RequestParam(value = "p", required = false, defaultValue = "0") int page,
			@RequestParam(value = "s", required = false, defaultValue = S_ITEMS_PER_PAGE) int size) {

		model.addAttribute("featuredItems", itemService.getRandomFeaturedItems(Constant.MAX_RANDOM_ITEMS, null));

		Page<Item> p = itemService.getItemsByParams(title, null, dis, lat, lng, prizeMin, prizeMax, page, size);

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

		model.addAttribute("item", item);
		model.addAttribute("votesPositive", voteService.getNumberVotesPositive(item.getUser().getUserId()));
		model.addAttribute("votesNegative", voteService.getNumberVotesNegative(item.getUser().getUserId()));
		model.addAttribute("totalItems", itemService.getNumberUserItems(item.getUser().getUserId()));

		return "elovendo/item/itemView";
	}

	/** STUFF **/

	private String fixPaginationUrl(String categoryName, String title, double dis, double lat, double lng,
			int prizeMin, int prizeMax) {

		String tmp = categoryName + "?lat=" + lat + "&lng=" + lng;

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
	 * @throws UserNotFoundException 
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add/item", method = RequestMethod.GET)
	public String addItemPage(Model model) throws UserNotFoundException {

		model.addAttribute("item", new ItemForm());

		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		model.addAttribute("user", user);

		List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
		model.addAttribute("categories", categories);

		return "elovendo/item/add_item";
	}

	@RequestMapping(value = "add/item", method = RequestMethod.POST)
	public String processAddItemWeb(
			@Valid @ModelAttribute(value = "item") ItemForm itemForm, BindingResult result,
			@RequestParam("mI") MultipartFile mainImage,
			@RequestParam("i1") MultipartFile image1, 
			@RequestParam("i2") MultipartFile image2,
			@RequestParam("i3") MultipartFile image3, 
			Model model, Locale locale) throws InvalidItemNameMinLenghtException,
			UserNotFoundException, SubCategoryNotFoundException, IOException, InsufficientPointsException {

		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (result.hasErrors()) {

			@SuppressWarnings("unchecked")
			List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
			model.addAttribute("categories", categories);
			
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
	 **/

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "edit/item/{itemId}", method = RequestMethod.GET)
	public String editItemPage(Model model, @PathVariable long itemId) throws ItemNotFoundException,
			NotUserItemException {

		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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
	public String processEditItemPage(Model model, 
			@PathVariable long itemId,
			@Valid @ModelAttribute(value = "item") ItemForm itemForm, 
			BindingResult result, 
			@RequestParam("mI") MultipartFile mainImage,
			@RequestParam("i1") MultipartFile image1, 
			@RequestParam("i2") MultipartFile image2,
			@RequestParam("i3") MultipartFile image3) throws ItemNotFoundException, NotUserItemException,
			SubCategoryNotFoundException {

		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			model.addAttribute("item", itemForm);

			List<Category> categories = IteratorUtils.toList(categoryService.getAllCategories().iterator());
			model.addAttribute("categories", categories);
			List<SubCategory> subCategories = IteratorUtils.toList(categoryService.getAllSubCatByCategoryId(
					itemForm.getCategory()).iterator());
			model.addAttribute("subCategories", subCategories);

			return "elovendo/item/edit/edit_item";
		}
		else { 
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

	/** DELETE ITEM */

	@RequestMapping(value = "delete/item", method = RequestMethod.POST)
	public @ResponseBody int deleteItem(@RequestParam(value = "id", required = true, defaultValue = "0") long itemId) {
		itemService.deleteItem(itemId);
		return (int) itemId;
	}

	/**
	 * SET ITEM FAVORITE
	 * 
	 * @throws ItemNotFoundException
	 */

	@RequestMapping(value = "item/fav", method = RequestMethod.POST)
	public @ResponseBody boolean toggleItemFavorite(
			@RequestParam(value = "id", required = true, defaultValue = "") long itemId) throws ItemNotFoundException {
		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (user == null) return false;

		return favoriteService.setFavorite(user, itemId) != null;
	}

}
