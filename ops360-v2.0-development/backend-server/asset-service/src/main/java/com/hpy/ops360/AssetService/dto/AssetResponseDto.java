package com.hpy.ops360.AssetService.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetResponseDto extends GenericDto{

	@JsonIgnore
	private Long id;
	
	
	
	
	
	
	
}
