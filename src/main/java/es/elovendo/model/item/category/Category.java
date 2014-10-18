package es.elovendo.model.item.category;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */

@Entity
@Immutable
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "categoryid")
    private Long categoryId;

    @Column(name = "categoryname")
    private String categoryName;

    public Category() { }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
