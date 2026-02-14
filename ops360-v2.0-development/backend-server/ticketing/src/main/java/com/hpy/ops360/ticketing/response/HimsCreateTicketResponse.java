package com.hpy.ops360.ticketing.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HimsCreateTicketResponse {
    private String ticketno;
    private String status;
}