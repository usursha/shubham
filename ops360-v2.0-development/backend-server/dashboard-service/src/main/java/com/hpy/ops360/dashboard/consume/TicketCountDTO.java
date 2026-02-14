package com.hpy.ops360.dashboard.consume;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketCountDTO {

	private int breakdown;
	private int service;
	private int updated;
	private int total;
}
