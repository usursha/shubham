package com.hpy.ops360.ticketing.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketTaskDTO {

	private String comment;
	private String createdBy;
	private LocalDateTime createdDate;
	private String reason;
	private String status;
	private String checkerName;
	private String checkerComment;
	private String atmCode;
	private String address;
	private String bankName;
	private String type = "TICKET";
//	
}
