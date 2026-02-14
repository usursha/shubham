package com.hpy.ops360.sampatti.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserIncentiveMonthYearDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	private String monthyear;
	
	public UserIncentiveMonthYearDto(String monthyear) {
		super();
		this.monthyear = monthyear;
	}
	
	
}
