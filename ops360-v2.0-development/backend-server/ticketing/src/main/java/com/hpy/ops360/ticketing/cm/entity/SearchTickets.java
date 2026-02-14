package com.hpy.ops360.ticketing.cm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SearchTickets {

	@Id
	private Long rno;

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

	@Column(name = "downtimeinmins")
	private String downtimeInMins;

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

	@Column(name = "ageing_days")
	private Integer ageingDays;

	@Column(name = "ce_user_id")
	private String ceUserId;

	@Column(name = "flag")
	private String flag;

	@Column(name = "atm_status")
	private String atmStatus;

	@Column(name = "total_records")
	private Long totalRecords;

}
