package es.telocompro.rest.controller.exception;

@SuppressWarnings("serial")
public class PurchaseDuplicateException extends Exception {
	
	public PurchaseDuplicateException(String txn_id) {
        super("purchase with txn_id = " + txn_id + " already exists");
    }
	
}
