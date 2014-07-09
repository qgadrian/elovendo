package es.telocompro.model.item;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.CategoryRepository;
import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.item.category.subcategory.SubCategoryRepository;
import es.telocompro.model.province.Province;
import es.telocompro.model.province.ProvinceRepository;
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
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    SubCategoryRepository subCategoryRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ProvinceRepository provinceRepository;

    Item item;
    User user;
    Category category;
    SubCategory subCategory;
    Province province;

    @Before
    public void setUp() {
        category = new Category("Categoria Test");
        subCategory = new SubCategory(category, "SubCategoria Test");

        categoryRepository.save(category);
        subCategoryRepository.save(subCategory);
        
        province = new Province("Valladolid");
    	provinceRepository.save(province);

        user = new User("login", "password", "fn", "ln", "addr", "phone", "email", province,
        		roleRepository.findByRoleName(RoleEnum.ROLE_USER), null);
        userRepository.save(user);

        item = new Item(user, subCategory, "title", "description", province, new BigDecimal(10), 
        		Calendar.getInstance(), null);
    }

    @Test
    public void testCreateItem() {
        Long id = itemRepository.save(item).getItemId();
        Assert.assertEquals(item, itemRepository.findOne(id));
    }

//    @Test
//    public void testFindByTitle() {
//        itemRepository.save(item);
//        // There is only one
//        Page<Item> p = itemRepository.findByTitle("tle", new PageRequest(0, 100));
//
//        Assert.assertEquals(user.getUserId(), 
//        		p.getContent().get(0).getUser().getUserId());
//        Assert.assertEquals(item.getItemId(), 
//        		p.getContent().get(0).getItemId());
//        Assert.assertEquals(item.getTitle(), 
//        		p.getContent().get(0).getTitle());
//        Assert.assertEquals(item.getDescription(), 
//        		p.getContent().get(0).getDescription());
//        Assert.assertEquals(item.getPrize(), 
//        		p.getContent().get(0).getPrize());
//        Assert.assertEquals(item.getStartDate(), 
//        		p.getContent().get(0).getStartDate());
//        Assert.assertEquals(item.getSubCategory(), 
//        		p.getContent().get(0).getSubCategory());
//        Assert.assertEquals(item.getSubCategory().getCategory().getCategoryName(),
//        		p.getContent().get(0).getSubCategory().getCategory().getCategoryName());
//
//        Assert.assertEquals("title", itemRepository.findByTitle("tle", new PageRequest(0, 100))
//        		.iterator().next().getTitle());
//    }

    @Test
    public void testFindByTitleNoResults() throws Exception {
        itemRepository.save(item);
        // There is only one
//        Iterator i = itemRepository.findByTitle("PS").iterator();
        Iterator<Item> i = itemRepository.findByTitle("zzzzz", new PageRequest(0, 100))
        		.iterator();

        Assert.assertFalse(i.hasNext());
    }

    @Test
    public void testFindAllItemsByUserId() {
        itemRepository.save(item);
        Item item2 = new Item(user, subCategory, "title", "description", province, 
        		new BigDecimal(10), Calendar.getInstance(), null);
        itemRepository.save(item2);
        Item item3 = new Item(user, subCategory, "title", "description", 
        		province, new BigDecimal(10), Calendar.getInstance(), null);
        itemRepository.save(item3);

        Assert.assertEquals(3, itemRepository.findByUserName(user.getLogin(), 
        		new PageRequest(0, 100)).getNumberOfElements());
    }

}