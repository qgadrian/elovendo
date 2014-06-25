package es.telocompro.rest.util;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.telocompro.model.item.Item;
import es.telocompro.model.item.category.Category;
import es.telocompro.model.item.category.subcategory.SubCategory;

public class RestItemObject {
	
	private Item item;
	private String username;
	private String category;
	private String subCategory;
	
	
	public RestItemObject(Item item, String username, String category,
			String subCategory) {
		super();
		this.item = item;
		this.username = username;
		this.category = category;
		this.subCategory = subCategory;
	}


	public Item getItem() {
		return item;
	}


	public String getUsername() {
		return username;
	}


	public String getCategory() {
		return category;
	}


	public String getSubCategory() {
		return subCategory;
	}
	
	

}
