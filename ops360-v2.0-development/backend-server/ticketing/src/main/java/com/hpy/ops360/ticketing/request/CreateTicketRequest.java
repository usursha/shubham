package com.hpy.ops360.ticketing.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateTicketRequest {
	
	private String requestid;
	private String atmid;
	private String refno;
	private String diagnosis;
	private String createdby;
	private String documentname;
	private String document;
	private String comment;

}
