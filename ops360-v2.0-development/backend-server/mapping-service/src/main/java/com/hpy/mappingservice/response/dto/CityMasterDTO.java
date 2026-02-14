package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CityMasterDTO extends GenericDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3538726124122858150L;
	@JsonIgnore
	private Long id;
	private Long cityId;   
    private String cityName;
    private Long stateId;
    
	public CityMasterDTO(Long cityId, String cityName, Long stateId) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
		this.stateId = stateId;
	}
    
    
}
