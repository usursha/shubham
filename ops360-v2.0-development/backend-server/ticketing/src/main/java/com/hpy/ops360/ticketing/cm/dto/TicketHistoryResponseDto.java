package com.hpy.ops360.ticketing.cm.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistoryResponseDto extends GenericDto{
	//private Long srno;
	//private String dateLabel;
//    private String createdOn;
//    private String ceUserId;
//    private String ticketNumber; //srno
//    private String atmId;  //ticketNo
//    private String owner;
//    private String remark;
//    private String subCallType;
//    private String atmAddress;
//    private String resolvedOn;
//    private String vendor;
//    private String siteId;
//    private String status;
//    private String himsAtmStatus;
	
	private Long rno;
    private String ticketNumber; // maps to srno
    private String bank; // maps to customer
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
    private String insertDate;
    private String ceName; // maps to ce_user_id
    private String remark;
    private String atmAddress; // maps to atm_address
    private String siteId; // maps to site_id
    private String himsTicketStatus; // maps to atm_status
    private String flagStatus; // maps to flag_status
}
