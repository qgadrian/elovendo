package es.telocompro.rest.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.CharEncoding;
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
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import es.telocompro.model.item.Item;
import es.telocompro.model.province.Province;
import es.telocompro.model.user.User;
import es.telocompro.rest.controller.MainController;
import es.telocompro.rest.controller.exception.LoginNotAvailableException;
import es.telocompro.rest.controller.exception.ProvinceNotFoundException;
import es.telocompro.rest.controller.exception.PurchaseDuplicateException;
import es.telocompro.rest.controller.exception.PurchaseNotFoundException;
import es.telocompro.rest.controller.exception.SubCategoryNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;
import es.telocompro.rest.web.validator.FormValidator;
import es.telocompro.service.exception.InvalidItemNameMinLenghtException;
import es.telocompro.service.item.ItemService;
import es.telocompro.service.province.ProvinceService;
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
	private ProvinceService provinceService;
	@Autowired
	private PurchaseService purchaseService;

	/**
	 * USER STUFF
	 */
	
	/** Add a new user **/
	
    @RequestMapping(value="new_user", method = RequestMethod.GET)
    public String addUserPage(Model model) {
    	model.addAttribute("user", new User());
    	model.addAttribute("provinceName", new String());
    	
    	@SuppressWarnings("unchecked")
		List<Province> provinces = IteratorUtils.toList(provinceService.findAllProvinces().iterator());
    	model.addAttribute("provinces", provinces);
    	
        return "elovendo/user/add_user";
    }
	
	@RequestMapping(value = "new_user", method = RequestMethod.POST)
	public String processAddUserWeb(@ModelAttribute(value = "user") User user, BindingResult result,
			@ModelAttribute(value = "provinceName") String provinceName,
			@ModelAttribute(value = "profilePic") MultipartFile profilePic, ModelMap model) 
					throws ProvinceNotFoundException, LoginNotAvailableException {
		//FIXME: Edit input type email
		
//		System.gc();
//		
//		Pattern loginMatcherPattern = Pattern.compile(Constant.loginPattern);
//		Pattern passwordMatcherPattern = Pattern.compile(Constant.passwordPattern);
//		Matcher loginMatcher = loginMatcherPattern.matcher(user.getLogin());
//		Matcher passwordMatcher = passwordMatcherPattern.matcher(user.getPassword());
//		
//		if (loginMatcher.find() && passwordMatcher.find()) {
//		    System.out.println("Fields ok");
//		    
//			byte[] profilePicBytes = null;
//			if (!profilePic.isEmpty()) try {
//				profilePicBytes = profilePic.getBytes();
//			} catch (IOException e) {
//				System.out.println("Error converting to bytes image file");
//			}
//			
//			// TODO: Workaround because multipart/form-data don't send data as UTF-8
//			String firstName = new String (user.getFirstName().getBytes ("iso-8859-1"), "UTF-8");
//			user.setFirstName(firstName);
//			String lastName = new String (user.getLastName().getBytes ("iso-8859-1"), "UTF-8");
//			user.setLastName(lastName);
//			String province = new String (provinceName.getBytes ("iso-8859-1"), "UTF-8");
//		    
//			userService.addUser(user, province, profilePicBytes);
//		} else {
//		    System.out.println("Bad login or password format");
//		    return "redirect:/error";
//		}
//		
//		return "elovendo/user/registered_successful";
		
		FormValidator formValidator = new FormValidator();
		formValidator.validate(user, result);

//		return "elovendo/user/add_user";
		
		if (result.hasErrors()) {
//			logger.debug("Form has errors");
			System.out.println("form has errores");
			
			@SuppressWarnings("unchecked")
			List<Province> provinces = IteratorUtils.toList(provinceService.findAllProvinces().iterator());
	    	model.addAttribute("provinces", provinces);
	    	
//			result.addError(new FieldError("registrationform", "login", "rejected stuff"));
			return "elovendo/user/add_user";
		} 
		else  {
			logger.debug("Form is ok");
			
			// TODO: Workaround because multipart/form-data don't send data as UTF-8
//			String firstName = new String (user.getFirstName().getBytes ("iso-8859-1"), "UTF-8");
//			user.setFirstName(firstName);
//			String lastName = new String (user.getLastName().getBytes ("iso-8859-1"), "UTF-8");
//			user.setLastName(lastName);
//			String province = new String (provinceName.getBytes ("iso-8859-1"), "UTF-8");

			byte[] profilePicBytes = null;
			if (!profilePic.isEmpty()) try {
				profilePicBytes = profilePic.getBytes();
			} catch (IOException e) {
				System.out.println("Error converting to bytes image file");
			}
	
			userService.addUser(user, provinceName, profilePicBytes);
//	
			return "elovendo/user/registered_successful";
		}
	}

	/**
	 * ITEMS REFERREAL
	 */
	@RequestMapping(value = "items/item", method = RequestMethod.POST)
	public String processAddItemWeb(@ModelAttribute(value = "item") Item item,
			@ModelAttribute(value = "provinceName") String provinceName,
			@ModelAttribute(value = "categoryName") String categoryName,
			@ModelAttribute(value = "subCategoryName") String subCategoryName,
			@RequestParam("image1") MultipartFile profilePic)
			throws InvalidItemNameMinLenghtException,
			ProvinceNotFoundException, UserNotFoundException,
			SubCategoryNotFoundException, IOException {

		if (SecurityContextHolder.getContext().getAuthentication()
				.isAuthenticated())
			System.out.println("User is not authenticated!!!!!");
		else
			System.out.println("user ocrrectly authenticated :)");

		byte[] imgBytes = null;

		try {
			imgBytes = profilePic.getBytes();
		} catch (IOException e) {
			System.out.println("Error converting to bytes image file");
		}

		itemService.addItem(item, subCategoryName, provinceName, imgBytes);

		return "elovendo/item/item_create_successful";
	}

	/** PAYPAL 
	 * @throws PurchaseDuplicateException 
	 * @throws UserNotFoundException **/
	//http://83.165.60.132:8080/site/paypalok
	@RequestMapping(value = "paypalok", method = RequestMethod.POST)
	@Async
	public String processIPN(HttpServletRequest request) throws UserNotFoundException, PurchaseDuplicateException {

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
			nameValuePairs.add(new BasicNameValuePair(PARAM_NAME_CMD, PARAM_VAL_CMD));

			// Process the parameters
			Enumeration<String> names = request.getParameterNames();
			while (names.hasMoreElements()) {
				String param = names.nextElement();
				String value = request.getParameter(param);
//				Windows-1252
//				String param = new String (names.nextElement().getBytes ("UTF-8"), "8859_1");
//				String value = new String (request.getParameter(param).getBytes ("UTF-8"), "8859_1");

				nameValuePairs.add(new BasicNameValuePair(param, value));
				params.put(param, value);
				System.out.println(param + "=" + value);
				// Get the payment status
				if (param.equalsIgnoreCase("payment_status")) paymentStatus = value;
			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			if (verifyResponse(httpClient.execute(httppost))) {
				// Implement your processing logic here, I used an @Async annotation
				// Remember to track completed transactions and don't process duplicates
				
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
					purchaseService.addPurchase(txn_id, date, userId, payment_status, item_name, 
							item_number, ipn_track_id, receiver_id, mc_gross, mc_fee, first_name, 
							last_name, payer_email, residence_country);
				
					if (paymentStatus.equalsIgnoreCase(PAYMENT_COMPLETED)) {
						User user = userService.findUserById(userId);
						user.setPoints(item_number);
						userService.updateUser(user);
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
