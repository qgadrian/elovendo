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

import org.hibernate.validator.constraints.Length;

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
    
    @Length(min = 10, max = 500)
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
    
    private String imgHome;
    @Transient
    private String imgHome200h;
    
    // Premium features
    private boolean featured;
    private boolean highlight;

    public Item() { }

    public Item(User user, SubCategory subCategory, String title, String description, 
    		Province province, BigDecimal prize, Calendar startDate, String imgHome, 
    		boolean featured, boolean highlight) {
        this.user = user;
        this.subCategory = subCategory;
        this.title = title;
        this.description = description;
        this.prize = prize;
        this.startDate = startDate;
        this.imgHome = imgHome;
        this.province = province;
        
        Calendar endDate = Calendar.getInstance();
        endDate.set(startDate.get(Calendar.YEAR), 
        		startDate.get(Calendar.MONTH), 
        		startDate.get(Calendar.DATE),
        		startDate.get(Calendar.HOUR_OF_DAY), 
        		startDate.get(Calendar.MINUTE),
        		startDate.get(Calendar.SECOND));
        
        endDate.add(Calendar.DATE, Constant.ITEM_DEFAULT_DURATION);
        
        this.endDate = endDate;
        
        this.featured = featured;
        this.highlight = highlight;
    }
    
    public String getImgHome200h() {
    	return this.imgHome.concat("-200h.jpg");
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

	public String getImgHome() {
		return imgHome.concat(".jpg");
	}

	public void setImgHome(String imgHome) {
		this.imgHome = imgHome;
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

}
