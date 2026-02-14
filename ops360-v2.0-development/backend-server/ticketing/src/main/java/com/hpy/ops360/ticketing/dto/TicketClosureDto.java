package com.hpy.ops360.ticketing.dto;

import lombok.Data;

@Data
public class TicketClosureDto {

	private String username;
	private String password;
	private String ticketNo;
	private String atmId;
	private String comments;
	private String closureDate;

}
