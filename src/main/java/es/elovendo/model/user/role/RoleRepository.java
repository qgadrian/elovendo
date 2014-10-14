package es.elovendo.model.user.role;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.elovendo.util.RoleEnum;

@Repository("roleRepository")
public interface RoleRepository extends CrudRepository<Role, Long> {

	@Query("SELECT r FROM Role r WHERE roleName = :rolename")
	Role findByRoleName(@Param("rolename") RoleEnum roleEnum);
	
}
