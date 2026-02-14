package com.hpy.ops360.atmservice.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TicketAgingDayOption {
    private int ticketAgingDaysId;
    private String ticketAgingDaysName;
    private int ticketAgingDaysCount;
}