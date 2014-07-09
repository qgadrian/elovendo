package es.telocompro.service.exception;


@SuppressWarnings("serial")
public class InvalidItemNameMinLenghtException extends Exception {

	
	public InvalidItemNameMinLenghtException(String itemName) {
        super("Wrong name lenght " + itemName);
    }
	
}
