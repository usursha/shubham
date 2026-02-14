package com.hpy.ops360.ticketing.cm.dto;

import lombok.Data;

@Data
public class TicketArchiveListReqDto {
	private String bankname;
	private String atmcode;
	private String ticketNumber; 
	private String ceUserId;

}
