package com.hpy.uam.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class GenericMessageResponseDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
	private String message;

}
