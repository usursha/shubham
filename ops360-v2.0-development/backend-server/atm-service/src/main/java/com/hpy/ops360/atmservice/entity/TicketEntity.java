package com.hpy.ops360.atmservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketEntity {

//	@Id
//	private Long rno;
//
//	@Column(name = "srno")
//	private String srno;
//
//	@Column(name = "customer")
//	private String customer;
//
//	@Column(name = "equipmentid")
//	private String equipmentId;
//
//	@Column(name = "model")
//	private String model;
//
//	@Column(name = "atmcategory")
//	private String atmCategory;
//
//	@Column(name = "atmclassification")
//	private String atmClassification;
//
//	@Column(name = "calldate")
//	private String callDate;
//
//	@Column(name = "createddate")
//	private String createdDate;
//
//	@Column(name = "calltype")
//	private String callType;
//
//	@Column(name = "subcalltype")
//	private String subCallType;
//
//	@Column(name = "completiondatewithtime")
//	private String completionDateWithTime;
//
//	@Column(name = "downtimeinmins")
//	private String downtimeInMins;
//
//	@Column(name = "vendor")
//	private String vendor;
//
//	@Column(name = "servicecode")
//	private String serviceCode;
//
//	@Column(name = "diagnosis")
//	private String diagnosis;
//
//	@Column(name = "eventcode")
//	private String eventCode;
//
//	@Column(name = "helpdeskname")
//	private String helpdeskName;
//
//	@Column(name = "lastallocatedtime")
//	private String lastAllocatedTime;
//
//	@Column(name = "lastcomment")
//	private String lastComment;
//
//	@Column(name = "lastactivity")
//	private String lastActivity;
//
//	@Column(name = "status")
//	private String status;
//
//	@Column(name = "substatus")
//	private String subStatus;
//
//	@Column(name = "ro")
//	private String ro;
//
//	@Column(name = "site")
//	private String site;
//
//	@Column(name = "address")
//	private String address;
//
//	@Column(name = "city")
//	private String city;
//
//	@Column(name = "locationname")
//	private String locationName;
//
//	@Column(name = "state")
//	private String state;
//
//	@Column(name = "nextfollowup")
//	private String nextFollowUp;
//
//	@Column(name = "etadatetime")
//	private String etaDateTime;
//
//	@Column(name = "owner")
//	private String owner;
//
//	@Column(name = "flag_status") 
//	private String pinStatus;
//
//	@Column(name = "customer_remark")
//	private String customerRemark;
//
//	@Column(name = "ce_user_id")
//	private String ce_user_id;
//
//	@Column(name = "atm_status")
//	private String atm_status;
//
//	@Column(name = "ageing_days")
//	private String ageingDays;
//
//	@Column(name = "total_records")
//	private String total_records;
//
//	@Column(name = "current_page")
//	private String current_page;
//
//	@Column(name = "page_size")
//	private String page_size;
//
//	@Column(name = "total_pages")
//	private String total_pages;
//	
	
	
	@Id
    private String rno;
    
    @Column(name = "srno")
    private String srno;
    
    @Column(name = "customer")
    private String customer;
    
    @Column(name = "equipmentid")
    private String equipmentid;
    
    @Column(name = "model")
    private String model;
    
    @Column(name = "atmcategory")
    private String atmcategory;
    
    @Column(name = "atmclassification")
    private String atmclassification;
    
    @Column(name = "calldate")
    private String calldate;
    
    @Column(name = "createddate")
    private String createddate;
    
    @Column(name = "calltype")
    private String calltype;
    
    @Column(name = "subcalltype")
    private String subcalltype;
    
    @Column(name = "completiondatewithtime")
    private String completiondatewithtime;
    
    @Column(name = "downtimeinmins")
    private String downtimeinmins;
    
    @Column(name = "vendor")
    private String vendor;
    
    @Column(name = "servicecode")
    private String servicecode;
    
    @Column(name = "diagnosis")
    private String diagnosis;
    
    @Column(name = "eventcode")
    private String eventcode;
    
    @Column(name = "helpdeskname")
    private String helpdeskname;
    
    @Column(name = "lastallocatedtime")
    private String lastallocatedtime;
    
    @Column(name = "lastcomment")
    private String lastcomment;
    
    @Column(name = "lastactivity")
    private String lastactivity;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "substatus")
    private String substatus;
    
    @Column(name = "ro")
    private String ro;
    
    @Column(name = "site")
    private String site;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "locationname")
    private String locationname;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "nextfollowup")
    private String nextfollowup;
    
    @Column(name = "etadatetime")
    private String etadatetime;
    
    @Column(name = "owner")
    private String owner;
    
    @Column(name = "customer_remark")
    private String customerRemark;
    
    @Column(name = "ticket_source")
    private String ticketSource;
    
    @Column(name = "formatted_downtime")
    private String formattedDowntime;
    
    @Column(name = "ageing_days")
    private String ageingDays;
    
    @Column(name = "atm_status")
    private String atmStatus;
    
    @Column(name = "ce_user_id")
    private String ceUserId;
    
    @Column(name = "flag_status")
    private String flagStatus;
    
    @Column(name = "total_records")
    private String total_records;
    
    @Column(name = "current_page")
    private String current_page;
    
    @Column(name = "page_size")
    private String page_size;
    
    @Column(name = "total_pages")
    private String total_pages;
}