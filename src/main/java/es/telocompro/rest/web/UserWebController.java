package es.telocompro.rest.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.IteratorUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.elasticsearch.common.Strings;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.user.User;
import es.telocompro.rest.controller.exception.InsufficientPointsException;
import es.telocompro.rest.controller.exception.LoginNotAvailableException;
import es.telocompro.rest.controller.exception.ProvinceNotFoundException;
import es.telocompro.rest.controller.exception.PurchaseDuplicateException;
import es.telocompro.rest.controller.exception.SubCategoryNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;
import es.telocompro.service.exception.InvalidItemNameMinLenghtException;
import es.telocompro.service.item.ItemService;
import es.telocompro.service.item.category.CategoryService;
import es.telocompro.service.purchase.PurchaseService;
import es.telocompro.service.user.UserService;
import es.telocompro.util.Constant;

@Controller
@SuppressWarnings("unused")
@RequestMapping("/site/")
public class UserWebController {

	Logger logger = Logger.getLogger(UserWebController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private PurchaseService purchaseService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private MessageSource messageSource;

	/**
	 * USER STUFF
	 */
	
	/** View profile */
	@RequestMapping(value = "profile", method = RequestMethod.GET)
	public String getProfile(Model model) {
		
		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
		model.addAttribute("user", user);
		
		model.addAttribute("itemList", itemService.getAllItemsByUser(user));
		
		model.addAttribute("totalItems", itemService.getNumberUserItems(user));
		
		int votesPositive = userService.getVotesPositive(user);
		int votesNegative = userService.getVotesNegative(user);
		int totalVotes = votesPositive + votesNegative;
		model.addAttribute("votesPositive", votesPositive);
		model.addAttribute("votesNegative", votesNegative);
		model.addAttribute("votesQueued", userService.getVotesQueued(user));
		model.addAttribute("totalVotes", totalVotes);
		
		return "elovendo/user/profile";
	}
	
	/** View profile 
	 * @throws UserNotFoundException */
	@RequestMapping(value = "public/{userName}", method = RequestMethod.GET)
	public String getProfile(@PathVariable(value="userName") String userName,
			Model model) throws UserNotFoundException {
		
		User user = userService.findUserByLogin(userName);
		model.addAttribute("user", user);
		
		model.addAttribute("totalItems", itemService.getNumberUserItems(user));
		
		int votesPositive = userService.getVotesPositive(user);
		int votesNegative = userService.getVotesNegative(user);
		int totalVotes = votesPositive + votesNegative;
		model.addAttribute("votesPositive", votesPositive);
		model.addAttribute("votesNegative", votesNegative);
		model.addAttribute("totalVotes", totalVotes);
		
		return "elovendo/user/publicProfile";
	}

	/** Add a new user **/

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String addUserPage(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("provinceName", new String());

		return "elovendo/user/add_user";
	}

	@RequestMapping(value = "quick_register", method = RequestMethod.GET)
	public String quickAddUserPage(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("provinceName", new String());

		return "elovendo/user/quick_add_user";
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String processAddUserWeb(
			@Valid @ModelAttribute(value = "user") User user,
			BindingResult result,
			@ModelAttribute(value = "profilePic") MultipartFile profilePic,
			@ModelAttribute(value = "confirmPassword") String confirmPassword,
			ModelMap model, Locale locale) throws LoginNotAvailableException {
		// FIXME: Edit input type email
		// TODO: Get confirmPassword to validate

		// System.gc();
		//
		// Pattern loginMatcherPattern = Pattern.compile(Constant.loginPattern);
		// Pattern passwordMatcherPattern =
		// Pattern.compile(Constant.passwordPattern);
		// Matcher loginMatcher = loginMatcherPattern.matcher(user.getLogin());
		// Matcher passwordMatcher =
		// passwordMatcherPattern.matcher(user.getPassword());
		//
		// if (loginMatcher.find() && passwordMatcher.find()) {
		// System.out.println("Fields ok");
		//
		// byte[] profilePicBytes = null;
		// if (!profilePic.isEmpty()) try {
		// profilePicBytes = profilePic.getBytes();
		// } catch (IOException e) {
		// System.out.println("Error converting to bytes image file");
		// }
		//
		// userService.addUser(user, province, profilePicBytes);
		// } else {
		// System.out.println("Bad login or password format");
		// return "redirect:/error";
		// }
		//
		// return "elovendo/user/registered_successful";

		// FormValidator formValidator = new FormValidator();
		// formValidator.validate(user, result);

		// result.addError(new FieldError("registrationform", "username",
		// "rejected stuff"));

		// return "elovendo/user/add_user";

		// Check confirm password matches
		if (!user.getPassword().equals(confirmPassword)) {
			logger.error("passwords doesnt match!");
			result.addError(new FieldError("registrationform", "password",
					messageSource.getMessage("Error.password.missmatch", null,
							locale)));
		}

		// check image is jpeg

		// TODO: image/png, image/gif
		// FIXME: Convert objefct to FormUser and make there validations
		if (!profilePic.getContentType().equals("image/jpeg")
				&& !profilePic.getContentType().equals("image/pjpeg")) {
			logger.error("not jpg!");
			logger.error("multipart file is :" + profilePic.getContentType());
			result.addError(new FieldError("registrationform", "profilePic",
					messageSource.getMessage("Error.password.missmatch", null,
							locale)));
		}
		

		if (result.hasErrors()) {
			// logger.debug("Form has errors");
			System.out.println("form has errores");

			// result.addError(new FieldError("registrationform", "login",
			// "rejected stuff"));
			return "elovendo/user/add_user";
		} else {
			byte[] profilePicBytes = null;
			if (!profilePic.isEmpty())
				try {
					profilePicBytes = profilePic.getBytes();
				} catch (IOException e) {
					System.out.println("Error converting to bytes image file");
				}

			userService.addUser(user, profilePicBytes);

			return "elovendo/user/registered_successful";
		}
	}
	
	@RequestMapping(value = "user/edit", method = RequestMethod.GET)
	public String editUserPage(Model model) {
		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

		model.addAttribute("user", user);

		return "elovendo/user/profileEdit";
	}
	
//	@RequestMapping(value = "user/edit", method = RequestMethod.POST)
//	public String editUserPage(@Valid @ModelAttribute(value = "user") User _user,
//			BindingResult result,
//			@ModelAttribute(value = "profilePic") MultipartFile profilePic,
//			@ModelAttribute(value = "confirmPassword") String confirmPassword,
//			Model model, Locale locale) throws LoginNotAvailableException {
//		
//			User user = null;
//			SecurityContext context = SecurityContextHolder.getContext();
//			if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
//				user = (User) SecurityContextHolder.getContext()
//						.getAuthentication().getPrincipal();
//			
//			if (_user.getPassword() != null) user.setPassword(_user.getPassword());
//			if (_user.getFirstName() != null) user.setFirstName(_user.getFirstName());
//			if (_user.getLastName() != null) user.setLastName(_user.getLastName());
//
//			// Check confirm password matches
//			if (!user.getPassword().equals(confirmPassword)) {
//				result.addError(new FieldError("registrationform", "password",
//						messageSource.getMessage("Error.password.missmatch", null, locale)));
//			}
//
//			// check image is jpeg
//			// TODO: image/png, image/gif
//			if (!profilePic.getContentType().equals("image/jpeg")
//					&& !profilePic.getContentType().equals("image/pjpeg")) {
//				result.addError(new FieldError("registrationform", "profilePic",
//						messageSource.getMessage("Error.password.missmatch", null, locale)));
//			}
//
//			if (result.hasErrors()) {
//				logger.error("Form has errors");
//				return "elovendo/user/profileEdit";
//			} else {
//				byte[] profilePicBytes = null;
//				if (!profilePic.isEmpty())
//					try {
//						profilePicBytes = profilePic.getBytes();
//					} catch (IOException e) {
//						System.out.println("Error converting to bytes image file");
//					}
//
//				userService.updateUser(user, profilePicBytes);
//
//				return "elovendo/user/registered_successful";
//			}
//	}
	
	@RequestMapping(value = "user/edit", method = RequestMethod.POST)
	public String editUserPage(
			@RequestParam(required=false, defaultValue="")  String password,
			@RequestParam(required=false, defaultValue="") String confirmPassword,
			@RequestParam(required=false, defaultValue="") String email,
			@RequestParam(required=false, defaultValue="") String firstName,
			@RequestParam(required=false, defaultValue="") String lastName,
			@RequestParam(required=false, defaultValue="") String phone,
			@ModelAttribute(value = "profilePic") MultipartFile profilePic,
			Model model, Locale locale) throws LoginNotAvailableException {

			// Check confirm password matches
//			if (!password.equals("") && !password.equals(confirmPassword)) {
//				result.addError(new FieldError("registrationform", "password",
//						messageSource.getMessage("Error.password.missmatch", null, locale)));
//			}
//
//			// check image is jpeg
//			// TODO: image/png, image/gif
//			if (!profilePic.getContentType().equals("image/jpeg")
//					&& !profilePic.getContentType().equals("image/pjpeg")) {
//				result.addError(new FieldError("registrationform", "profilePic",
//						messageSource.getMessage("Error.password.missmatch", null, locale)));
//			}
//
//			// TODO: Make validations
//			
//			if (result.hasErrors()) {
//				logger.error("Form has errors");
//				return "elovendo/user/profileEdit";
//			} else {
		logger.error("received: " + password + ";" + email + firstName + lastName + phone );
				byte[] profilePicBytes = null;
				if (!profilePic.isEmpty())
					try {
						profilePicBytes = profilePic.getBytes();
					} catch (IOException e) {
						System.out.println("Error converting to bytes image file");
					}

				User user = null;
				SecurityContext context = SecurityContextHolder.getContext();
				if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
					user = (User) SecurityContextHolder.getContext()
							.getAuthentication().getPrincipal();
				
				logger.error("user password: " + user.getPassword());
				logger.error("received password null? " + password.equals(""));
				
				if (!password.equals("")) user.setPassword(password);
				if (!firstName.equals("")) user.setFirstName(firstName);
				if (!lastName.equals("")) user.setLastName(lastName);
				if (!email.equals("")) user.setPassword(email);
				
				userService.updateUser(user, profilePicBytes);

				return "elovendo/user/registered_successful";
//			}
	}

	/**
	 * ITEMS STUFF
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/items/item", method = RequestMethod.GET)
	public String addItemPage(Model model) {
		model.addAttribute("item", new Item());
		model.addAttribute("provinceName", new String());
		model.addAttribute("categoryName", new Category());
		model.addAttribute("subCategoryName", new SubCategory());

		User _user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			_user = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

		// TODO: Ugly workaround in sake for save my mental healthy
		User user = userService.findUserById(_user.getUserId());
		
		model.addAttribute("user", user);

		List<Category> categories = IteratorUtils.toList(categoryService
				.findAllCategories().iterator());
		model.addAttribute("categories", categories);
		List<SubCategory> subCategories = IteratorUtils.toList(categoryService
				.findAllSubCategories().iterator());
		model.addAttribute("subCategories", subCategories);

		return "elovendo/item/add_item";
	}

	@RequestMapping(value = "items/item", method = RequestMethod.POST)
	public String processAddItemWeb(
			@Valid @ModelAttribute(value = "item") Item item,
			BindingResult result,
			@RequestParam(value = "lat", required=true) String lat,
			@RequestParam(value = "lng", required=true) String lng,
			@ModelAttribute(value = "categoryName") String categoryName,
			@ModelAttribute(value = "subCategoryName") long subCategoryId,
			@ModelAttribute(value = "featured") String _featured,
			@ModelAttribute(value = "highlight") String _highlight,
			@ModelAttribute(value = "autoRenew") String _autoRenew,
			@RequestParam("mI") MultipartFile _mainImage,
			@RequestParam("i1") MultipartFile _image1,
			@RequestParam("i2") MultipartFile _image2,
			@RequestParam("i3") MultipartFile _image3,
			Model model)
			throws InvalidItemNameMinLenghtException,
			ProvinceNotFoundException, UserNotFoundException,
			SubCategoryNotFoundException, IOException,
			InsufficientPointsException {

		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
		model.addAttribute("user", user);

		if (result.hasErrors()) {
			logger.error("add item form has errors ");
			for (FieldError e : result.getFieldErrors()) {
				logger.error(e.getRejectedValue() + " - " + e.getField());
				logger.warn("because: " + e.getDefaultMessage());
			};

			@SuppressWarnings("unchecked")
			List<Category> categories = IteratorUtils.toList(categoryService
					.findAllCategories().iterator());
			model.addAttribute("categories", categories);
			@SuppressWarnings("unchecked")
			List<SubCategory> subCategories = IteratorUtils
					.toList(categoryService.findAllSubCategories().iterator());
			model.addAttribute("subCategories", subCategories);
			
			model.addAttribute("user", user);

			return "elovendo/item/add_item";

		} else {
			// Sanitize item description HTML string
			// TODO: more check on tags used
			// TODO: This is service's job service
			PolicyFactory policy = new HtmlPolicyBuilder()
					.allowElements("b", "u", "i", "p", "br", "h1", "h2", "h3",
							"h4", "h5", "h6", "span", "ul", "li").allowAttributes("class")
					.onElements("b", "u", "i", "p", "br", "h1", "h2", "h3",
							"h4", "h5", "h6", "span", "ul", "li").requireRelNofollowOnLinks()
					.toFactory();

			String safeDescription = policy.sanitize(item.getDescription());
			item.setDescription(safeDescription);
			
			// Youtube video format
			if (!item.getYoutubeVideo().equals("")) {
				String wellYoutubeVideo = item.getYoutubeVideo();
				wellYoutubeVideo = Strings.replace(wellYoutubeVideo, "watch?v=", "embed/");
				item.setYoutubeVideo(wellYoutubeVideo);
			}
			
			item.setLatitude(Double.parseDouble(lat));
			item.setLongitude(Double.parseDouble(lng));

			byte[] mainImageBytes = null;
			byte[] image1Bytes = null;
			byte[] image2Bytes = null;
			byte[] image3Bytes = null;

			// Main image
			try { mainImageBytes = _mainImage.getBytes();
			} catch (IOException e) { logger.debug("Error converting image for user " + user.getUserId()); }
			// Image 1
			try { image1Bytes = _image1.getBytes();
			} catch (IOException e) { logger.debug("Error converting image for user " + user.getUserId()); }
			// Image 2
			try { image2Bytes = _image2.getBytes();
			} catch (IOException e) { logger.debug("Error converting image for user " + user.getUserId()); }
			// Image 3
			try { image2Bytes = _image2.getBytes();
			} catch (IOException e) { logger.debug("Error converting image for user " + user.getUserId()); }

			boolean featured = _featured.equalsIgnoreCase("on");
			boolean highlight = _highlight.equalsIgnoreCase("on");
			boolean autoRenew = _autoRenew.equalsIgnoreCase("on");

			int totalPoints = 0;
			if (featured)
				totalPoints += Constant.OP_FEATURE_PRIZE;
			if (highlight)
				totalPoints += Constant.OP_HIGLIGHT_PRIZE;
			if (autoRenew)
				totalPoints += Constant.OP_AUTORENEW_PRIZE;

			if (user.getPoints() < totalPoints)
				throw new InsufficientPointsException();
			else {
				user.setPoints(user.getPoints() - totalPoints);
				userService.updateUser(user);
			}

			itemService.addItem(item, subCategoryId, mainImageBytes, image1Bytes, image2Bytes, 
					image3Bytes, featured, highlight);

			return "elovendo/item/item_create_successful";
		}
	}
	
	/** DELETE ITEM */

	@RequestMapping(value = "/delete/item", method = RequestMethod.POST)
	public void deleteItem(@RequestParam(value = "id", required = true, defaultValue = "0") long itemId) {
		itemService.deleteItem(itemId);
	}

	/** SET ITEM FAVORITE */

	@RequestMapping(value = "item/fav", method = RequestMethod.POST)
	public void toggleItemFavorite(
			@RequestParam(value = "id", required = true, defaultValue = "") String itemId) {
		// TODO implement items favourite
	}

	/**
	 * PAYPAL
	 * 
	 * @throws PurchaseDuplicateException
	 * @throws UserNotFoundException
	 **/
	// http://83.165.60.132:8080/site/paypalok
	@RequestMapping(value = "paypalok", method = RequestMethod.POST)
	@Async
	public String processIPN(HttpServletRequest request)
			throws UserNotFoundException, PurchaseDuplicateException {

		String PAY_PAL_DEBUG = "https://www.sandbox.paypal.com/cgi-bin/webscr";
		String CONTENT_TYPE = "Content-Type";
		String MIME_APP_URLENC = "application/x-www-form-urlencoded";
		String PARAM_NAME_CMD = "cmd";
		String PARAM_VAL_CMD = "_notify-validate";
		String PAYMENT_COMPLETED = "Completed";

		String paymentStatus = "";

		System.out.println("POST Confirm");

		// Create client for Http communication
		HttpClient httpClient = HttpClientBuilder.create().build();

		// Request configuration can be overridden at the request level.
		// They will take precedence over the one set at the client level.
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(40000).setConnectTimeout(40000)
				.setConnectionRequestTimeout(40000).build();

		HttpPost httppost = new HttpPost(PAY_PAL_DEBUG);
		httppost.setHeader(CONTENT_TYPE, MIME_APP_URLENC);

		try {
			// Store Payment info for passing to processing service
			Map<String, String> params = new HashMap<String, String>();

			// Use name/value pair for building the encoded response string
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			// Append the required command
			nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_CMD,
					PARAM_VAL_CMD));

			// Process the parameters
			Enumeration<String> names = request.getParameterNames();
			while (names.hasMoreElements()) {
				String param = names.nextElement();
				String value = request.getParameter(param);
				// Windows-1252
				// String param = new String (names.nextElement().getBytes
				// ("UTF-8"), "8859_1");
				// String value = new String
				// (request.getParameter(param).getBytes ("UTF-8"), "8859_1");

				nameValuePairs.add(new BasicNameValuePair(param, value));
				params.put(param, value);
				System.out.println(param + "=" + value);
				// Get the payment status
				if (param.equalsIgnoreCase("payment_status"))
					paymentStatus = value;
			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			if (verifyResponse(httpClient.execute(httppost))) {
				// Implement your processing logic here, I used an @Async
				// annotation
				// Remember to track completed transactions and don't process
				// duplicates

				// user info: transaction_subject=*userId*

				// Save payment
				String txn_id = params.get("txn_id");
				String payment_date = params.get("payment_date");
				Long userId = Long.valueOf(params.get("custom"));
				String payment_status = params.get("payment_status");
				String item_name = params.get("item_name");
				int item_number = Integer.valueOf(params.get("item_number"));
				String ipn_track_id = params.get("ipn_track_id");
				String receiver_id = params.get("receiver_id");
				BigDecimal mc_gross = new BigDecimal(params.get("mc_gross"));
				BigDecimal mc_fee = new BigDecimal(params.get("mc_fee"));
				String first_name = params.get("first_name");
				String last_name = params.get("last_name");
				String payer_email = params.get("payer_email");
				String residence_country = params.get("residence_country");

				SimpleDateFormat sdf = new SimpleDateFormat(
						"HH:mm:ss MMM d, yyyy z", Locale.ENGLISH);
				Date _date = sdf.parse(payment_date,
						new java.text.ParsePosition(0));
				Calendar date = Calendar.getInstance();
				date.setTime(_date);

				try {
					purchaseService.addPurchase(txn_id, date, userId,
							payment_status, item_name, item_number,
							ipn_track_id, receiver_id, mc_gross, mc_fee,
							first_name, last_name, payer_email,
							residence_country);

					if (paymentStatus.equalsIgnoreCase(PAYMENT_COMPLETED)) {
						User user = userService.findUserById(userId);
						user.setPoints(item_number);
						userService.updateUser(user);

						// Not working
//						Authentication authentication = new UsernamePasswordAuthenticationToken(
//								user, user.getPassword(), user.getAuthorities());
//						SecurityContextHolder.getContext().setAuthentication(authentication);
						
					}
				} catch (UserNotFoundException e) {
					logger.error("PAYMENT FROM GOSTH: " + e.getMessage());
				}

				return "elovendo/pricing/paymentOk";
			} else {
				System.out.println("shit, payment not confirmed");
				return "elovendo/pricing/paymentFailed";
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "redirect:/error";
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "redirect:/error";
		} catch (IOException e) {
			e.printStackTrace();
			return "redirect:/error";
		}
	}

	private boolean verifyResponse(HttpResponse response)
			throws IllegalStateException, IOException {

		String RESP_VERIFIED = "VERIFIED";

		InputStream is = response.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String responseText = reader.readLine();
		is.close();

		System.out.println("RESPONSE : " + responseText);

		return responseText.equals(RESP_VERIFIED);

	}

}
