package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class FilterStateCityData {
	
	@Id
	private Long id;
	
	@Column(name="state_id")
	private Integer stateId;
	
	@Column(name="state_name")
	private String stateName;
	
	@Column(name="city_id")
	private Integer cityId;
	
	@Column(name="city_name")
	private String cityName;
	
	

}
