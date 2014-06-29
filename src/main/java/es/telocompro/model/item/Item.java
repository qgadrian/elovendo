package es.telocompro.model.item;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.user.User;

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

    private String title;
    private String description;
    private BigDecimal prize;

    @Column(columnDefinition="DATETIME", name = "startdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startDate;
    
    @Column(name="imghome")
    @Lob
    @Basic(fetch=FetchType.LAZY)
    private String imgHome;

    public Item() {
    }

    public Item(User user, SubCategory subCategory, String title, String description, 
    		BigDecimal prize, Calendar startDate, String imgHome) {
        this.user = user;
        this.subCategory = subCategory;
        this.title = title;
        this.description = description;
        this.prize = prize;
        this.startDate = startDate;
        this.imgHome = imgHome;
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
		return imgHome;
	}

	public void setImgHome(String imgHome) {
		this.imgHome = imgHome;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

}
