package com.hpy.ops360.dashboard.dto;

import java.util.List;

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
