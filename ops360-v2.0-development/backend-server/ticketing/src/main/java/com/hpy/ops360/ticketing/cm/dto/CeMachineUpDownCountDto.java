package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CeMachineUpDownCountDto extends GenericDto{
	
	@JsonIgnore
	private Long id;

	private int downAtm;

	private int upAtm;

	private int totalAtm;
}
