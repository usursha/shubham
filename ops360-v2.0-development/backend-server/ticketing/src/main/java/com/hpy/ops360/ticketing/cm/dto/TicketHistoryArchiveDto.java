package com.hpy.ops360.ticketing.cm.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistoryArchiveDto extends GenericDto {
	
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
    private Integer downtimeInMins;
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
 //   private String insertDate;
}
