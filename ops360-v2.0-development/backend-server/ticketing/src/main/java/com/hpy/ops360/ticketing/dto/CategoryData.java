package com.hpy.ops360.ticketing.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryData {
    private String name;
    private int count;
    private List<TicketGroup> flagedTickets;
    private List<TicketGroup> openTickets;
}