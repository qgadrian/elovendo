package es.elovendo.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="SubCategories Not Found")
public class WrongItemSubCategoryRequestException extends Exception {

	private String subCategoryName;
	private long itemId;
	
	public WrongItemSubCategoryRequestException(String subCategoryName,
			long itemId) {
        
        super("Wron subcategory " + subCategoryName + " for item => "
        		+ itemId);
        this.subCategoryName = subCategoryName;
        this.itemId = itemId;
    }

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public long getItemId() {
		return itemId;
	}
	
}
