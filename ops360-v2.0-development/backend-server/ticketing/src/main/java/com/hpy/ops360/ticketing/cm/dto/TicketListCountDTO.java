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
public class TicketListCountDTO extends GenericDto{
	
	@JsonIgnore
	private Long id;

	private Long srNo;
	private long openCount;
	
	@Builder
	public TicketListCountDTO(Long srNo, long openCount) {
		this.srNo = srNo;
		this.openCount = openCount;
	}
}
