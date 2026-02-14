package com.hpy.ops360.ticketing.cm.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetail extends GenericDto{
	
	
	@JsonIgnore
	private Long id;
	
    private Long srNo;
    private String ticketNumber;
    private String ceUserName;
    private String atmId;
    private String owner;
    private String subcallType;
    private String etaDateTime;
    private String vendor;
    private String eventcode;
    private String travelEtaDatetime;
    private String downTime;
    private String travelHours;
    private String flagStatus;
    private LocalDateTime flag_status_inserttime;
    private String createdTime;
    private String border;
    private String fill;
	public TicketDetail(Long srNo, String ticketNumber, String ceUserName, String atmId, String owner,
			String subcallType, String etaDateTime, String vendor, String eventcode, String travelEtaDatetime,
			String downTime, String travelHours, String flagStatus, LocalDateTime flag_status_inserttime,String createdTime, String border,
			String fill) {
		this.srNo = srNo;
		this.ticketNumber = ticketNumber;
		this.ceUserName = ceUserName;
		this.atmId = atmId;
		this.owner = owner;
		this.subcallType = subcallType;
		this.etaDateTime = etaDateTime;
		this.vendor = vendor;
		this.eventcode = eventcode;
		this.travelEtaDatetime = travelEtaDatetime;
		this.downTime = downTime;
		this.travelHours = travelHours;
		this.flagStatus = flagStatus;
		this.flag_status_inserttime = flag_status_inserttime;
		this.createdTime=createdTime;
		this.border = border;
		this.fill = fill;
	}
    
    
}