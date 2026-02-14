package com.hpy.uam.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMobileResponseDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
	private String mobile;

}
