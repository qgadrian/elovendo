package es.telocompro.model.item;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by @adrian on 17/06/14.
 * All rights reserved.
 */

@Entity
@Table(name = "item")
@SuppressWarnings("unused")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "itemid")
    private Long itemId;

    //    private Category category;
    //    private Subcategory subcategory;
    private String title;
    private String description;
    private BigDecimal price;

    @Column(columnDefinition="DATETIME", name = "startdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startDate;

    public Item() {
    }

    public Item(String title, String description, BigDecimal price, Calendar startDate) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.startDate = startDate;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }
}
