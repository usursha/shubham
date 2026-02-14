package com.hpy.ops360.ticketing.request;

import lombok.Data;

@Data
public class TicketClosureRequestDto {
	
	private String ticketNumber;
	private String atmId;
}
