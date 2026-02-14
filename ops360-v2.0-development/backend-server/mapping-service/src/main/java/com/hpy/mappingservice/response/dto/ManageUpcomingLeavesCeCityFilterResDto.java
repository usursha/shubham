package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageUpcomingLeavesCeCityFilterResDto {
	
	@JsonIgnore
	private Long srno;
	
    private String city_name;
    private int count;
    
    public ManageUpcomingLeavesCeCityFilterResDto(String city_name, int count) {
		super();
		this.city_name = city_name;
		this.count = count;
	}

}
