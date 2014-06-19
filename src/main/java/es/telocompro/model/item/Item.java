package es.telocompro.model.item;

import es.telocompro.model.item.category.subcategory.SubCategory;
import es.telocompro.model.user.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

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

    public Item() {
    }

    public Item(User user, SubCategory subCategory, String title, String description, BigDecimal prize, Calendar startDate) {
        this.user = user;
        this.subCategory = subCategory;
        this.title = title;
        this.description = description;
        this.prize = prize;
        this.startDate = startDate;
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

}
