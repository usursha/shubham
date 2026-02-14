package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TicketAgingOption {
	private int ticketAgingId;
	private String ticketAgingName;
	private int ticketAgingCount;
}