package es.elovendo.model.user;

import javax.validation.constraints.Pattern;

import es.elovendo.util.Constant;

public class EditUserForm {

	@Pattern(regexp = Constant.PASSWORD_PATTERN)
	String password;
	
	String confirmPassword;
	
	String firstName;
	String lastName;
	String address;
	String phone;
	boolean whatssapUser;

	// Will use a well-tested method instead rely on a string pattern
	// EmailValidator.getInstance().isValid(String email);
	String email;
	
	// Parameters to inject if page if form is invalid 
	String avatar;
	String login;

	public EditUserForm() {}

	public EditUserForm(String password, String confirmPassword, String firstName, String lastName, 
			String address, String phone, boolean whatssapUser, String email) {
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phone = phone;
		this.whatssapUser = whatssapUser;
		this.email = email;
	}
	
	public EditUserForm(User user) {
		this.password = user.getPassword();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.address = user.getAddress();
		this.phone = user.getPhone();
		this.whatssapUser = user.isWhatssapUser();
		this.email = user.getEmail();
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isWhatssapUser() {
		return whatssapUser;
	}

	public void setWhatssapUser(boolean whatssapUser) {
		this.whatssapUser = whatssapUser;
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Override
	public String toString() {
		return "EditUserForm [password=" + password + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", address=" + address + ", phone=" + phone + ", whatssapUser=" + whatssapUser + ", email=" + email
				+ ", avatar=" + avatar + ", login=" + login + "]";
	}
	
}
