package com.hpy.hims_kafka.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketDetailsOpen {

	private String srno;
	private String customer;
	private String equipmentid;
	private String model;
	private String atmcategory;
	private String atmclassification;

	private LocalDateTime calldate; // Consider converting to LocalTime if format is consistent
	private LocalDateTime createddate; // Same here, if time-only format is used

	private String calltype;
	private String subcalltype;
	private LocalDateTime completiondatewithtime;
	private Integer downtimeinmins;

	private String vendor;
	private String servicecode;
	private String diagnosis;
	private String eventcode;
	private String helpdeskname;

	private LocalDateTime lastallocatedtime;
	private String lastcomment;
	private String lastactivity; // Consider LocalTime if format is consistent

	private String status;
	private String substatus;

	private String ro;
	private String site;
	private String address;
	private String city;
	private String locationname;
	private String state;

	
	private LocalDateTime nextfollowup;
	
	private LocalDateTime etadatetime;

	private String owner;
	private String customerRemark;
	
	private LocalDateTime first_dispatch_datetime;
	private String re_allocated_docket_no;
	private String lastsubcalltypeby;
	private LocalDateTime last_activity_datetime;
	
	private LocalDateTime lastmodifieddatetime;
	private String username;
	private String comment;
}
