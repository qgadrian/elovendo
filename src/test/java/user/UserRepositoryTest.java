//package user;
//
//import java.io.IOException;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//
//import es.telocompro.model.item.Item;
//import es.telocompro.model.item.category.Category;
//import es.telocompro.model.item.category.CategoryRepository;
//import es.telocompro.model.item.category.subcategory.SubCategory;
//import es.telocompro.model.item.category.subcategory.SubCategoryRepository;
//import es.telocompro.model.province.Province;
//import es.telocompro.model.province.ProvinceRepository;
//import es.telocompro.model.user.User;
//import es.telocompro.model.user.UserRepository;
//import es.telocompro.model.user.role.RoleRepository;
//import es.telocompro.rest.controller.exception.InvalidVoteUsersException;
//import es.telocompro.rest.controller.exception.ItemNotFoundException;
//import es.telocompro.rest.controller.exception.LoginNotAvailableException;
//import es.telocompro.rest.controller.exception.ProvinceNotFoundException;
//import es.telocompro.rest.controller.exception.SubCategoryNotFoundException;
//import es.telocompro.rest.controller.exception.UserNotFoundException;
//import es.telocompro.rest.controller.exception.VoteDuplicateException;
//import es.telocompro.service.exception.InvalidItemNameMinLenghtException;
//import es.telocompro.service.item.ItemService;
//import es.telocompro.service.user.UserService;
//import es.telocompro.util.Constant;
//import es.telocompro.util.RoleEnum;
//import static es.telocompro.util.Constant.*;
//
//@Configuration
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:/spring-config-test.xml")
////@ContextConfiguration(classes = {BeanConfTest.class})
//@Transactional
//@TransactionConfiguration(defaultRollback = true)
//@WebAppConfiguration
//public class UserRepositoryTest {
//
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    RoleRepository roleRepository;
//    @Autowired
//    CategoryRepository categoryRepository;
//    @Autowired
//    SubCategoryRepository subCategoryRepository;
//    @Autowired
//    ProvinceRepository provinceRepository;
//    @Autowired
//    UserService userService;
//    @Autowired
//    ItemService itemService;
//
//    private User user;
//    private Province province;
//    private Item item;
//    private SubCategory subCategory;
//    private Category category;
//
//    @Before
//    public void setUp() throws InvalidItemNameMinLenghtException, UserNotFoundException, 
//    		SubCategoryNotFoundException, ProvinceNotFoundException, LoginNotAvailableException, IOException {
//    	
//    	province = new Province("Valladdolid");    	
//        category = new Category("Categoria Test");
//        subCategory = new SubCategory(category, "SubCategoria Test");
//        
//        provinceRepository.save(province);
//        categoryRepository.save(category);
//        subCategoryRepository.save(subCategory);
//        
//        user = userService.addUser("login", "password", "fn", "ln", "addr", "phone", 
//        		"email", province.getProvinceName(), null);
//
//        item = itemService.addItem(user.getLogin(), subCategory.getSubCategoryName(), "title", "d", 
//        		province.getProvinceName(), 2, null, false, false, null);
//    }
//
//    @Test
//    public void testSaveUser() {
//
//        Assert.assertEquals("login", user.getLogin());
//        Assert.assertEquals("password", user.getPassword());
//        Assert.assertEquals("fn", user.getFirstName());
//        Assert.assertEquals("ln", user.getLastName());
//        Assert.assertEquals("addr", user.getAddress());
//        Assert.assertEquals("phone", user.getPhone());
//        Assert.assertEquals("email", user.getEmail());
//        Assert.assertEquals(RoleEnum.ROLE_USER, user.getRole().getRoleName());
//        Assert.assertEquals(null, user.getSignInProvider());
//        Assert.assertEquals(Constant.DEFAULT_USER_VALUE, user.getUserValue());
//
//    }
//
//    @Test
//    public void testFindByLogin() {
//        Assert.assertEquals(user, userRepository.findByLogin("login"));
//    }
//
//    @Test
//    public void testVotePending() throws UserNotFoundException, ItemNotFoundException, 
//    		LoginNotAvailableException, ProvinceNotFoundException, VoteDuplicateException, InvalidVoteUsersException {
//    	
//        User user2 = userService.addUser("usuario", "password", "f", "l", "a", "password", "e", province.getProvinceName(), null);
//        
//        userService.voteUser(user2.getUserId(), user.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//
//        Assert.assertEquals(Constant.DEFAULT_USER_VALUE, user.getUserValue());
//    }
//    
//    @Test
//    public void testVote() throws UserNotFoundException, ItemNotFoundException, 
//    		LoginNotAvailableException, ProvinceNotFoundException, VoteDuplicateException, InvalidVoteUsersException {
//    	
//        User user2 = userService.addUser("usuario", "password", "f", "l", "a", "password", "e", province.getProvinceName(), null);
//
//        userService.voteUser(user2.getUserId(), user.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//        userService.voteUser(user.getUserId(), user2.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//
//        // Reability is 0.5f as user default value is 60
//        // 70 + Base * reability = 90
//        Assert.assertEquals(85, user.getUserValue());
//        Assert.assertEquals(85, user2.getUserValue());
//    }
//    
//    @Test(expected=InvalidVoteUsersException.class)
//    public void testVoteInvalidUsersException() throws UserNotFoundException, ItemNotFoundException, 
//    		LoginNotAvailableException, ProvinceNotFoundException, VoteDuplicateException, 
//    		InvalidVoteUsersException {
//    	
//        User user2 = userService.addUser("usuario", "password", "f", "l", "a", "password", "e", province.getProvinceName(), null);
//        User user3 = userService.addUser("u2", "password", "f", "l", "a", "password", "e", province.getProvinceName(), null);
//
//        userService.voteUser(user2.getUserId(), user.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//        userService.voteUser(user.getUserId(), user2.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//        userService.voteUser(user3.getUserId(), user2.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//
//    }
//    
//    @Test(expected = VoteDuplicateException.class)
//    public void testVoteDuplicate() throws UserNotFoundException, ItemNotFoundException, 
//    		LoginNotAvailableException, ProvinceNotFoundException, VoteDuplicateException, InvalidVoteUsersException {
//    	
//        User user2 = userService.addUser("usuario", "password", "f", "l", "a", "password", "e", province.getProvinceName(), null);
//
//        userService.voteUser(user2.getUserId(), user.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//        userService.voteUser(user.getUserId(), user2.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//        userService.voteUser(user.getUserId(), user2.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//
//        // Reability is 0.5f as user default value is 60
//        // 70 * Base * reability = 90
//        Assert.assertEquals(85, user.getUserValue());
//    }
//    
//    @Test
//    public void testVoteTwo() throws UserNotFoundException, ItemNotFoundException, 
//    		LoginNotAvailableException, ProvinceNotFoundException, VoteDuplicateException, 
//    		InvalidItemNameMinLenghtException, SubCategoryNotFoundException, InvalidVoteUsersException, IOException {
//    	
//        User user2 = userService.addUser("usuario", "password", "f", "l", "a", "password", "e", province.getProvinceName(), null);
//        
//        Item item2 = itemService.addItem(user2.getLogin(), subCategory.getSubCategoryName(), 
//        		"title", "description", province.getProvinceName(), 2, null, false, false, null);
//
//        userService.voteUser(user2.getUserId(), user.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//        userService.voteUser(user.getUserId(), user2.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//        
//        // Reability is 0.8f as user value is 85
//        // 70 + Base * reability = 90
//        Assert.assertEquals(85, user.getUserValue());
//        
//        /* SECOND VOTE */
//        
//        // this vote won count yet
//        userService.voteUser(user2.getUserId(), user.getUserId(), item2.getItemId(), VOTE_POSITIVE, "vote message");
//        Assert.assertEquals(85, user.getUserValue());
//        // now the second vote will make both count
//        userService.voteUser(user.getUserId(), user2.getUserId(), item2.getItemId(), VOTE_POSITIVE, "vote message");
//        
//        // Reability is 0.8f as user value is 85
//        // 85 + Base * reability = 104 (will save it as 100)
//        Assert.assertEquals(100, user.getUserValue());
//        Assert.assertEquals(100, user2.getUserValue());
//    }
//    
//    @Test
//    public void testVoteThree() throws UserNotFoundException, ItemNotFoundException, 
//    		LoginNotAvailableException, ProvinceNotFoundException, VoteDuplicateException, 
//    		InvalidItemNameMinLenghtException, SubCategoryNotFoundException, InvalidVoteUsersException, IOException {
//    	
//        User user2 = userService.addUser("u2", "password", "f", "l", "a", "password", "e", province.getProvinceName(), null);
//        User user3 = userService.addUser("u3", "password", "f", "l", "a", "password", "e", province.getProvinceName(), null);
//        
//        Item item2 = itemService.addItem(user2.getLogin(), subCategory.getSubCategoryName(), 
//        		"title", "description", province.getProvinceName(), 2, null, false, false, null);
//        Item item3 = itemService.addItem(user.getLogin(), subCategory.getSubCategoryName(), 
//        		"title", "description", province.getProvinceName(), 2, null, false, false, null);
//        Item item4 = itemService.addItem(user.getLogin(), subCategory.getSubCategoryName(), 
//        		"title", "description", province.getProvinceName(), 2, null, false, false, null);
//
//        userService.voteUser(user2.getUserId(), user.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//        userService.voteUser(user.getUserId(), user2.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//
//        Assert.assertEquals(85, user.getUserValue());// 70 + Base * 0.8f = 90
//        
//        /* SECOND VOTE */
//        userService.voteUser(user2.getUserId(), user.getUserId(), item2.getItemId(), VOTE_POSITIVE, "vote message");
//        Assert.assertEquals(85, user.getUserValue());
//        userService.voteUser(user.getUserId(), user2.getUserId(), item2.getItemId(), VOTE_POSITIVE, "vote message");
//        Assert.assertEquals(100, user.getUserValue()); // 85 + Base * 0.8f = 104 (will save it as 100)
//        Assert.assertEquals(100, user2.getUserValue());
//        
//        /* THIRD VOTE */ 
//        // 100 + -30 * 0.5f
//        userService.voteUser(user3.getUserId(), user.getUserId(), item4.getItemId(), VOTE_NEGATIVE, "message");
//        Assert.assertEquals(100, user.getUserValue());  // nothing happened yet
//        // 100 + -30 * 1.5f
//        userService.voteUser(user2.getUserId(), user.getUserId(), item3.getItemId(), VOTE_NEGATIVE, "message");
//        Assert.assertEquals(55, user.getUserValue()); // users with value >75 can vote
//        
//        // user votes user3 and unluckily "eats" the negative vote by him.
//        Assert.assertEquals(70, user3.getUserValue()); // 70 + 30 * 0.1f
//        userService.voteUser(user.getUserId(), user3.getUserId(), item4.getItemId(), VOTE_POSITIVE, "message");
//        Assert.assertEquals(73, user3.getUserValue()); // 70 + 30 * 0.1f
//        Assert.assertEquals(40, user.getUserValue()); // 55 + -30 * 0.5f
//    }
//    
//    @Test
//    public void testVoteNegativeTwo() throws UserNotFoundException, ItemNotFoundException, 
//    		LoginNotAvailableException, ProvinceNotFoundException, VoteDuplicateException, 
//    		InvalidItemNameMinLenghtException, SubCategoryNotFoundException, 
//    		InvalidVoteUsersException, IOException {
//    	
//        User user2 = userService.addUser("usuario", "password", "f", "l", "a", "password", "e", province.getProvinceName(), null);
//        
//        Item item2 = itemService.addItem(user2.getLogin(), subCategory.getSubCategoryName(), 
//        		"title", "description", province.getProvinceName(), 2, null, false, false, null);
//
//        userService.voteUser(user2.getUserId(), user.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//        userService.voteUser(user.getUserId(), user2.getUserId(), item.getItemId(), VOTE_POSITIVE, "vote message");
//        
//        // Reability is 0.8f as user value is 85
//        // 70 + Base * reability = 90
//        Assert.assertEquals(85, user.getUserValue());
//        Assert.assertEquals(85, user2.getUserValue());
//        
//        /* SECOND VOTE */
//        
//        // this vote won count yet
//        userService.voteUser(user2.getUserId(), user.getUserId(), item2.getItemId(), VOTE_POSITIVE, "vote message");
//        Assert.assertEquals(85, user.getUserValue());
//        Assert.assertEquals(85, user2.getUserValue());
//        // now the second vote will make both count
//        userService.voteUser(user.getUserId(), user2.getUserId(), item2.getItemId(), VOTE_NEGATIVE, "vote message");
//        
//        // Reability is 0.8f as user value is 85
//        // 85 + Base * reability = 104 (will save it as 100)
//        Assert.assertEquals(100, user.getUserValue());
//        Assert.assertEquals(61, user2.getUserValue()); // 85 - Base * 0,8(reability)
//    }
//}