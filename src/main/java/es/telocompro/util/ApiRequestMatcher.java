package es.telocompro.util;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class ApiRequestMatcher implements RequestMatcher {

	// Class
	private static ApiRequestMatcher apiRequestMatcher;

	private Pattern allowedMethods = Pattern
			.compile("^(GET|HEAD|TRACE|OPTIONS)$");
	private RegexRequestMatcher apiMatcher = new RegexRequestMatcher(
			Constant.API_PATTERN, null);
	private RegexRequestMatcher paypalMatcher = new RegexRequestMatcher(
			Constant.PAYPAL_PROCESS_PATTERN, null);

	public ApiRequestMatcher() {
	}

	public static ApiRequestMatcher getInstance() {

		if (apiRequestMatcher == null) {
			apiRequestMatcher = new ApiRequestMatcher();
		}

		return apiRequestMatcher;
	}

	@Override
	public boolean matches(HttpServletRequest request) {

		// No CSRF due to allowedMethod
		if (allowedMethods.matcher(request.getMethod()).matches())
			return false;

		// No CSRF due to API call if matches
		// CSRF for everything else that is not an API call or an allowedMethod
		boolean bool = !apiMatcher.matches(request) && !paypalMatcher.matches(request);
//		System.out.println("ApiRequestMatcher: bool is " + bool);
//		System.out.println("RECEIVENG REQUEST URL " + request.getRequestURL());
		return bool;
	}

}
