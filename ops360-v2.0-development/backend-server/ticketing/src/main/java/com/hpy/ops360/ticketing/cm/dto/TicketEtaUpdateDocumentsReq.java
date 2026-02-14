package com.hpy.ops360.ticketing.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketEtaUpdateDocumentsReq {

	private String username;
	private String atmId;
	private String ticketNumber;

}
