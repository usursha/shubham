package com.hpy.mappingservice.request.dto;

import org.springframework.data.repository.query.Param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CEIndexRequestDto {
	
	private Integer  pageNumber;
	private Integer  pageSize;
	private String sortOption;
	private String availability;
	private Integer mtdTxnTargetStart;
	private Integer mtdTxnTargetEnd;
	private String city;
	private String area; 
	private String search; 

}
