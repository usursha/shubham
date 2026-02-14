package com.hpy.ops360.ticketing.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistoryData {
    private int total_count;
    private List<TabDataForNTickets> tab_data;
}