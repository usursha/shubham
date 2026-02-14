package com.hpy.ops360.ticketing.cm.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistorySearchEntity {
	
	
	@Column(name = "rno")
	private Long rno;

	@Id
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
	private String callDate;

	@Column(name = "createddate")
	private String createdDate;

	@Column(name = "calltype")
	private String callType;

	@Column(name = "subcalltype")
	private String subCallType;

	@Column(name = "completiondatewithtime")
	private String completionDateWithTime;

	@Column(name = "downtime")
	private String downtime;

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
	private String lastAllocatedTime;

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
	private String nextFollowUp;

	@Column(name = "etadatetime")
	private String etaDateTime;

	@Column(name = "owner")
	private String owner;

	@Column(name = "customer_remark")
	private String customerRemark;

	@Column(name = "insertdate")
	private String insertDate;

	@Column(name = "ce_user_id")
	private String ce_user_id;

	@Column(name = "remark")
	private String remark;

	@Column(name = "atm_address")
	private String atm_address;

	@Column(name = "site_id")
	private String site_id;

	@Column(name = "hims_atm_status")
	private String atm_status;

	@Column(name = "flag_status")
	private String flag_status;


}
