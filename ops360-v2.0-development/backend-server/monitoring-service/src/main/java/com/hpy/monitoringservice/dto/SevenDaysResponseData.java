package com.hpy.monitoringservice.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SevenDaysResponseData {
	    private String designation;
	    private String color;
	    private String maxCountColor;
	    private Set<HistoryDto> history;
	
	
	

}
