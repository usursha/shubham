package com.hpy.ops360.ticketing.cm.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketDTO {

	private Long rno;
    private String ticketNumber;
    private String bank;
    private String equipmentId;
    private String model;
    private String atmCategory;
    private String atmClassification;
    private String callDate;
    private String createdDate;
    private String callType;
    private String subCallType;
    private String completionDateWithTime;
    private String downtimeInMins;
    private String vendor;
    private String serviceCode;
    private String diagnosis;
    private String eventCode;
    private String helpdeskName;
    private String lastAllocatedTime;
    private String lastComment;
    private String lastActivity;
    private String status;
    private String subStatus;
    private String ro;
    private String site;
    private String address;
    private String city;
    private String locationName;
    private String state;
    private String nextFollowUp;
    private String etaDateTime;
    private String owner;
    private String customerRemark;
    private Integer ageingDays;
    private String ceName;
    private String HimsTicketStatus;
    private String flagStatus;
    
    // Pagination fields
    private String totalRecords;
    private String currentPage;
    private String pageSize;
    private String totalPages;
}
