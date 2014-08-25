package es.telocompro.rest.web.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import es.telocompro.model.user.User;

public class FormValidator implements Validator {

	private String loginPattern = "^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$";
	private Pattern pattern;
	private Matcher matcher;

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		User user = (User) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "required.login");

		// input string contains numeric values only
		if (user != null) {
			pattern = Pattern.compile(loginPattern);
			matcher = pattern.matcher(user.getLogin());
			if (!matcher.matches()) {
				errors.rejectValue("username", "login.invalid");
			}
		}

	}

}
