package es.telocompro.service.item.favorite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.favorite.Favorite;
import es.telocompro.model.item.favorite.FavoriteKey;
import es.telocompro.model.item.favorite.FavoriteRepository;
import es.telocompro.model.user.User;
import es.telocompro.rest.controller.exception.ItemNotFoundException;
import es.telocompro.service.item.ItemService;

@Service("favoriteService")
public class FavoriteServiceImpl implements FavoriteService {
	
	@Autowired
	private FavoriteRepository favRepository;
	@Autowired
	private ItemService itemService;

	@Override
	public boolean toggleFavorite(User user, Long itemId) throws ItemNotFoundException {
		Item item = getItem(itemId);
		FavoriteKey favKey = new FavoriteKey(user, item);
		
		if (favRepository.findOne(favKey) == null) {
			Favorite favorite = new Favorite(user.getUserId(), item.getItemId());
			return favRepository.save(favorite) != null;
		} else {
			favRepository.delete(favKey);
			return false;
		}
	}

//	@Override
//	public boolean removeFavorite(User user, Long itemId) throws ItemNotFoundException {
//		Item item = getItem(itemId);
//		FavoriteKey favKey = new FavoriteKey(user, item);
//		
//		if (favRepository.findOne(favKey) == null) return false;
//		else {
//			favRepository.delete(favKey);
//			return true;
//		}
//	}
//
//	@Override
//	public boolean isFavorite(User user, Long itemId) throws ItemNotFoundException {
//		Item item = getItem(itemId);
//		FavoriteKey favKey = new FavoriteKey(user, item);
//		
//		return favRepository.findOne(favKey) != null;
//	}
	
	private Item getItem(Long itemId) throws ItemNotFoundException {
		Item item = itemService.getItemById(itemId);
		if (item == null) throw new ItemNotFoundException(itemId);
		return item;
	}

}
