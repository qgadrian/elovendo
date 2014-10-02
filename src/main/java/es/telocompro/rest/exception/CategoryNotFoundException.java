package es.telocompro.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="SubCategories Not Found")
public class CategoryNotFoundException extends Exception {

	private String categoryName;
	
	public CategoryNotFoundException(String categoryName) {
        
        super("No categories found for => "
        		+ categoryName);
        this.categoryName = categoryName;
    }
	
	public CategoryNotFoundException(long categoryId) {
        
        super("No categories found for => "
        		+ categoryId);
        this.categoryName = String.valueOf(categoryId);
    }

	public String getCategoryName() {
		return categoryName;
	}
	
}
