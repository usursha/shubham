package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchTicketsDto extends GenericDto{

	@JsonIgnore
	private Long id;
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
    private String ceUserId;
    private String flag;
    private String atmStatus;
    private Long totalRecords;
}
