package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketAllocationResponseDto extends GenericDto {
	
	  private List<TicketAllocationGroupDto> ticketAllocation;
}
