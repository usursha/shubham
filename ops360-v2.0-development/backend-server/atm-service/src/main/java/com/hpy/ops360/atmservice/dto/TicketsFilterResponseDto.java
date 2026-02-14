package com.hpy.ops360.atmservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketsFilterResponseDto {

	private String rno;
    private String srno;
    private String customer;
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
    private String ticketSource;
    private String formattedDowntime;
    private String ageingDays;
    private String atmStatus;
    private String ceUserId;
    private String flagStatus;
    
    // Pagination fields
    //private String totalRecords;
    //private String currentPage;
    //private String pageSize;
    //private String totalPages;
}
