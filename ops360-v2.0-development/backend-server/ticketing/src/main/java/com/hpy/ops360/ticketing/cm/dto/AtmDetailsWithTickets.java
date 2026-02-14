package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import com.hpy.ops360.ticketing.dto.TicketDetailsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmDetailsWithTickets {

	private String atmid;
	private List<TicketDetailsDto> openticketdetails;
}
