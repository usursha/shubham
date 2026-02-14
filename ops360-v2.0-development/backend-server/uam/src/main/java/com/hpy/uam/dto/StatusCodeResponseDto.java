package com.hpy.uam.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusCodeResponseDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
	private int code;
	
	private String message;

}
