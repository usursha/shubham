package com.hpy.mappingservice.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexOfCeSearchRequestDto {
	
	private String sortOption;
	private String availability;
	private Integer mtdTxnTargetStart;
	private Integer mtdTxnTargetEnd;
	private String city;
	private String area; 

}
