package es.telocompro.service.item;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.ItemRepository;
import es.telocompro.model.user.User;
import es.telocompro.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */

@SuppressWarnings(value = "unused")
@Service("itemService")
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Item addItem(Long userId, String title, String description, double prize) {
        User user = userRepository.findOne(userId);
        Item item = new Item(user, title, description, new BigDecimal(prize), Calendar.getInstance());
        return itemRepository.save(item);
    }

    @Override
    public Iterable<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getAllItemsByUserId(Long userId) {
        return itemRepository.findByUserId(userId);
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.findOne(id);
    }

    @Override
    public Iterable<Item> getItemByTitle(String title) {
        return itemRepository.findByTitle(title);
    }

    @Override
    public Item updateItem(Long itemId, String title, String description, double prize) {
        Item item = itemRepository.findOne(itemId);
        item.setTitle(title);
        item.setDescription(description);
        item.setPrize(new BigDecimal(prize));
        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.delete(itemId);
    }
}
