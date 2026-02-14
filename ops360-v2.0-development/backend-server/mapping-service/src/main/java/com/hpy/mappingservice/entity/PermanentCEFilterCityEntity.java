package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class PermanentCEFilterCityEntity {
	@Id
    private Long srno;
	
//	@Column(name="city_id")
//    private String cityId;

	@Column(name="city_name")
    private String cityName;
    
    private String count;
}
