package com.hpy.ops360.ticketing.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentCMTicketDetailsRequestDTO {

	private String atmId;
	private String ticketNumber;
	private String createBy;
	private String cmComments;
}
