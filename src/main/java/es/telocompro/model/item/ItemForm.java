package es.telocompro.model.item;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import es.telocompro.model.user.User;
import es.telocompro.util.Constant;

public class ItemForm {
	
	private Long itemId;
	private User user;
	
	@Length(min = 5, max = 60)
	@NotNull
	private String title;
	
	@Length(max = 1000)
	private String description;
	
	@NotNull 
	@NumberFormat(style = Style.NUMBER) 
	@Min(0)
	private BigDecimal prize;
	
	@NotNull
	@Min(1)
	private long subCategory;
	
	private long category;
	
    @Pattern(regexp=Constant.YOUTUBE_URL_PATTERN)
	private String youtubeVideo;
    
    @NotNull
	private double latitude;
    
    @NotNull
	private double longitude;
    
    private boolean featured;
    private boolean highlight;
    private boolean autoRenew;
    
    public ItemForm() {}
	
	public ItemForm(long itemId, User user, String title, String description, BigDecimal prize,
			long subCategory, long category, String youtubeVideo, double latitude,
			double longitude, boolean featured, boolean highlight,
			boolean autoRenew) {
		this.itemId = itemId;
		this.user = user;
		this.title = title;
		this.description = description;
		this.prize = prize;
		this.subCategory = subCategory;
		this.category = category;
		this.youtubeVideo = youtubeVideo;
		this.latitude = latitude;
		this.longitude = longitude;
		this.featured = featured;
		this.highlight = highlight;
		this.autoRenew = autoRenew;
	}

	public ItemForm(Item item) {
		this.itemId = item.getItemId();
		this.user = item.getUser();
		this.title = item.getTitle();
		this.description = item.getDescription();
		this.prize = item.getPrize();
		this.subCategory = item.getSubCategory().getId();
		this.category = item.getSubCategory().getCategory().getCategoryId();
		this.youtubeVideo = item.getYoutubeVideo();
		this.latitude = item.getLatitude();
		this.longitude = item.getLongitude();
		this.featured = item.isFeatured();
		this.highlight = item.isHighlight();
		this.autoRenew = item.isAutoRenew();
	}
	
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public long getSubCategory() {
		return subCategory;
	}
	public String getYoutubeVideo() {
		return youtubeVideo;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public BigDecimal getPrize() {
		return prize;
	}
	
	public long getCategory() {
		return category;
	}

	public void setCategory(long category) {
		this.category = category;
	}

	public Item getItemObject() {
		Item item = new Item();
		item.setTitle(title);
		item.setDescription(description);
		item.setPrize(prize);
		item.setYoutubeVideo(youtubeVideo);
		return item;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrize(BigDecimal prize) {
		this.prize = prize;
	}

	public void setSubCategory(long subCategory) {
		this.subCategory = subCategory;
	}

	public void setYoutubeVideo(String youtubeVideo) {
		this.youtubeVideo = youtubeVideo;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public boolean isFeatured() {
		return featured;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

	public boolean isHighlight() {
		return highlight;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

	public boolean isAutoRenew() {
		return autoRenew;
	}

	public void setAutoRenew(boolean autoRenew) {
		this.autoRenew = autoRenew;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
