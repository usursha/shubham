package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistoryGroupDto{ 
	
	private String date;
	private List<TicketHistoryResponseDto> ticketData;

}
