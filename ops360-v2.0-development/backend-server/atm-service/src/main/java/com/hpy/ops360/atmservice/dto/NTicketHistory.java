package com.hpy.ops360.atmservice.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NTicketHistory extends GenericDto{
	
	@JsonIgnore
	private Long id;

	private String atmId;// equipmentid
	private String ticketNumber; // srno
	private String owner; // broad category
	private String issue; // eventcode
	private String calldate; // calldate
	private String downTime; // downtimeinmins
	private String subcall; //subcalltype
	public NTicketHistory(String atmId, String ticketNumber, String owner, String issue, String calldate,
			String downTime, String subcall) {
		this.atmId = atmId;
		this.ticketNumber = ticketNumber;
		this.owner = owner;
		this.issue = issue;
		this.calldate = calldate;
		this.downTime = downTime;
		this.subcall = subcall;
	}
	public NTicketHistory(String equipmentId, String srNo, String selectedBroadCategory, String eventCode,
			Date callDate2, String downTimeInHrs, String subCallType) {
	}
	
	
}
