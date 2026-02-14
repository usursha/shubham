package com.hpy.ops360.report_service.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class AtmUptimefilterEntity {
    @Id
    @Column(name = "atmId")
    private String atmId;
    
    @Column(name = "bankName")
    private String bankName;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "site")
    private String site;
    
    @Column(name = "siteId")
    private String siteId;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "installationDate")
    private String installationDate;
    
    @Column(name = "uptime")
    private Double uptime;
    
    @Column(name = "ceFullName")
    private String ceFullName;
    
    @Column(name = "installationDateRaw")
    private Timestamp installationDateRaw;
    
}