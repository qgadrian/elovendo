package es.telocompro.rest.controller.exception;

@SuppressWarnings("serial")
public class PurchaseNotFoundException extends Exception {
	
	public PurchaseNotFoundException(String txn_id) {
        super("purchase with txn_id = " + txn_id + " not found");
    }
	
}
