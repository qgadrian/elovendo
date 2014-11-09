package es.elovendo.model.user;

import org.springframework.data.jpa.repository.Query;
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

    User findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.socialCompositeKey = :compKey")
    User findUserBySocialUserKey(@Param("compKey") String compositeKey);

    @Query("SELECT email FROM User WHERE userid = :userId")
    String findEmailByUserId(@Param("userId") long userId);
}
