package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AtmMtdUptimeDto extends GenericDto{
	
	@JsonIgnore
	private Long id;

	private double atmMtdUptime;
	
	private String lastUpdatedMtdDateTime;

}
