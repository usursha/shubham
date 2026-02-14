package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetailDto extends GenericDto{

	@JsonIgnore
	private Long id;
    private String srno;
    private String customer;
    private String equipmentId;
    private String model;
    private String atmCategory;
    private String atmClassification;
    private LocalDateTime createdDate;
    private String callType;
    private String subCallType;
    private String vendor;
    private String serviceCode;
    private String diagnosis;
    private String eventCode;
    private String helpdeskname;
    private String lastActivity;
    private String status;
    private String subStatus;
    private String site;
    private String city;
    private String state;
    private LocalDateTime etaDatetime;
    private String owner;
    private String customerRemark;
    private String ticketSource;
    private String atmStatus;

    // Calculated/Formatted fields
    private String formattedDowntime;
    private Integer ageingDays;
    private String ceUserId;
    private String flagStatus;
}