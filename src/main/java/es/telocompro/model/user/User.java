package es.telocompro.model.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import es.telocompro.model.user.role.Role;
import es.telocompro.util.Constant;
import es.telocompro.util.SocialMediaService;

/**
 * Created by @adrian on 13/06/14. All rights reserved.
 */

@Entity
@Table(name = "userprofile")
public class User implements UserDetails {
	private static final long serialVersionUID = -8564691953630292818L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userid")
	private Long userId;

	private String socialCompositeKey;

	// @NotNull
	// @Pattern(regexp=Constant.LOGIN_PATTERN)
	private String login;

	// @Column(name = "password")
	// // @Length(min = 8, max = 255)
	// @Pattern(regexp=Constant.PASSWORD_PATTERN)
	private String password;

	@Column(name = "firstname")
	private String firstName;
	@Column(name = "lastname")
	private String lastName;
	private String address;
	private String phone;
	private String email;

	@Column(name = "whatssap")
	private boolean whatssapUser;

	private String avatar; // avatar path
	@Transient
	private String avatar200h;

	private Calendar registerDate;

	@Column
	private int userValue;

	private int points;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "roleid")
	private Role role;

	// @ManyToOne(optional = false, fetch=FetchType.LAZY)
	// @JoinColumn(name = "provinceId")
	// private Province province;

	private boolean enabled;

	@Enumerated(EnumType.STRING)
	@Column(name = "sign_in_provider", length = 20)
	private SocialMediaService signInProvider;

	// @Transient
	// private Collection<? extends GrantedAuthority> authorities = new
	// ArrayList<>();

	public User() {
	}

	public User(String login, String password, String socialCompositeKey, String firstName, String lastName,
			String address, String phone, boolean whatssapUser, String email, String avatar, Role role,
			SocialMediaService signInProvider) {
		this.login = login;
		this.password = password;
		this.socialCompositeKey = socialCompositeKey;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phone = phone;
		this.whatssapUser = whatssapUser;
		this.email = email;
		// this.province = province;
		this.avatar = avatar;
		this.role = role;
		this.signInProvider = signInProvider;
		this.userValue = Constant.DEFAULT_USER_VALUE;
		this.points = Constant.INITIAL_POINTS;
		this.enabled = true;
		this.registerDate = Calendar.getInstance();
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean getEnabled() {
		return this.enabled;
	}

	public Long getUserId() {
		return userId;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setLogin(String login) {
		this.login = login;
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

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public SocialMediaService getSignInProvider() {
		return signInProvider;
	}

	public void setSignInProvider(SocialMediaService signInProvider) {
		this.signInProvider = signInProvider;
	}

	// public Province getProvince() {
	// return province;
	// }
	//
	// public void setProvince(Province province) {
	// this.province = province;
	// }

	public int getUserValue() {
		return userValue;
	}

	public void setUserValue(int userValue) {
		this.userValue = userValue;
	}

	public String getAvatar() {
		return avatar.concat(".jpg");
	}

	/**
	 * Gets user picture file name without file type extensions
	 * 
	 * @return User's picture file name
	 */
	@Transient
	public String getRawAvatar() {
		return avatar;
	}

	public String getSocialCompositeKey() {
		return socialCompositeKey;
	}

	public void setSocialCompositeKey(String socialCompositeKey) {
		this.socialCompositeKey = socialCompositeKey;
	}

	public String getAvatar200h() {
		return avatar.concat("-200h.jpg");
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Calendar getRegisterDate() {
		return registerDate;
	}

	/**
	 * User details Overrides
	 */

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		Role userRol = this.getRole();
		if (userRol != null) {
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRol.getRoleNameString());
			authorities.add(authority);
		}
		return authorities;
	}

	// //TODO temporary authorities implementation - will revise in the next
	// example
	// public void setAuthorities(Collection<? extends GrantedAuthority>
	// authorities) {
	// this.authorities = authorities;
	// }

	@Override
	@Transient
	public String getUsername() {
		return login;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.getEnabled();
	}

	/** OVERRIDE **/

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof User))
			return false;

		User user = (User) obj;
		return user.getLogin().equals(this.login);
	}

}
