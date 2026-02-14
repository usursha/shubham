package com.hpy.ops360.report_service.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TicketDetails {
	
    @Id
    private String ticketnumber;

    private String atmid;
    private String bank;
    private String status;
    private String owner;
    private String subcalltype;
    private String cefullname;
    private String businessmodel;
    private String sitetype;
    @Column(name = "etadatetime")
    private String etaDateTime;
}