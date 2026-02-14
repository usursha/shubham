package com.hpy.ops360.ticketing.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketHistoryRequest {
	private String requestid;
	private String atmid;
	private int nticket;
}
