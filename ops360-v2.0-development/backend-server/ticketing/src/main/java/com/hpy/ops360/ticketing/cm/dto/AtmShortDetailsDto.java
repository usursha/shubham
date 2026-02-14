package com.hpy.ops360.ticketing.cm.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.ticketing.utils.TicketColorDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AtmShortDetailsDto extends GenericDto {

	@JsonIgnore
	private Long id;

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
	 //@JsonSerialize(using = CustomDateSerializer.class)
	private Date travelTime;
	private Integer travelEta;
	private Integer downCall;
	private String etaDateTime;
	private String etaTimeout;
	private String CreatedDate;
	private String closeDate;
	private String SynergyStatus;
	private String remark;
	private String himsStatus;
	
	private int flagStatus;
	private LocalDateTime flagStatusInsertTime;

	@Setter
	private TicketColorDto color;
	private String ceName;
	private String createdTime;
	private String closedTime;
	
	private String hr;

}
