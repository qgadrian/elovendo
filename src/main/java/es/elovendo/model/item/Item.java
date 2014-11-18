package es.elovendo.model.item;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.joda.time.DateTime;
import org.joda.time.Days;

import es.elovendo.model.item.category.subcategory.SubCategory;
import es.elovendo.model.user.User;
import es.elovendo.util.Constant;
import es.elovendo.util.LocaleHelper;
import es.elovendo.util.currency.CurrencyLocaler;
import es.elovendo.util.currency.NoFixLocaleFoundException;

/**
 * Created by @adrian on 17/06/14. All rights reserved.
 */

@Entity
@Table(name = "item")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "itemid")
	private Long itemId;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "subcategoryid")
	private SubCategory subCategory;

	// @Length(min = 5, max = 60)
	private String title;

	// @Length(max = 1000)
	private String description;

	private String currency;
	private BigDecimal prize;

	@Column(columnDefinition = "DATETIME", name = "startdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startDate;

	@Column(columnDefinition = "DATETIME", name = "endDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endDate;

	private String mainImage;

	@Transient
	private String distance;
	@Transient
	private String currencySymbol;

	private String image1;
	private String image2;
	private String image3;

	// @Pattern(regexp=Constant.YOUTUBE_URL_PATTERN)
	private String youtubeVideo;

	// Premium features
	private boolean featured;
	private boolean highlight;
	private boolean autoRenew;

	// Geolocation
	// @NotNull
	private double latitude;
	// @NotNull
	private double longitude;
	private double cosRadLat;
	private double radLng;
	private double sinRadLat;

	// FIXME
	// @Version
	// private long version = 0;

	public Item() {
	}

	public Item(User user, SubCategory subCategory, String title, String description, String currency,
			BigDecimal prize, String mainImage, String image1, String image2, String image3, String youtubeVideo,
			boolean featured, boolean highlight, boolean autoRenew, double latitude, double longitude) {

		this.user = user;
		this.subCategory = subCategory;
		this.title = title;
		this.description = description;
		this.currency = currency;
		this.prize = prize;
		this.startDate = Calendar.getInstance();
		this.mainImage = mainImage;
		this.image1 = image1;
		this.image2 = image2;
		this.image3 = image3;
		this.youtubeVideo = youtubeVideo;

		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DATE, Constant.ITEM_DEFAULT_DURATION);

		this.endDate = endDate;

		this.featured = featured;
		this.highlight = highlight;
		this.autoRenew = autoRenew;
		this.latitude = latitude;
		this.longitude = longitude;

		double latRad = Math.toRadians(latitude);
		this.cosRadLat = Math.cos(latRad);
		this.sinRadLat = Math.sin(latRad);
		this.radLng = Math.toRadians(longitude);
	}

	public Item(User user, SubCategory subCategory, String title, String description, String currency,
			BigDecimal prize, String youtubeVideo, boolean featured, boolean highlight, boolean autoRenew,
			double latitude, double longitude) {

		this.user = user;
		this.subCategory = subCategory;
		this.title = title;
		this.description = description;
		this.currency = currency;
		this.prize = prize;
		this.startDate = Calendar.getInstance();
		this.youtubeVideo = youtubeVideo;

		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DATE, Constant.ITEM_DEFAULT_DURATION);

		this.endDate = endDate;

		this.featured = featured;
		this.highlight = highlight;
		this.autoRenew = autoRenew;
		this.latitude = latitude;
		this.longitude = longitude;

		double latRad = Math.toRadians(latitude);
		this.cosRadLat = Math.cos(latRad);
		this.sinRadLat = Math.sin(latRad);
		this.radLng = Math.toRadians(longitude);
	}

	public Item(Item item, double distance) {
		this.itemId = item.getItemId();
		this.user = item.getUser();
		this.subCategory = item.getSubCategory();
		this.title = item.getTitle();
		this.description = item.getDescription();
		this.currency = item.getCurrency();
		this.prize = item.getPrize();

		this.startDate = item.startDate;
		this.endDate = item.getEndDate();

		this.mainImage = item.getMainImageName();

		this.distance = new DecimalFormat("##.##").format(distance);

		this.image1 = item.getImage1();
		this.image2 = item.getImage2();
		this.image3 = item.getImage3();
		this.youtubeVideo = item.getYoutubeVideo();
		this.featured = item.isFeatured();
		this.highlight = item.isHighlight();
		this.latitude = item.getLatitude();
		this.longitude = item.getLongitude();

		this.cosRadLat = item.getCosRadLat();
		this.sinRadLat = item.getSinRadLat();
		this.radLng = item.getRadLng();
	}

	public String getDistance() {
		// return new DecimalFormat("##.##").format(distance);ç
		return distance;
	}

	// public void setDistance(double distance) {
	// this.distance = distance;
	// }

	@Transient
	public String getPlainDescription() {
		String plainText = this.description.replaceAll("(<br\\ ?/>)+", "");
		return plainText.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", "");
	}

	@Transient
	public boolean isNew() {
		DateTime s = new DateTime(this.startDate.getTimeInMillis());
		DateTime e = new DateTime(Calendar.getInstance().getTimeInMillis());
		Days days = Days.daysBetween(s, e);
		return days.getDays() < 5;
	}

	@Transient
	public String getMainImage200h() {
		return this.mainImage.concat("-200h.jpg");
	}
	
	@Transient
	public List<String> getAllImages200h() {
		List<String> images = new ArrayList<>();
		
		if (this.image1 != null) images.add(this.image1.concat("-200h.jpg"));
		if (this.image2 != null) images.add(this.image2.concat("-200h.jpg"));
		if (this.image3 != null) images.add(this.image3.concat("-200h.jpg"));
		
		return images;
	}
	
	@Transient
	public List<String> getAllImages() {
		List<String> images = new ArrayList<>();
		
		if (this.image1 != null) images.add(this.image1.concat(".jpg"));
		if (this.image2 != null) images.add(this.image2.concat(".jpg"));
		if (this.image3 != null) images.add(this.image3.concat(".jpg"));
		
		return images;
	}
	
	@Transient
	public boolean isOtherCurrency(Locale locale) {
		// Workaround for some language locale's without Country specified
		if (locale.getISO3Country().isEmpty()) locale = new Locale(locale.toString(), locale.toString());
		
		try {
			Currency currency = Currency.getInstance(this.currency);
			Currency localeCurrency = Currency.getInstance(locale);
			return !currency.getCurrencyCode().equalsIgnoreCase(localeCurrency.getCurrencyCode());
		} catch (IllegalArgumentException e) {
			LocaleHelper localeHelper = LocaleHelper.getInstance();
			try {
				locale = localeHelper.getFixedLocale(locale);
				Currency currency = Currency.getInstance(this.currency);
				Currency localeCurrency = Currency.getInstance(locale);
				return !currency.getCurrencyCode().equalsIgnoreCase(localeCurrency.getCurrencyCode());
			} catch (NoFixLocaleFoundException e1) {
				return true;
			}
		}
	}
	
	@Transient
	public String getCurrencyPrize(Locale locale) {
		// Workaround for some language locale's without Country specified
//		if (locale.getISO3Country().isEmpty()) locale = new Locale(locale.toString(), locale.toString());
//		
//		try {
//			CurrencyLocaler localer = CurrencyLocaler.getInstance();
//			Currency currency = Currency.getInstance(this.currency);
//			return localer.getFormattedCurrency(prize, locale, currency);
//			
//		} catch (Exception e) {
//			LocaleHelper localeHelper = LocaleHelper.getInstance();
//			try {
//				locale = localeHelper.getFixedLocale(locale);
//				CurrencyLocaler localer = CurrencyLocaler.getInstance();
//				Currency currency = Currency.getInstance(this.currency);
//				return localer.getFormattedCurrency(prize, locale, currency);
//			} catch (NoFixLocaleFoundException e1) {
//				return this.prize.toString();
//			}
//		}
		return CurrencyLocaler.getInstance().getCurrencyFromLocale(locale, this.currency, this.prize);
	}
	
	@Transient
	public String getExchangeCurrencyPrize(Locale locale) {
		// Workaround for some language locale's without Country specified
		if (locale.getISO3Country().isEmpty()) locale = new Locale(locale.toString(), locale.toString());
		
		try {
			// Try to set locale Currency with the workaround
			Currency fromCurrency = Currency.getInstance(this.currency);
			CurrencyLocaler localer = CurrencyLocaler.getInstance();
			Currency toCurrency = localer.getCurrencyLocaled(locale);
			return localer.getFormattedCurrency(prize, locale, fromCurrency, toCurrency);
			
		} catch (Exception e) {
			System.out.println("WARN!!! WARN!!! WARN!!! WARN!!! WARN!!!");
			System.out.println("WARN!!! WARN!!! WARN!!! WARN!!! WARN!!!");
			System.out.println("WARN!!! WARN!!! WARN!!! WARN!!! WARN!!!");
			System.out.println("Locale/Currency not supported/Whatever ERROR");
			System.out.println("Will now enter in a 'safe print mode'");
			System.out.println("Should print: Item currency, Item currency symbol, and prize formatted");
			System.out.println(e.getMessage());
			
			Currency currency = Currency.getInstance(this.currency);
			System.out.println("Item currency " + currency.getCurrencyCode());
			System.out.println("Item currency symbol with locale " + currency.getSymbol(locale));
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
			numberFormat.setCurrency(currency);
			System.out.println("Return prize formatted " + numberFormat.format(this.prize));
			
			return numberFormat.format(this.prize);
		}
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public SubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getItemId() {
		return itemId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrize() {
		return prize;
	}

	public void setPrize(BigDecimal prize) {
		this.prize = prize;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public String getMainImage() {
		return mainImage.concat(".jpg");
	}

	@Transient
	public String getMainImageName() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	// public Province getProvince() {
	// return province;
	// }
	//
	// public void setProvince(Province province) {
	// this.province = province;
	// }

	public Calendar getEndDate() {
		return endDate;
	}

	public boolean isFeatured() {
		return featured;
	}

	public boolean isHighlight() {
		return highlight;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
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

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
		double latRad = Math.toRadians(latitude);
		this.cosRadLat = Math.cos(latRad);
		this.sinRadLat = Math.sin(latRad);
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
		this.radLng = Math.toRadians(longitude);
	}

	public String getImage1() {
		return image1;
	}
	
	@Transient
	public String getImage1200h() {
		return image1 != null ? this.image1.concat("-200h.jpg") : null;
	}

	public String getImage2() {
		return image2;
	}
	
	@Transient
	public String getImage2200h() {
		return image2 != null ? this.image2.concat("-200h.jpg") : null;
	}

	public String getImage3() {
		return image3;
	}
	
	@Transient
	public String getImage3200h() {
		return image3 != null ? this.image3.concat("-200h.jpg") : null;
	}

	public String getYoutubeVideo() {
		return youtubeVideo;
	}

	public void setYoutubeVideo(String youtubeVideo) {
		this.youtubeVideo = youtubeVideo;
	}

	public double getCosRadLat() {
		return cosRadLat;
	}

	public double getRadLng() {
		return radLng;
	}

	public double getSinRadLat() {
		return sinRadLat;
	}

}
