package com.hpy.ops360.ticketing.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketDetailsRequest {
	
	private String requestid;
	private String ticketno;
	private String atmid;
	
}
