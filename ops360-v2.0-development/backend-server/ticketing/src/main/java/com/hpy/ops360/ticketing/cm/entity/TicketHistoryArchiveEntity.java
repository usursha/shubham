package com.hpy.ops360.ticketing.cm.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class TicketHistoryArchiveEntity {

		@Id
		@Column(name="id")
		private Integer id;
		
	    @Column(name = "srno")
	    private String srno;

	    @Column(name = "customer")
	    private String customer;

	    @Column(name = "equipmentid")
	    private String equipmentId;

	    @Column(name = "model")
	    private String model;

	    @Column(name = "atmcategory")
	    private String atmCategory;

	    @Column(name = "atmclassification")
	    private String atmClassification;

	    @Column(name = "calldate")
	    private Date callDate;

	    @Column(name = "createddate")
	    private Date createdDate;

	    @Column(name = "calltype")
	    private String callType;

	    @Column(name = "subcalltype")
	    private String subCallType;

	    @Column(name = "completiondatewithtime")
	    private Date completionDateWithTime;

	    @Column(name = "downtimeinmins")
	    private Integer downtimeInMins;

	    @Column(name = "vendor")
	    private String vendor;

	    @Column(name = "servicecode")
	    private String serviceCode;

	    @Column(name = "diagnosis")
	    private String diagnosis;

	    @Column(name = "eventcode")
	    private String eventCode;

	    @Column(name = "helpdeskname")
	    private String helpdeskName;

	    @Column(name = "lastallocatedtime")
	    private Date lastAllocatedTime;

	    @Column(name = "lastcomment")
	    private String lastComment;

	    @Column(name = "lastactivity")
	    private String lastActivity;

	    @Column(name = "status")
	    private String status;

	    @Column(name = "substatus")
	    private String subStatus;

	    @Column(name = "ro")
	    private String ro;

	    @Column(name = "site")
	    private String site;

	    @Column(name = "address")
	    private String address;

	    @Column(name = "city")
	    private String city;

	    @Column(name = "locationname")
	    private String locationName;

	    @Column(name = "state")
	    private String state;

	    @Column(name = "nextfollowup")
	    private Date nextFollowUp;

	    @Column(name = "etadatetime")
	    private Date etaDateTime;

	    @Column(name = "owner")
	    private String owner;

	    @Column(name = "customer_remark")
	    private String customerRemark;

//	    @Column(name = "insertdate")
//	    private Date insertDate;
	
}

