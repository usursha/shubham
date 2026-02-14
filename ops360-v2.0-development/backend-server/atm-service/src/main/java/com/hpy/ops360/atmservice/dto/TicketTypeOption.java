package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TicketTypeOption {
	private int ticketTypeId;
	private String ticketTypeName;
	private int ticketTypeCount;

}