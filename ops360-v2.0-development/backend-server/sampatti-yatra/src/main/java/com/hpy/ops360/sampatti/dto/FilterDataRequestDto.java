package com.hpy.ops360.sampatti.dto;

import lombok.Data;

@Data
public class FilterDataRequestDto {
	
	private String selectedZone;
	private String selectedState;
	private String selectedCity;
	private String role;

}
