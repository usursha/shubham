package com.hpy.ops360.AssetService.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenericMessageResponseDto extends GenericDto{
	
	
	@JsonIgnore
	private Long id;
	
	private String message;

}
