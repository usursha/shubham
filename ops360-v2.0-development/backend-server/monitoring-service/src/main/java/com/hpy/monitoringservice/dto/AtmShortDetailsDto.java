package com.hpy.monitoringservice.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AtmShortDetailsDto {

	private String requestid;
	private String atmId;
	private String ticketNumber;
	private String bank;
	private String siteName;
	private String owner;
	private String subcall;
	private String vendor;
	private String error;
	private String downTime;
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
	private String etaTimeout;
	private String CreatedDate;
	
	private int flagStatus;
	private LocalDateTime flagStatusInsertTime;
	private boolean updateEnable;
}
