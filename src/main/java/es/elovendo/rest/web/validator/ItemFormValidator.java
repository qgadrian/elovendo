package es.elovendo.rest.web.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import es.elovendo.model.item.ItemForm;
import es.elovendo.model.user.User;

public class ItemFormValidator implements Validator {

	private String loginPattern = "^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$";
	private Pattern pattern;
	private Matcher matcher;

	@Override
	public boolean supports(Class<?> clazz) {
		return ItemForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		ItemForm item = (ItemForm) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required.login");

	}

}
