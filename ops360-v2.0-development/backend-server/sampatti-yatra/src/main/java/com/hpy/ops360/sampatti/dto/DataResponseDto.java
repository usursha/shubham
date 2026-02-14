package com.hpy.ops360.sampatti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataResponseDto {
	
	private String createdBy;
	private String createdAt;
	private String modifiedBy;
	private String modifiedAt;
	private String imageId;

}
