package com.hpy.ops360.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private Integer id;
    private String username;
    private Date actionTime;
    private String actionTaken;
    private String atmId;
    private String ticketNo;
    private Date etaUpdatedTo;
    private String travelMode;
    private Date travelEta;
    private String workMode;
    private String owner;
    private String custRemarks;
    private String internalRemarks;
    private String createTicketReason;
	
}
