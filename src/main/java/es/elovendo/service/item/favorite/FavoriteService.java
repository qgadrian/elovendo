package es.elovendo.service.item.favorite;

import java.util.List;

import es.elovendo.model.item.Item;
import es.elovendo.model.item.favorite.Favorite;
import es.elovendo.model.user.User;
import es.elovendo.rest.exception.ItemNotFoundException;

public interface FavoriteService {
	
	public boolean toggleFavorite(User user, Long itemId) throws ItemNotFoundException;
	
	/**
	 * Sets an item as favorite for the given user
	 * @param user
	 * @param itemId
	 * @return
	 * @throws ItemNotFoundException
	 */
	public Favorite setFavorite(User user, Long itemId) throws ItemNotFoundException;
	
	/**
	 * Removes as favorite an item for the given user favorite's list
	 * @param user
	 * @param itemId
	 * @return
	 * @throws ItemNotFoundException
	 */
	public void unsetFavorite(User user, Long itemId) throws ItemNotFoundException;
	
	/**
	 * Returns {@link Constant}.MAX_LAST_FAVORITES items from the given user 
	 * @param user User to obtain last favorites
	 * @return
	 */
	public List<Item> getLastFavs(User user);

//	public boolean removeFavorite(User user, Long itemId) throws ItemNotFoundException;
//	
//	public boolean isFavorite(User user, Long itemId) throws ItemNotFoundException;
}
