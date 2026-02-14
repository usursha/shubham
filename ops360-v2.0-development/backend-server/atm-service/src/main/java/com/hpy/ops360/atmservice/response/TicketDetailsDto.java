package com.hpy.ops360.atmservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
@RequiredArgsConstructor
public class TicketDetailsDto {
	private String srno;
	private String customer;
	private String equipmentid;
	private String model;
	private String atmcategory;
	private String atmclassification;
	private String calldate;
	private String createddate;
	private String calltype;
	private String subcalltype;
	private String completiondatewithtime;
	private int downtimeinmins;
	private String vendor;
	private String servicecode;
	private String diagnosis;
	private String eventcode;
	private String helpdeskname;
	private String lastallocatedtime;
	private String lastcomment;
	private String lastactivity;
	private String status;
	private String substatus;
	private String ro;
	private String site;
	private String address;
	private String city;
	private String locationname;
	private String state;
	private String nextfollowup;
	public TicketDetailsDto(String srno, String equipmentid, String eventcode) {
		super();
		this.srno = srno;
		this.equipmentid = equipmentid;
		this.eventcode = eventcode;
	}
	public TicketDetailsDto(String srNo2, String customer2, String equipmentId2, String model2, String atmCategory2,
			String atmClassification2, String formatDateField, String formatDateField2, String callType2,
			String subCallType2, String formatDateField3, int downtimeInMins2, String vendor2, String serviceCode2,
			String diagnosis2, String eventCode2, String helpdeskName2, String formatDateField4, String lastComment2,
			String formatDateField5, String status2, String subStatus2, String ro2, String site2, String address2,
			String city2, String locationName2, String state2, String formatDateField6, String formatDateField7,
			String owner, String customerRemark) {
	}
	
	
}
