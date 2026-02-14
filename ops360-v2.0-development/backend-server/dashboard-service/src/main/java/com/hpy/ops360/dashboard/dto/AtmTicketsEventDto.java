package com.hpy.ops360.dashboard.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class AtmTicketsEventDto extends GenericDto {

	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private Long id;
	private Long srNo;
	private String atmId;
	private String ticketId;
	private String eventCode;
	private Double priorityScore;
	private String eventGroup;
	private Integer isBreakdown;
	private Integer isUpdated;
	private Integer isTimedOut;
	private Integer isTravelling;
	private LocalDateTime travelTime;
	private Integer travelEta;
	private Integer downCall;
	private String etaDateTime;
	private String owner;
	private String subcall;
	private String etaTimeout;
	private int flagStatus;
	private LocalDateTime flagStatusInsertTime;

	public AtmTicketsEventDto(Long srNo, String atmId, String ticketId, String eventCode, Double priorityScore,
			String eventGroup, Integer isBreakdown, Integer isUpdated, Integer isTimedOut, Integer isTravelling,
			LocalDateTime travelTime, Integer travelEta, Integer downCall, String etaDateTime, String owner,
			String subcall, String etaTimeout, int flagStatus, LocalDateTime flagStatusInsertTime) {
		this.srNo = srNo;
		this.atmId = atmId;
		this.ticketId = ticketId;
		this.eventCode = eventCode;
		this.priorityScore = priorityScore;
		this.eventGroup = eventGroup;
		this.isBreakdown = isBreakdown;
		this.isUpdated = isUpdated;
		this.isTimedOut = isTimedOut;
		this.isTravelling = isTravelling;
		this.travelTime = travelTime;
		this.travelEta = travelEta;
		this.downCall = downCall;
		this.etaDateTime = etaDateTime;
		this.owner = owner;
		this.subcall = subcall;
		this.etaTimeout = etaTimeout;
		this.flagStatus = flagStatus;
		this.flagStatusInsertTime = flagStatusInsertTime;
	}

}