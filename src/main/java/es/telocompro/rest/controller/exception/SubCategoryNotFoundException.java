package es.telocompro.rest.controller.exception;

@SuppressWarnings("serial")
public class SubCategoryNotFoundException extends Exception {
	
	public SubCategoryNotFoundException(String subCategoryName) {
		super("user " + subCategoryName + " not found");
	}
	
}
