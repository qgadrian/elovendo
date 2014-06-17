package es.telocompro.model.item;

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
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ItemRepositoryTest extends TestCase {

    @Autowired
    ItemRepository itemRepository;
    Item item;

    @Before
    public void setUp() {
        item = new Item("title", "description", new BigDecimal(10), Calendar.getInstance());
    }

    @Test
    public void testCreateItem() throws Exception {
        Long id = itemRepository.save(item).getItemId();
        Assert.assertEquals(item, itemRepository.findOne(id));
    }

    @Test
    public void testFindByTitle() throws Exception {
        itemRepository.save(item);
        Assert.assertEquals(item, itemRepository.findByTitle("tle"));
        Assert.assertEquals("title", itemRepository.findByTitle("tle").getTitle());
    }

}