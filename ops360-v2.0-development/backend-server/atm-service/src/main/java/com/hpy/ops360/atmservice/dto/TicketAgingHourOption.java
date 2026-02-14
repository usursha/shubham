package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TicketAgingHourOption {
    private int ticketAgingHrId;
    private String ticketAgingHrName;
    private int ticketAgingHrCount;
}