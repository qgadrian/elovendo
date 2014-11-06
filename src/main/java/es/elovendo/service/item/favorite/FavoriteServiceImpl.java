package es.elovendo.service.item.favorite;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import es.elovendo.model.item.Item;
import es.elovendo.model.item.favorite.Favorite;
import es.elovendo.model.item.favorite.FavoriteKey;
import es.elovendo.model.item.favorite.FavoriteRepository;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.ItemNotFoundException;
import es.elovendo.service.item.ItemService;
import es.elovendo.util.Constant;

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
	
	@Override
	public Favorite setFavorite(User user, Long itemId) throws ItemNotFoundException {
		Item item = getItem(itemId);
		Favorite favorite = new Favorite(user.getUserId(), item.getItemId());
		return favRepository.save(favorite);
	}

	@Override
	public void unsetFavorite(User user, Long itemId) throws ItemNotFoundException {
		Item item = getItem(itemId);
		FavoriteKey favKey = new FavoriteKey(user, item);
		
		if (!favRepository.exists(favKey)) throw new ItemNotFoundException(itemId);
		
		favRepository.delete(favKey);
	}
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Item> getLastFavs(User user) {
		PageRequest request = new PageRequest(0, Constant.MAX_LAST_FAVORITES);
		List <Favorite> favs = favRepository.findLastFav(user.getUserId(), request).getContent();
		List<Long> favsIds = new ArrayList<>(); 
		
		for (Favorite fav : favs) {
			favsIds.add(fav.getItemId());
		}
		
		return IteratorUtils.toList(itemService.getAll(favsIds).iterator());
	}

}
