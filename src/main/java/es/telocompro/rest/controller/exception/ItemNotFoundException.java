package es.telocompro.rest.controller.exception;

@SuppressWarnings("serial")
public class ItemNotFoundException extends Exception {
	
	public ItemNotFoundException(long itemId) {
        super("item " + itemId + " not found");
    }
	
}