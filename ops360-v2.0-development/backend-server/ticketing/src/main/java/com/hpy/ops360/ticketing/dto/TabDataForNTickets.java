package com.hpy.ops360.ticketing.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabDataForNTickets {
    private String tab_name;
    private int tab_count;
    private List<DateGroupedTickets> ticket_data;
}