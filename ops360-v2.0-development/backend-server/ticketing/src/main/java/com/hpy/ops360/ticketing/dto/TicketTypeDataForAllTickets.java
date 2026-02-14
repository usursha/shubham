package com.hpy.ops360.ticketing.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeDataForAllTickets {

	private String name;
    private int count;
    private List<TicketDateGroup> flagedTickets;
    private List<TicketDateGroup> otherTickets;
}
