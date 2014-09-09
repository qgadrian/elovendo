package es.telocompro.model.item.favorite;

import java.io.Serializable;

import es.telocompro.model.item.Item;
import es.telocompro.model.user.User;

public class FavoriteKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private Long itemId;
	
	public FavoriteKey() {
	}

	public FavoriteKey(Long userId, Long itemId) {
		this.userId = userId;
		this.itemId = itemId;
	}
	
	public FavoriteKey(User user, Item item) {
		this.userId = user.getUserId();
		this.itemId = item.getItemId();
	}

	public Long getUserId() {
		return userId;
	}

	public Long getItemId() {
		return itemId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FavoriteKey) {
			FavoriteKey fav = (FavoriteKey) obj;
			return this.userId.equals(fav.getUserId()) && this.itemId.equals(fav.getItemId());
		}
		else return false;
	}

}
