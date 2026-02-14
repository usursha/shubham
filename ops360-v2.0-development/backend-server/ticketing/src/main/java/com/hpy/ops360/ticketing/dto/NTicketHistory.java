package com.hpy.ops360.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NTicketHistory {
	
	private String atmId;//equipmentid
	private String ticketNumber; //srno
	private String owner; //servicecode
	private String issue; //calltype
	private String calldate	;	//calldate
	private int downTime; //downtimeinmins
}
