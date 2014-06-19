package es.telocompro.model.item.category;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */
@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-config-test.xml")
//@ContextConfiguration(classes = {BeanConfTest.class})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testGetAllCategories() {
        int numberOfCategories = 0;
        for (@SuppressWarnings("unused") Category category : categoryRepository.findAll()) {
            numberOfCategories++;
        }

        // Total categories inserted are 10
        Assert.assertEquals(10, numberOfCategories);

    }
}
