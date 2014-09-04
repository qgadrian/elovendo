package es.telocompro.rest.controller.exception;

@SuppressWarnings("serial")
public class SubCategoryNotFoundException extends Exception {
	
	public SubCategoryNotFoundException(long subCategoryId) {
		super("subCategory " + subCategoryId + " not found");
	}
	
}
