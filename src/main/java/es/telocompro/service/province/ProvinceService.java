package es.telocompro.service.province;

import es.telocompro.model.province.Province;

public interface ProvinceService {
	
	public Province findProvinceByName(String provinceName);
	
	public Iterable<Province> findAllProvinces();

}
