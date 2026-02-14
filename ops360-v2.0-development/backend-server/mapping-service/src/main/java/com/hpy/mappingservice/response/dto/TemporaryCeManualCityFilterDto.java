package com.hpy.mappingservice.response.dto;

import com.hpy.mappingservice.request.dto.PermanentCECityDataFilterDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
	
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemporaryCeManualCityFilterDto {
	
	// 	private String city_id;
	    private String city_name;
	    private String count;

}


