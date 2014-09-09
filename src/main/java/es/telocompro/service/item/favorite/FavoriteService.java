package es.telocompro.service.item.favorite;

import es.telocompro.model.user.User;
import es.telocompro.rest.controller.exception.ItemNotFoundException;

public interface FavoriteService {
	
	public boolean toggleFavorite(User user, Long itemId) throws ItemNotFoundException;

//	public boolean removeFavorite(User user, Long itemId) throws ItemNotFoundException;
//	
//	public boolean isFavorite(User user, Long itemId) throws ItemNotFoundException;
}
