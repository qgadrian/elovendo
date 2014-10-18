package es.elovendo.rest.exception;

@SuppressWarnings("serial")
public class SubCategoryNotFoundException extends Exception {
	
	public SubCategoryNotFoundException(long subCategoryId) {
		super("subCategory " + subCategoryId + " not found");
	}
	
	public SubCategoryNotFoundException(String subCategoryName) {
		super("subCategory " + subCategoryName + " not found");
	}
	
}
