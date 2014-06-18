package es.telocompro.model.item.category.subcategory;

import es.telocompro.model.item.category.Category;
import es.telocompro.model.user.User;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

/**
 * Created by @adrian on 18/06/14.
 * All rights reserved.
 */
@Entity
@Immutable
@Table(name = "subcategory")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subcategoryid")
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name = "categoryid")
    private Category category;

    @Column(name = "subcategoryname")
    private String subCategoryName;

    public SubCategory() {
    }

    public Long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }
}
