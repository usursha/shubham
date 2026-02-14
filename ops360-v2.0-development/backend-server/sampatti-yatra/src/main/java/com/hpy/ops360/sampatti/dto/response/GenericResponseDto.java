package com.hpy.ops360.sampatti.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponseDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
	private String message;
	

}
