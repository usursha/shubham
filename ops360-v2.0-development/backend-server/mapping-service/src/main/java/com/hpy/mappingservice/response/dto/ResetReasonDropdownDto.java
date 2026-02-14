package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetReasonDropdownDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
    private Long srno;
    private String reason;
}
