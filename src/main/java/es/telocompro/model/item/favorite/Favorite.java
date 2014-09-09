package es.telocompro.model.item.favorite;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.telocompro.model.item.Item;
import es.telocompro.model.user.User;

@Entity
@Table(name = "favorite")
@IdClass(FavoriteKey.class)
public class Favorite {

	@Id
	private Long userId;
	
	@Id
	private Long itemId;
	
	@Column(columnDefinition="DATETIME", name = "favDate")
    @Temporal(TemporalType.TIMESTAMP)
	private Calendar favDate;

	public Favorite() {
	}

	public Favorite(Long userId, Long itemId) {
		this.userId = userId;
		this.itemId = itemId;
		this.favDate = Calendar.getInstance();
	}

	public Long getUserId() {
		return userId;
	}

	public Long getItemId() {
		return itemId;
	}

	public Calendar getFavDate() {
		return favDate;
	}

}
