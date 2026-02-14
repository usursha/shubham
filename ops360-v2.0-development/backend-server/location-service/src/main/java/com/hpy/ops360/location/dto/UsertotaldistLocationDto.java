package com.hpy.ops360.location.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsertotaldistLocationDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
    private String totalDistance;
    
    private String totalTime;
}
