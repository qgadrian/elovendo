package es.telocompro.model.user;

import java.util.ArrayList;
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
import es.telocompro.util.SocialMediaService;

/**
 * Created by @adrian on 13/06/14.
 * All rights reserved.
 */

@Entity
@Table(name = "userprofile")
public class User implements UserDetails {
	private static final long serialVersionUID = -8564691953630292818L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userid")
    private Long userId;

    private String login;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    private String address;
    private String phone;
    private String email;
    @Column(name = "votespositive")
    private int votesPositive;
    @Column(name = "votesnegative")
    private int votesNegative;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "role", length = 20, nullable = false)
//    @Column(name = "roleid")
    @ManyToOne(optional=false, fetch=FetchType.EAGER)
    @JoinColumn(name = "roleid")
    private Role role;
    
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "sign_in_provider", length = 20)
    private SocialMediaService signInProvider;
    
//    @Transient
//    private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();


    protected User() {
    }

    public User(String login, String password, String firstName, String lastName, String address, String phone,
                String email, int votesPositive, int votesNegative, Role role, SocialMediaService signInProvider) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.votesPositive = votesPositive;
        this.votesNegative = votesNegative;
        this.role = role;
        this.signInProvider = signInProvider;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getVotesPositive() {
        return votesPositive;
    }

    public void setVotesPositive(int votesPositive) {
        this.votesPositive = votesPositive;
    }

    public int getVotesNegative() {
        return votesNegative;
    }

    public void setVotesNegative(int votesNegative) {
        this.votesNegative = votesNegative;
    }

    @Transient
    public int getVoteRating() {
//        return Math.round((votesPositive/(votesPositive+votesNegative)) * 100);
        return Math.round((votesPositive*100)/(votesPositive+votesNegative));
    }
    
    /**
     * User details Overrides
     */

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
        Role userRol = this.getRole();
        if(userRol != null) {
        	SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRol.getRoleNameString());
            authorities.add(authority);
        }
        return authorities;
	}
	
//	//TODO temporary authorities implementation - will revise in the next example
//    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
//        this.authorities = authorities;
//    }

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

}
