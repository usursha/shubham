package com.hpy.ops360.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketFlagStatusPinResponseDTO {

	private String ceUserId;
	private String ticketNumber;
	private int flagStatus;
}
