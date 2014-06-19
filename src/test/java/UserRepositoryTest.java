import es.telocompro.model.item.Item;
import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.user.User;
import es.telocompro.model.user.UserRepository;
import es.telocompro.util.Role;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;

@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-config-test.xml")
//@ContextConfiguration(classes = {BeanConfTest.class})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User user;

    @Before
    public void setUp() {
        user = new User("login", "password", "fn", "ln", "addr", "phone", "email",
                8, 3, Role.ROLE_USER, null);
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
        Assert.assertEquals(Role.ROLE_USER, user.getRole());
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