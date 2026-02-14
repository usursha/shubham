package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import com.hpy.ops360.ticketing.dto.TicketCategoryCountDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketsWithCategoryDto {
    
    private List<TicketGroupedByDateDto> ticketShortDetails;  // Grouped ticket details by event group and date
    private TicketCategoryCountDto ticketCategoryCount;  // Category count from service

    // Nested DTO for grouped tickets by event group and date
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TicketGroupedByDateDto {
        private String eventGroup;  // The event group (e.g., "HardwareFault")
        private List<TicketDataWithDateDto> flaggedTicketsByDate;  // Flagged tickets grouped by date
        private List<TicketDataWithDateDto> unflaggedTicketsByDate;  // Unflagged tickets grouped by date
    }

    // Nested DTO for ticket data grouped by date
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TicketDataWithDateDto {
        private String date;  // Date for the ticket group (e.g., "2024-11-06")
        private List<AtmShortDetailsDto> ticketData;  // List of tickets for that date
    }
}
