import es.telocompro.model.user.User;
import es.telocompro.model.user.UserRepository;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-config-test.xml")
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UserRepositoryTest extends TestCase {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testFindByLogin() throws Exception {
        User user = new User("login", "fn", "ln", "addr", "phone", "email");

        userRepository.save(user);

        Assert.assertEquals(user, userRepository.findByLogin("login"));
    }
}