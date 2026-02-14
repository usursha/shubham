package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketDataWrapper {

	private Integer totalCount;
	private List<GroupedTicketDTO> groupedTickets;
}
