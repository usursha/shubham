package com.hpy.ops360.atmservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class TicketHistoryRequest {
	private String requestid;
	private String atmid;
	private int nticket;
}
