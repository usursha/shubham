package com.hpy.ops360.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketsNumberOfCEListResponceDTO {

	 private String atmId;
	    private String comment;
	    private String createdBy;
	    private String diagnosis;
	    private String document;
	    private String documentName;
	    private String reason;
	    private String status;
	    private String ticketNumber;
}
