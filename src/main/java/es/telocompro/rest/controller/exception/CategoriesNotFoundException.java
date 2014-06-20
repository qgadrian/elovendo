package es.telocompro.rest.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="SubCategories Not Found")
public class CategoriesNotFoundException extends Exception {

	private String categoryName;
	
	public CategoriesNotFoundException(String categoryName) {
        
        super("No ctegories found for => "
        		+ categoryName);
        this.categoryName = categoryName;
    }

	public String getCategoryName() {
		return categoryName;
	}
	
}
