package com.hpy.ops360.ticketing.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketAtmIdDtoReq {
	private String createdBy;
	private String ticketNumber;
	private String atmId;
}
