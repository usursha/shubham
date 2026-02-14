package com.hpy.ops360.ticketing.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateTicketHimsDto {
	private String atmid;
	private String refno;
	private String diagnosis;
	private String createdby;
	private String comment;
	private String eventcode;
	
}
