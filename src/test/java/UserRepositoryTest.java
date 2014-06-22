import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.telocompro.model.user.User;
import es.telocompro.model.user.UserRepository;
import es.telocompro.model.user.role.RoleRepository;
import es.telocompro.util.RoleEnum;

@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-config-test.xml")
//@ContextConfiguration(classes = {BeanConfTest.class})
@Transactional
@TransactionConfiguration(defaultRollback = true)
@WebAppConfiguration
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    User user;

    @Before
    public void setUp() {
        user = new User("login", "password", "fn", "ln", "addr", "phone", "email",
                8, 3, roleRepository.findByRoleName(RoleEnum.ROLE_USER), null);
    }

    @Test
    public void testSaveUser() {
        userRepository.save(user);
        User u = userRepository.findOne(user.getUserId());

        Assert.assertEquals("login", user.getLogin());
        Assert.assertEquals("password", user.getPassword());
        Assert.assertEquals("fn", user.getFirstName());
        Assert.assertEquals("ln", user.getLastName());
        Assert.assertEquals("addr", user.getAddress());
        Assert.assertEquals("phone", user.getPhone());
        Assert.assertEquals("email", user.getEmail());
        Assert.assertEquals(RoleEnum.ROLE_USER, user.getRole().getRoleName());
        Assert.assertEquals(null, user.getSignInProvider());
        Assert.assertEquals(8, user.getVotesPositive());
        Assert.assertEquals(3, user.getVotesNegative());
        Assert.assertEquals(u.getVoteRating(), user.getVoteRating());

    }

    @Test
    public void testFindByLogin() {
        userRepository.save(user);

        Assert.assertEquals(user, userRepository.findByLogin("login"));
    }

    @Test
    public void testGetVoteRating() {
        userRepository.save(user);

        Assert.assertEquals(72, user.getVoteRating());
    }
}