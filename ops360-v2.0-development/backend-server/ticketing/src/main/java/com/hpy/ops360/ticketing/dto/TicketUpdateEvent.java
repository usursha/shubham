package com.hpy.ops360.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class TicketUpdateEvent {
    private String ticketNumber;
    private String atmId;
    private String status;
    private String userName;
    private String checkerName;
    private String checkerComment;
    private String synergyTicketNo;
    private LocalDateTime timestamp;
}
