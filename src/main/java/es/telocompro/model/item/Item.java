package es.telocompro.model.item;

import java.math.BigDecimal;
import java.util.Calendar;

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
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;
import org.joda.time.Days;

import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.province.Province;
import es.telocompro.model.user.User;
import es.telocompro.util.Constant;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "itemid")
    private Long itemId;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name = "subcategoryid")
    private SubCategory subCategory;

    @Length(min = 5, max = 40)
    private String title;
    
    @Length(max = 1000)
    private String description;
    
    private BigDecimal prize;

    @Column(columnDefinition="DATETIME", name = "startdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startDate;
    
    @Column(columnDefinition="DATETIME", name = "endDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar endDate;
    
    @ManyToOne(optional = false, fetch=FetchType.LAZY)
    @JoinColumn(name = "provinceId")
    private Province province;
    
    private String mainImage;
    @Transient
    private String mainImage200h;
    
    private String image1;
    private String image2;
    private String image3;
    
    @Pattern(regexp=Constant.YOUTUBE_URL_PATTERN)
    private String youtubeVideo;
    
    // Premium features
    private boolean featured;
    private boolean highlight;
    
    // Geolocation
//    @NotNull
    private String latitude;
//    @NotNull
    private String longitude;
    private float cosRadLat;
    private float radLng;
    private float sinRadLat;

    public Item() { }

    public Item(User user, SubCategory subCategory, String title, String description, 
    		Province province, BigDecimal prize, String mainImage,
    		String image1, String image2, String image3, String youtubeVideo, boolean featured, 
    		boolean highlight, String latitude, String longitude) {
    	
        this.user = user;
        this.subCategory = subCategory;
        this.title = title;
        this.description = description;
        this.prize = prize;
        this.startDate = Calendar.getInstance();
        this.mainImage = mainImage;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.youtubeVideo = youtubeVideo;
        this.province = province;
        
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, Constant.ITEM_DEFAULT_DURATION);
        
        this.endDate = endDate;
        
        this.featured = featured;
        this.highlight = highlight;
        this.latitude = latitude;
        this.longitude = longitude;
        
        double latRad = Math.toRadians(Double.parseDouble(latitude));
        this.cosRadLat = (float) Math.cos(latRad);
        this.sinRadLat = (float) Math.sin(latRad);
		this.radLng = (float) Math.toRadians(Double.parseDouble(longitude));
    }
    
    @Transient
    public String getPlainDescription() {
    	String plainText = this.description.replaceAll("(<br\\ ?/>)+", "&");
    	return plainText.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", "");
    }
    
    @Transient
    public boolean isNew() {
    	DateTime s = new DateTime(this.startDate.getTimeInMillis());
    	DateTime e = new DateTime(Calendar.getInstance().getTimeInMillis());
    	Days days = Days.daysBetween(s, e);
    	return days.getDays()< 5;
    }
    
    public String getMainImage200h() {
    	return this.mainImage.concat("-200h.jpg");
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

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

	public String getMainImage() {
		return mainImage.concat(".jpg");
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

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

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
		double latRad = Math.toRadians(Double.parseDouble(latitude));
        this.cosRadLat = (float) Math.cos(latRad);
        this.sinRadLat = (float) Math.sin(latRad);
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
		this.radLng = (float) Math.toRadians(Double.parseDouble(longitude));
	}

	public String getImage1() {
		return image1;
	}

	public String getImage2() {
		return image2;
	}

	public String getImage3() {
		return image3;
	}

	public String getYoutubeVideo() {
		return youtubeVideo;
	}

	public void setYoutubeVideo(String youtubeVideo) {
		this.youtubeVideo = youtubeVideo;
	}

	public float getCosRadLat() {
		return cosRadLat;
	}

	public float getRadLng() {
		return radLng;
	}

	public float getSinRadLat() {
		return sinRadLat;
	}

}
