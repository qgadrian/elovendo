package es.telocompro.model.item;

import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.CategoryRepository;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.item.category.subcategory.SubCategoryRepository;
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
import java.util.Iterator;


@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-config-test.xml")
//@ContextConfiguration(classes = {BeanConfTest.class})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    SubCategoryRepository subCategoryRepository;

    Item item;
    User user;
    Category category;
    SubCategory subCategory;

    @Before
    public void setUp() {
        category = new Category("Categoria Test");
        subCategory = new SubCategory(category, "SubCategoria Test");

        categoryRepository.save(category);
        subCategoryRepository.save(subCategory);

        user = new User("login", "password", "fn", "ln", "addr", "phone", "email",
                0, 0, Role.ROLE_USER, null);
        userRepository.save(user);

        item = new Item(user, subCategory, "title", "description", new BigDecimal(10), Calendar.getInstance());
    }

    @Test
    public void testCreateItem() {
        Long id = itemRepository.save(item).getItemId();
        Assert.assertEquals(item, itemRepository.findOne(id));
    }

    @Test
    public void testFindByTitle() {
        itemRepository.save(item);
        // There is only one
        Item i = itemRepository.findByTitle("tle").iterator().next();

        Assert.assertEquals(user.getUserId(), i.getUser().getUserId());
        Assert.assertEquals(item.getItemId(), i.getItemId());
        Assert.assertEquals(item.getTitle(), i.getTitle());
        Assert.assertEquals(item.getDescription(), i.getDescription());
        Assert.assertEquals(item.getPrize(), i.getPrize());
        Assert.assertEquals(item.getStartDate(), i.getStartDate());
        Assert.assertEquals(item.getSubCategory(), i.getSubCategory());
        Assert.assertEquals(item.getSubCategory().getCategory().getCategoryName(),
                i.getSubCategory().getCategory().getCategoryName());

        Assert.assertEquals("title", itemRepository.findByTitle("tle").iterator().next().getTitle());
    }

    @Test
    public void testFindByTitleNoResults() throws Exception {
        itemRepository.save(item);
        // There is only one
//        Iterator i = itemRepository.findByTitle("PS").iterator();
        Iterator i = itemRepository.findByTitle("zzzzz").iterator();

        Assert.assertFalse(i.hasNext());
    }

    @Test
    public void testFindAllItemsByUserId() {
        itemRepository.save(item);
        Item item2 = new Item(user, subCategory, "title", "description", new BigDecimal(10), Calendar.getInstance());
        itemRepository.save(item2);
        Item item3 = new Item(user, subCategory, "title", "description", new BigDecimal(10), Calendar.getInstance());
        itemRepository.save(item3);

        Assert.assertEquals(3, itemRepository.findByUserId(user.getUserId()).size());
    }

}