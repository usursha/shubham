package com.hpy.ops360.atmservice.entity;

import java.sql.Date;

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
public class AtmTicketEvent {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "atm_id")
	private String atmId;

	@Column(name = "ticket_id")
	private String ticketId;

	@Column(name = "eventcode")
	private String eventCode;

	@Column(name = "priority_score")
	private Double priorityScore;

	@Column(name = "eventgroup")
	private String eventGroup;

	@Column(name = "isbreakdown")
	private Integer isBreakdown;

 //	@Column(name = "isUpdated")
	@Column(name = "is_updated")
	private Integer isUpdated;

 //	@Column(name = "isTimedOut")
	@Column(name = "is_timed_out")
	private Integer isTimedOut;

//	@Column(name = "isTravelling")
	@Column(name = "is_travelling")
	private Integer isTravelling;

	@Column(name = "travel_time")
	private Date travelTime;

	@Column(name = "travel_eta")
	private Integer travelEta;

}
