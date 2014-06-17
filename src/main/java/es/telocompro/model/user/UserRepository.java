package es.telocompro.model.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
* Created by @adrian on 14/06/14.
* All rights reserved.
*/

@Repository("userRepository")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByLogin(@Param("login") String login);
}
