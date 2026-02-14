package com.MapPUC.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsertotaldistLocationDto{
	
	@JsonIgnore
	private Long id;
	
    private String totalDistance;
    private String totalTime;
}
