package com.hpy.ops360.ticketing.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDateGroup {
    private String date;
    private List<TicketData> data;
}