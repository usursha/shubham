package com.hpy.ops360.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataResponseDto {
	
	@JsonIgnore
	private String createdBy;
	@JsonIgnore
	private String createdAt;
	@JsonIgnore
	private String modifiedBy;
	@JsonIgnore
	private String modifiedAt;
//	@JsonIgnore
	private String imageId;
	

	

}
