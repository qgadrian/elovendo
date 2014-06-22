package es.telocompro.model.user.role;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import es.telocompro.util.RoleEnum;

@Entity
@Table(name = "role")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="roleid")
	private Long roleId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="rolename")
	private RoleEnum roleName;
	
	protected Role() {}

	public Role(RoleEnum roleName) {
		super();
		this.roleName = roleName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public RoleEnum getRoleName() {
		return roleName;
	}
	
	@Transient
	public String getRoleNameString() {
		return this.getRoleName().toString();
	}

}
