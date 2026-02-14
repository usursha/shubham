package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketsRaisedCountresponseDto extends GenericDto {

	@JsonIgnore
	private Long id;
	
	private int sr_no;
	private int breakdown;
	private int service;
	private int updated;
	private int total;
}
