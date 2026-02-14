package com.hpy.mappingservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "eventcode_mast")
public class EventCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SR_NO")
    private Integer srNo;

    @Column(name = "eventcode")
    private String eventcode;

    @Column(name = "category")
    private String category;

    @Column(name = "CRMServiceCode")
    private String crmServiceCode;

    @Column(name = "ReportDisplay")
    private String reportDisplay;

    @Column(name = "CallType")
    private String callType;

    @Column(name = "SubCallType")
    private String subCallType;

    @Column(name = "eventgroup")
    private String eventgroup;

    @Column(name = "isbreakdown")
    private double isbreakdown;

    @Column(name = "instert_time")
    private LocalDateTime instertTime;

    @Column(name = "priority_score")
    private Integer priorityScore;

    @Column(name = "synergy_application_source")
    private String synergyApplicationSource;

    // Getters and setters omitted for brevity
}


