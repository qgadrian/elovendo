package es.telocompro.model.item;

import java.math.BigDecimal;

public class ItemForm {
	
	private String title, description, subCategory, youtubeVideo, latitude, longitude;
	private BigDecimal prize;
	public ItemForm(String title, String description, String subCategory,
			String youtubeVideo, String latitude, String longitude,
			BigDecimal prize) {
		this.title = title;
		this.description = description;
		this.subCategory = subCategory;
		this.youtubeVideo = youtubeVideo;
		this.latitude = latitude;
		this.longitude = longitude;
		this.prize = prize;
	}
	
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public String getYoutubeVideo() {
		return youtubeVideo;
	}
	public String getLatitude() {
		return latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public BigDecimal getPrize() {
		return prize;
	}
	
	public Item getItemObject() {
		Item item = new Item();
		item.setTitle(title);
		item.setDescription(description);
		item.setPrize(prize);
		item.setYoutubeVideo(youtubeVideo);
		return item;
	}
	
}
