package es.telocompro.rest.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.core.Context;
import es.telocompro.model.item.Item;
import es.telocompro.model.province.Province;
import es.telocompro.model.user.User;
import es.telocompro.rest.controller.exception.LoginNotAvailableException;
import es.telocompro.rest.controller.exception.ProvinceNotFoundException;
import es.telocompro.rest.controller.exception.SubCategoryNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;
import es.telocompro.rest.web.validator.FormValidator;
import es.telocompro.service.exception.InvalidItemNameMinLenghtException;
import es.telocompro.service.item.ItemService;
import es.telocompro.service.province.ProvinceService;
import es.telocompro.service.user.UserService;

@Controller
@SuppressWarnings("unused")
@RequestMapping("/site/")
public class UserWebController {

	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private ProvinceService provinceService;

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
	public String processAddUserWeb(@Valid @ModelAttribute(value = "user") User user,
			@ModelAttribute(value = "provinceName") String provinceName,
			@ModelAttribute(value = "profilePic") MultipartFile profilePic,
			BindingResult result) throws ProvinceNotFoundException, LoginNotAvailableException {
		
		// Check html pattern bypass matching input values
//		String loginPattern = "^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$";
//		String passwordPattern = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
//		
//		Pattern loginMatcherPattern = Pattern.compile(loginPattern);
//		Pattern passwordMatcherPattern = Pattern.compile(loginPattern);
//		Matcher loginMatcher = loginMatcherPattern.matcher(user.getLogin());
//		Matcher passwordMatcher = passwordMatcherPattern.matcher(user.getPassword());
//		
//		if (loginMatcher.find() && passwordMatcher.find()) {
//		    System.out.println("Fields ok");
//		} else {
//		    System.out.println("Bad login or password format");
//		    return "elovendo/index";
//		}
		
//		FormValidator formValidator = new FormValidator();
//		formValidator.validate(user, result);

		//		result.addError(new FieldError("registrationform", "name", "rejected stuff"));
//		return "elovendo/user/add_user";
		
		if (result.hasErrors()) {
			System.out.println("Form has errors");
//			result.addError(new FieldError("registrationform", "login", "rejected stuff"));
			return "elovendo/user/add_user";
		} 
		else  {
			System.out.println("Form is ok");

			byte[] profilePicBytes = null;
			if (!profilePic.isEmpty()) try {
				profilePicBytes = profilePic.getBytes();
			} catch (IOException e) {
				System.out.println("Error converting to bytes image file");
			}
	
			userService.addUser(user, provinceName, profilePicBytes);
	
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

	/** PAYPAL **/
	// @RequestMapping(value = "paypalok", method = RequestMethod.POST)
	// public void paypalProcessWeb(HttpServletRequest httpRequest) {
	//
	// System.out.println("request uri ---> " + httpRequest.getRequestURI());
	// System.out.println("parameters...");
	// for (String string : httpRequest.getParameterMap().keySet()) {
	// System.out.println("Parameter " + string + " -- value: " +
	// httpRequest.getParameter(string));
	// }
	//
	// String paypalUrl = "https://www.paypal.com/cgi-bin/webscr";
	// RestTemplate restTemplate = new RestTemplate();
	//
	// HttpMessageConverter formHttpMessageConverter = new
	// FormHttpMessageConverter();
	// HttpMessageConverter stringHttpMessageConverternew = new
	// StringHttpMessageConverter();
	// List<HttpMessageConverter> messageConverterList = new ArrayList<>();
	// messageConverterList.add(formHttpMessageConverter);
	// messageConverterList.add(stringHttpMessageConverternew);
	// restTemplate.setMessageConverters(messageConverterList);
	//
	// ResponseEntity<String> response = restTemplate.postForEntity(paypalUrl,
	// httpRequest.getParameterMap().keySet(), String.class);
	// HttpStatus status = response.getStatusCode();
	// String restCall = response.getBody();
	// System.out.println("PAYPAL SAYS:");
	// System.out.println("response code: " + response.getStatusCode());
	// System.out.println("response code: " + response.getHeaders().toString());
	// System.out.println("response from paypal " + restCall);
	//
	// }

	@RequestMapping(value = "paypalok", method = RequestMethod.POST)
	public void processIPN(HttpServletRequest request) {

		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			System.out.println("Error enconding http request");
		}

		String PAY_PAL_DEBUG = "https://www.sandbox.paypal.com/cgi-bin/webscr";
		String CONTENT_TYPE = "Content-Type";
		String MIME_APP_URLENC = "application/x-www-form-urlencoded";
		String PARAM_NAME_CMD = "cmd";
		String PARAM_VAL_CMD = "_notify-validate";

		System.out.println("POST Confirm");

		// Create client for Http communication
		HttpClient httpClient = new DefaultHttpClient();
		HttpParams clientParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(clientParams, 40000);
		HttpConnectionParams.setSoTimeout(clientParams, 40000);

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

				nameValuePairs.add(new BasicNameValuePair(param, value));
				params.put(param, value);
				System.out.println(param + "=" + value);
			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			System.out.println("Going to send " + httppost.getURI().toString());
			System.out
					.println("With params " + httppost.getParams().toString());

			if (verifyResponse(httpClient.execute(httppost))) {
				// Implement your processing logic here, I used an @Asyn
				// annotation
				// Remember to track completed transactions and don't process
				// duplicates
				System.out
						.println("here comes the logic stuff (should be a good sign)");
			} else {
				System.out.println("shit, payment not confirmed");
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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