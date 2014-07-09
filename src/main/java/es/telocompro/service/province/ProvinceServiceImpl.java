package es.telocompro.service.province;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.telocompro.model.province.Province;
import es.telocompro.model.province.ProvinceRepository;

@Service("provinceService")
public class ProvinceServiceImpl implements ProvinceService {
	
	@Autowired
	private ProvinceRepository provinceRepostory;

	@Override
	public Province findProvinceByName(String provinceName) {
		return provinceRepostory.findByProvinceName(provinceName);
	}

	@Override
	public Iterable<Province> findAllProvinces() {
		return provinceRepostory.findAll();
	}

}
