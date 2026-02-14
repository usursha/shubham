package com.hpy.ops360.ticketing.entity;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;

@Data
public class Report {
	
	
    @Id
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
