package es.telocompro.service.item.favorite;

import java.util.List;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.favorite.Favorite;
import es.telocompro.model.user.User;
import es.telocompro.rest.exception.ItemNotFoundException;

public interface FavoriteService {
	
	public boolean toggleFavorite(User user, Long itemId) throws ItemNotFoundException;
	
	public Favorite setFavorite(User user, Long itemId) throws ItemNotFoundException;
	
	public List<Item> getLastFavs(User user);

//	public boolean removeFavorite(User user, Long itemId) throws ItemNotFoundException;
//	
//	public boolean isFavorite(User user, Long itemId) throws ItemNotFoundException;
}
