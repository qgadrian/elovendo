package es.telocompro.rest.exception;

@SuppressWarnings("serial")
public class SubCategoryNotFoundException extends Exception {
	
	public SubCategoryNotFoundException(long subCategoryId) {
		super("subCategory " + subCategoryId + " not found");
	}
	
}
