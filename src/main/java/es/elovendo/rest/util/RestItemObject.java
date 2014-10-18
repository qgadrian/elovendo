package es.elovendo.rest.util;

import es.elovendo.model.item.Item;

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
