package com.hpy.ops360.dashboard.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketsRaisedCountDTO extends GenericDto{

	@JsonIgnore
	private Long id;
	
	private int srNo;
	private int breakdown;
	private int service;
	private int updated;
	private int total;
}
