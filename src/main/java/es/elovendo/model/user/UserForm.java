package es.elovendo.model.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import es.elovendo.util.Constant;

public class UserForm {

	@NotNull
	@Length(min = 3, max = 23)
	String login;

	@Pattern(regexp = Constant.PASSWORD_PATTERN)
	String password;

	String confirmPassword;

	String firstName;
	String lastName;
	String phone;
	boolean whatssap = false;

	// Will use a well-tested method instead rely on a string pattern
	// EmailValidator.getInstance().isValid(String email);
	String email;

	String avatar;

	public UserForm() {
	}

	public UserForm(String login, String password, String confirmPassword, String firstName, String lastName,
			String phone, boolean whatssap, String email) {
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.whatssap = whatssap;
		this.email = email;
	}

	public UserForm(User user) {
		this.login = user.getLogin();
		this.password = user.getPassword();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.phone = user.getPhone();
		this.whatssap = user.isWhatssapUser();
		this.email = user.getEmail();
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isWhatssap() {
		return whatssap;
	}

	public void setWhatssap(boolean whatssap) {
		this.whatssap = whatssap;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * Password is empty
	 * 
	 * @return True if password is empty
	 */
	public boolean hasEmptyPassword() {
		return this.password.isEmpty();
	}

	/**
	 * Password is empty
	 * 
	 * @return True if password is empty
	 */
	public boolean hasEmptyConfirmPassword() {
		return this.confirmPassword.isEmpty();
	}

	/**
	 * Password and confirmPassword match
	 * 
	 * @return True if password and confirmPassword are equal
	 */
	public boolean isPasswordMatch() {
		return this.password.equals(this.confirmPassword);
	}

	/* Helper methods */

	/**
	 * Return the phone number trimmed
	 * 
	 * @return Phone's number without spaces
	 */
	public long getPhoneNumber() {
		String temp = this.phone.replace("+", "");
		if (this.phone.contains(" ")) {
			String deleteTarget = temp.substring(0, this.phone.indexOf(" "));
			temp = temp.replaceFirst(deleteTarget, "").trim();
		}
		return Long.valueOf(temp.replace(" ", ""));
	}

	/**
	 * Returns phone's number country code, without '+' symbol
	 * 
	 * @return Country code
	 */
	public int getCountryCode() {
		String temp = this.phone.replace("+", "");
		if (this.phone.contains(" ")) {
			temp = temp.substring(0, this.phone.indexOf(" "));
		}
		return Integer.valueOf(temp);
	}

	public boolean isValidPhoneNumber() {
		// If no phone number provided, just ignore it
		if (this.phone.isEmpty())
			return true;

		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		PhoneNumber phoneNumber = new PhoneNumber();
		phoneNumber.setCountryCode(34).setNationalNumber(getPhoneNumber());
		return phoneUtil.isValidNumber(phoneNumber);
	}

	@Override
	public String toString() {
		return "UserForm [login=" + login + ", password=" + password + ", firstName=" + firstName + ", lastName="
				+ lastName + ", phone=" + phone + ", whatssapUser=" + whatssap + ", email=" + email + ", avatar="
				+ avatar + "]";
	}

}
