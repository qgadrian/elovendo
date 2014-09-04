//package es.telocompro.model.province;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import org.hibernate.annotations.Immutable;
//
//@Entity
//@Table(name = "province")
//@Immutable
//public class Province {
//	
//	@Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//	private Long provinceId;
//	
//	private String provinceName;
//
//	public Province() { }
//
//	public Province(String provinceName) {
//		super();
//		this.provinceName = provinceName;
//	}
//
//	public Long getProvinceId() {
//		return provinceId;
//	}
//
//	public String getProvinceName() {
//		return provinceName;
//	}
//
//}
