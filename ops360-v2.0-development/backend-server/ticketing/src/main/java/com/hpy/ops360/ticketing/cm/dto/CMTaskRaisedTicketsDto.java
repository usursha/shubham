package com.hpy.ops360.ticketing.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CMTaskRaisedTicketsDto {

	private int srNo;
	private String atmId;
	private String comment;
	private String createdBy;
	private String diagnosis;
	private String document;
	private String documentName;
	private String reason;
	private String status;
	private String ticketNumber;
	private String refNo;
	private String openTicketCount;
}
