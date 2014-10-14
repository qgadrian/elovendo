package es.elovendo.rest.exception;

@SuppressWarnings("serial")
public class NotUserItemException extends Exception {
	
	public NotUserItemException(long itemId, long userId) {
        super("user " + userId + " tried to edit item " + itemId + " without owning it.");
    }
	
}
