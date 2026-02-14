package com.hpy.ops360.ticketing.cm.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTicketDocumentsRequest {
    private String atm_id;
    private String status;
    private String ticketType;
    private String dateStr;
    private String startDate;
    private String endDate;
    private String ticketNumber;
    private String lastModifiedDateStr;
}