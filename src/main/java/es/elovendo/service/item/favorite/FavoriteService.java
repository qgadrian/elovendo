package es.elovendo.service.item.favorite;

import java.util.List;

import es.elovendo.model.item.Item;
import es.elovendo.model.item.favorite.Favorite;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.ItemNotFoundException;

public interface FavoriteService {
	
	public boolean toggleFavorite(User user, Long itemId) throws ItemNotFoundException;
	
	public Favorite setFavorite(User user, Long itemId) throws ItemNotFoundException;
	
	public List<Item> getLastFavs(User user);

//	public boolean removeFavorite(User user, Long itemId) throws ItemNotFoundException;
//	
//	public boolean isFavorite(User user, Long itemId) throws ItemNotFoundException;
}
