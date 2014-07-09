package es.telocompro.rest.controller.exception;


@SuppressWarnings("serial")
public class ProvinceNotFoundException extends Exception {

	private String provinceName;
	
	public ProvinceNotFoundException(String provinceName) {
        
        super(provinceName + " not found");
        this.provinceName = provinceName;
    }

	public String getProvinceName() {
		return provinceName;
	}
	
}
