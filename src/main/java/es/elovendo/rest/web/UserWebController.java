package es.elovendo.rest.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.validator.EmailValidator;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import es.elovendo.model.item.Item;
import es.elovendo.model.user.EditUserForm;
import es.elovendo.model.user.User;
import es.elovendo.model.user.UserForm;
import es.elovendo.rest.exception.EmailNotAvailableException;
import es.elovendo.rest.exception.InvalidSelfVoteException;
import es.elovendo.rest.exception.InvalidVoteUsersException;
import es.elovendo.rest.exception.ItemNotFoundException;
import es.elovendo.rest.exception.LoginNotAvailableException;
import es.elovendo.rest.exception.PurchaseDuplicateException;
import es.elovendo.rest.exception.UserNotFoundException;
import es.elovendo.rest.exception.VoteDuplicateException;
import es.elovendo.service.item.ItemService;
import es.elovendo.service.item.category.CategoryService;
import es.elovendo.service.item.favorite.FavoriteService;
import es.elovendo.service.purchase.PurchaseService;
import es.elovendo.service.user.UserService;
import es.elovendo.util.Constant;

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
	private FavoriteService favoriteService;
	@Autowired
	private MessageSource messageSource;
	@Resource(name="sessionRegistry")
	private SessionRegistry sessionRegistry;

	/**
	 * USER STUFF
	 */

	/** View profile */
	@RequestMapping(value = "profile", method = RequestMethod.GET)
	public String getProfile(Model model) {

		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("user", user);

		model.addAttribute("itemList", itemService.getAllItemsByUser(user));

		model.addAttribute("totalItems", itemService.getNumberUserItems(user));

		// votes
		int votesPositive = userService.getVotesPositive(user);
		int votesNegative = userService.getVotesNegative(user);
		int totalVotes = votesPositive + votesNegative;
		model.addAttribute("votesPositive", votesPositive);
		model.addAttribute("votesNegative", votesNegative);
		model.addAttribute("votesQueued", userService.getVotesQueued(user));
		model.addAttribute("totalVotes", totalVotes);

		// last favorites
		model.addAttribute("lastFavs", favoriteService.getLastFavs(user));

		return "elovendo/user/profile";
	}

	/**
	 * View profile
	 * 
	 * @throws UserNotFoundException
	 */
	@RequestMapping(value = "public/{userName}", method = RequestMethod.GET)
	public String getProfile(@PathVariable(value = "userName") String userName, Model model)
			throws UserNotFoundException {

		User user = userService.findUserByLogin(userName);
		model.addAttribute("user", user);
		
		List<Item> lastItems = itemService.getLastItems(user);
		model.addAttribute("lastItems", lastItems);

		model.addAttribute("totalItems", itemService.getNumberUserItems(user));

		int votesPositive = userService.getVotesPositive(user);
		int votesNegative = userService.getVotesNegative(user);
		int totalVotes = votesPositive + votesNegative;
		model.addAttribute("votesPositive", votesPositive);
		model.addAttribute("votesNegative", votesNegative);
		model.addAttribute("totalVotes", totalVotes);

		return "elovendo/user/publicProfile";
	}

	/**
	 * Add a new user
	 * 
	 * @throws EmailNotAvailableException
	 * @throws LoginNotAvailableException
	 **/

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String addUserPage(WebRequest request, Model model) {

		model.addAttribute("user", new UserForm());
		return "elovendo/user/add_user";

		// ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils();
		// Connection<?> connection =
		// providerSignInUtils.getConnectionFromSession(request);
		//
		// if (connection != null && connection.test()) {
		//
		// String firstName = "", lastName = "", email = "", username = "";
		// byte[] userImage = null;
		//
		// if (connection.getApi() instanceof Facebook) {
		// Facebook facebook = (Facebook) connection.getApi();
		// FacebookProfile facebookProfile =
		// facebook.userOperations().getUserProfile();
		// userImage = facebook.userOperations().getUserProfileImage();
		// username = "gorgorito";
		// firstName = facebookProfile.getFirstName();
		// lastName = facebookProfile.getLastName();
		// email = facebookProfile.getEmail();
		// }
		// // else if (connection.getApi() instanceof Google) {
		// // Google google = (Google) connection.getApi();
		// // userImage =
		// google.plusOperations().getGoogleProfile().getImageUrl().getBytes();
		// // username =
		// google.plusOperations().getGoogleProfile().getDisplayName();
		// // firstName =
		// google.plusOperations().getGoogleProfile().getDisplayName();
		// // lastName =
		// google.plusOperations().getGoogleProfile().getGivenName();
		// // email =
		// google.plusOperations().getGoogleProfile().getAccountEmail();
		// // }
		//
		// Role role = new Role(RoleEnum.ROLE_USER);
		//
		// User user = new User(username,
		// connection.createData().getAccessToken(),
		// firstName, lastName, "", "", email, null, role,
		// SocialMediaService.FACEBOOK);
		//
		// user = userService.addUser(user, userImage);
		//
		// model.addAttribute("user", user);
		//
		// Authentication authentication = new
		// UsernamePasswordAuthenticationToken(user, null,
		// user.getAuthorities());
		// SecurityContextHolder.getContext().setAuthentication(authentication);
		//
		// // FAQ: To obtain session id
		// // RequestContextHolder.currentRequestAttributes().getSessionId();
		//
		// return "elovendo/user/add_user";
		// }

		// model.addAttribute("user", new User());
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String processAddUserWeb(@Valid @ModelAttribute(value = "user") UserForm userForm, 
			BindingResult result,
			@ModelAttribute(value = "profilePic") MultipartFile profilePic,
			Model model, Locale locale)
			throws LoginNotAvailableException, EmailNotAvailableException, UserNotFoundException {

		// check image is jpeg

		// TODO: image/png, image/gif
		// FIXME: Convert objefct to FormUser and make there validations
//		if (!profilePic.getContentType().equals("image/jpeg") && !profilePic.getContentType().equals("image/pjpeg")) {
//			logger.error("not jpg!");
//			logger.error("multipart file is :" + profilePic.getContentType());
//			result.addError(new FieldError("registrationform", "profilePic", messageSource.getMessage(
//					"Error.password.missmatch", null, locale)));
//		}

		// Validate email address
		if (!EmailValidator.getInstance().isValid(userForm.getEmail())) {
			result.addError(new FieldError("user", "email", messageSource.getMessage(
					"Error.user.email", null, locale)));
		}
		
		// Validate phone number
		if (!userForm.isValidPhoneNumber()) {
			result.addError(new FieldError("user", "phone", messageSource.getMessage(
					"Error.user.phone", null, locale)));
		}
		
		// Validate that confirm password matches
		if (!userForm.getPassword().isEmpty() && !userForm.getPassword().equals(userForm.getConfirmPassword())) {
			result.addError(new FieldError("user", "confirmPassword", 
					messageSource.getMessage("Error.password.missmatch", null, locale)));
		}

		if (result.hasErrors()) {

			return "elovendo/user/add_user";

		} else {

			userService.addUser(userForm, profilePic);
			return "elovendo/user/registered_successful";

		}
	}

	@RequestMapping(value = "socialRegister", method = RequestMethod.GET)
	public String addSocialUserPage(WebRequest request, Model model) throws LoginNotAvailableException,
			EmailNotAvailableException {
		return "elovendo/user/add_social_user";
		// ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils();
		// Connection<?> connection =
		// providerSignInUtils.getConnectionFromSession(request);
		//
		// if (connection != null && connection.test()) {
		//
		// String firstName = "", lastName = "", email = "", username = "";
		// byte[] userImage = null;
		//
		// if (connection.getApi() instanceof Facebook) {
		// Facebook facebook = (Facebook) connection.getApi();
		// FacebookProfile facebookProfile =
		// facebook.userOperations().getUserProfile();
		// userImage = facebook.userOperations().getUserProfileImage();
		// username = "";
		// firstName = facebookProfile.getFirstName();
		// lastName = facebookProfile.getLastName();
		// email = facebookProfile.getEmail();
		// }
		//
		// Role role = new Role(RoleEnum.ROLE_USER);
		//
		// User user = new User(username,
		// connection.createData().getAccessToken(),
		// firstName, lastName, "", "", email, null, role,
		// SocialMediaService.FACEBOOK);
		//
		// user = userService.addUser(user, userImage);
		//
		// model.addAttribute("user", user);
		//
		// Authentication authentication = new
		// UsernamePasswordAuthenticationToken(user, null,
		// user.getAuthorities());
		// SecurityContextHolder.getContext().setAuthentication(authentication);
		//
		// // FAQ: To obtain session id
		// // RequestContextHolder.currentRequestAttributes().getSessionId();
		//
		// return "elovendo/user/add_social_user";
		// }
		//
		// // Who knows...
		// model.addAttribute("user", new User());
		// return "elovendo/user/add_user";
	}

	// @RequestMapping(value ="socialRegister", method = RequestMethod.POST)
	// public String addSocialUserPage(@RequestParam(value="username",
	// required=true) String username,
	// Model model, WebRequest request)
	// throws LoginNotAvailableException, EmailNotAvailableException {
	//
	// ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils();
	// Connection<?> connection =
	// providerSignInUtils.getConnectionFromSession(request);
	//
	// if (connection != null && connection.test()) {
	//
	// String firstName = "", lastName = "", email = "";
	// byte[] userImage = null;
	//
	// if (connection.getApi() instanceof Facebook) {
	// Facebook facebook = (Facebook) connection.getApi();
	// FacebookProfile facebookProfile =
	// facebook.userOperations().getUserProfile();
	// userImage = facebook.userOperations().getUserProfileImage();
	// firstName = facebookProfile.getFirstName();
	// lastName = facebookProfile.getLastName();
	// email = facebookProfile.getEmail();
	// }
	//
	// Role role = new Role(RoleEnum.ROLE_USER);
	//
	// ConnectionData data = connection.createData();
	// String cmpKey = connection.getKey().getProviderUserId() +
	// connection.getKey().getProviderId();
	//
	// User user = new User(username, null, cmpKey, firstName, lastName,
	// "", "", email, null, role, SocialMediaService.FACEBOOK);
	//
	// user = userService.addUser(user, userImage);
	//
	// model.addAttribute("user", user);
	//
	// Authentication authentication = new
	// UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	// SecurityContextHolder.getContext().setAuthentication(authentication);
	//
	// // FAQ: To obtain session id
	// // RequestContextHolder.currentRequestAttributes().getSessionId();
	//
	// return "redirect:/index";
	// }
	//
	// // Who knows...
	// model.addAttribute("user", new User());
	// return "elovendo/user/add_user";
	// }

	@RequestMapping(value = "quick_register", method = RequestMethod.GET)
	public String quickAddUserPage(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("provinceName", new String());

		return "elovendo/user/quick_add_user";
	}

	/** EDIT USER **/

	@RequestMapping(value = "user/edit", method = RequestMethod.GET)
	public String editUserPage(Model model) {
		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		EditUserForm userForm = new EditUserForm(user);
		userForm.setAvatar(user.getAvatar());
		userForm.setLogin(user.getLogin());

		model.addAttribute("user", userForm);

		model.addAttribute("uw", user.isWhatssapUser());

		return "elovendo/user/profileEdit";
	}

	@RequestMapping(value = "user/edit", method = RequestMethod.POST)
	public String processEditUserPage(@Valid @ModelAttribute(value = "user") EditUserForm userForm,
			BindingResult result, @ModelAttribute(value = "userPic") MultipartFile userPic,
			@RequestParam(value = "uw", required = false) String whatssapUser, Model model, Locale locale)
			throws LoginNotAvailableException, UserNotFoundException {
		//
		// // check image is jpeg
		// // TODO: image/png, image/gif
		// if (!profilePic.getContentType().equals("image/jpeg")
		// && !profilePic.getContentType().equals("image/pjpeg")) {
		// result.addError(new FieldError("registrationform", "profilePic",
		// messageSource.getMessage("Error.password.missmatch", null, locale)));
		// }

		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// Validate email address
		if (!EmailValidator.getInstance().isValid(userForm.getEmail())) {
			result.addError(new FieldError("user", "email", 
					messageSource.getMessage("Error.user.email", null, locale)));
		}
		
		// Validate that confirm password matches
		if (!userForm.getPassword().isEmpty() && !userForm.getPassword().equals(userForm.getConfirmPassword())) {
			result.addError(new FieldError("user", "confirmPassword", 
					messageSource.getMessage("Error.password.missmatch", null, locale)));
		}

		if (result.hasErrors()) {

			logger.error(userForm);
			userForm.setAvatar(user.getAvatar());
			userForm.setLogin(user.getLogin());
			model.addAttribute("user", userForm);
			return "elovendo/user/profileEdit";
		}

		if (whatssapUser != null)
			userForm.setWhatssapUser(whatssapUser.equalsIgnoreCase("on"));

		user = userService.updateUser(userForm, user.getUserId(), userPic);

		Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
				user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return "redirect:/site/profile";
	}

	/**
	 * VOTE STUFF
	 * 
	 * @throws InvalidVoteUsersException
	 * @throws VoteDuplicateException
	 * @throws ItemNotFoundException
	 * @throws InvalidSelfVoteException
	 */
	@RequestMapping(value = "vote", method = RequestMethod.POST)
	public @ResponseBody boolean processVote(@RequestParam(value = "uv", required = true) long receiverId,
			@RequestParam(value = "iv", required = true) long itemId,
			@RequestParam(value = "vt", required = true) int voteType,
			@RequestParam(value = "msg", required = true) String voteMessage) throws UserNotFoundException,
			PurchaseDuplicateException, ItemNotFoundException, VoteDuplicateException, InvalidVoteUsersException,
			InvalidSelfVoteException {

		User user = null;
		SecurityContext context = SecurityContextHolder.getContext();
		if (!(context.getAuthentication() instanceof AnonymousAuthenticationToken))
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return userService.voteUser(user.getUserId(), receiverId, itemId, voteType, voteMessage) != null;
	}

	/**
	 * PAYPAL
	 * 
	 * @throws PurchaseDuplicateException
	 * @throws UserNotFoundException
	 **/
	// http://www.elovendo.com/site/paypalprocess
	@RequestMapping(value = "paypalprocess", method = RequestMethod.POST)
	@Async
	public @ResponseBody void processIPN(HttpServletRequest request) throws UserNotFoundException,
			PurchaseDuplicateException {

		String PAY_PAL_DEBUG = "https://www.sandbox.paypal.com/cgi-bin/webscr";
		String CONTENT_TYPE = "Content-Type";
		String MIME_APP_URLENC = "application/x-www-form-urlencoded";
		String PARAM_NAME_CMD = "cmd";
		String PARAM_VAL_CMD = "_notify-validate";
		String PAYMENT_COMPLETED = "Completed";

		String paymentStatus = "";

		// logger.debug("Received IPN from payment");

		// Create client for Http communication
		HttpClient httpClient = HttpClientBuilder.create().build();

		// Request configuration can be overridden at the request level.
		// They will take precedence over the one set at the client level.
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(40000).setConnectTimeout(40000)
				.setConnectionRequestTimeout(40000).build();

		HttpPost httppost = new HttpPost(PAY_PAL_DEBUG);
		httppost.setHeader(CONTENT_TYPE, MIME_APP_URLENC);

		try {
			// Store Payment info for passing to processing service
			Map<String, String> params = new HashMap<String, String>();

			// Use name/value pair for building the encoded response string
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			// Append the required command
			nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_CMD, PARAM_VAL_CMD));

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
				// System.out.println(param + "=" + value);
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

				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss MMM d, yyyy z", Locale.ENGLISH);
				Date _date = sdf.parse(payment_date, new java.text.ParsePosition(0));
				Calendar date = Calendar.getInstance();
				date.setTime(_date);

				try {
					purchaseService.addPurchase(txn_id, date, userId, payment_status, item_name, item_number,
							ipn_track_id, receiver_id, mc_gross, mc_fee, first_name, last_name, payer_email,
							residence_country);

					if (paymentStatus.equalsIgnoreCase(PAYMENT_COMPLETED)) {
						logger.info("Payment received: " + params.get("txn_id"));

						User user = userService.findUserById(userId);

						int points = user.getPoints() + item_number;
						user.setPoints(points);
						user = userService.updateUser(user);
						
//						List<SessionInformation> li = sessionRegistry.getAllSessions(user, false);
//						for (SessionInformation si : li) {
//							sessionRegistry.registerNewSession(si.getSessionId(), user);
//						}
						
						@SuppressWarnings({ "rawtypes", "unchecked" })
						List<User> lwi = (ArrayList<User>) (ArrayList) sessionRegistry.getAllPrincipals();
						for (User u : lwi) {
							if (u.equals(user)) {
								List<SessionInformation> li = sessionRegistry.getAllSessions(u, false);
								u.setPoints(user.getPoints());
								for (SessionInformation si : li)
									sessionRegistry.registerNewSession(si.getSessionId(), u);
							}
						}

					}
				} catch (UserNotFoundException e) {
					logger.error("Payment from non existing user: " + userId);
				} catch (PurchaseDuplicateException e) {
					logger.error("Purchase " + params.get("txn_id") + " duplicated");
				}

			} else {
				logger.error("Payment not confirmed for " + params.get("txn_id"));
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			// return "redirect:/error";
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			// return "redirect:/error";
		} catch (IOException e) {
			e.printStackTrace();
			// return "redirect:/error";
		}
	}

	private boolean verifyResponse(HttpResponse response) throws IllegalStateException, IOException {

		String RESP_VERIFIED = "VERIFIED";

		InputStream is = response.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String responseText = reader.readLine();
		is.close();

		System.out.println("RESPONSE : " + responseText);

		return responseText.equals(RESP_VERIFIED);

	}

}
