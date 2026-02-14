package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubcallTypeForAtmTicketHistoryResponseDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
	private String subcallype;

	public SubcallTypeForAtmTicketHistoryResponseDto(String subcallype) {
		super();
		this.subcallype = subcallype;
	}
	
}
