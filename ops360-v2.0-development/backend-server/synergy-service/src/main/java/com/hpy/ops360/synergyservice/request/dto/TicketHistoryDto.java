package com.hpy.ops360.synergyservice.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistoryDto {

	private String atmid;
	private int nticket;

}
