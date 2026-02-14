package com.hpy.ops360.ticketing.cm.dto;
import java.util.Date;

import org.apache.james.mime4j.dom.datetime.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailsDto extends GenericDto{
	@JsonIgnore
	private Long id;
	private String sr_no;
    public String ticketNumber; // Ticket Number
    public String bankAsPerSynergy; // BANK as per Synergy
    public String bankAsPerOps360; // BANK as per Ops360
    public String atmId; // ATM ID
    public String atmCategory; // ATM Category
    public String siteType; // Site Type
    public String address; // Address
    public String city; // City
    public String state; // State
    public String zone; // Zone
    public String zonalHead; // Zonal Head
    public String stateHead; // State Head
    public String channelManager; // Channel Manager
    public String channelExecutive; // Channel Executive
    public String fieldServiceCoordinator; // Field Service Coordinator
    public String owner; // Owner
    public String subCall; // SubCall
    public String eventCode; // Event Code
    public String vendor; // Vendor
    public String downtimeHoursMin; // Downtime (Hours/Min)
    public String downtimeBucket; // Downtime (Bucket)
    public String ticketCreatedDateTime; // Ticket Created Date & Time
    public int expectedTat; // Expected TaT
    public String firstDispatchDateTime; // First Dispatch Date & Time
    public String reallocatedDocketNo; // Re-allocated Docket No
    public String lastAllocatedBy; // Last Allocated By
    public String lastAllocatedDateTime; // Last Allocated Date & Time
    public String cesActionDateTime; // CE's Action Date & Time
    public String ceEtaUpdatedDateTime; // CE ETA Updated Date & Time
    public String callCloseDateTime; // Call Close Date & Time
    public String callCloseWithinTatOrOutOfTat; // Call Close within TaT/Out of TaT
    public String customerRemarks; // Customer Remarks
    public String ceInternalRemarks; // CE Internal Remarks
    public String lastActivityDateTime; // Last Activity Date & Time
    public String lastCommentAsPerSynergy; // Last comment as per Synergy
    public String nextFollowUpDateTime; // Next Follow up Date & Time
    public String actionTaken; // Action Taken
    public String ticketStatus; // Ticket Status (Open/Updated/Time Out/Close)
	public ReportDetailsDto(String sr_no, String ticketNumber, String bankAsPerSynergy, String bankAsPerOps360, String atmId,
			String atmCategory, String siteType, String address, String city, String state, String zone,
			String zonalHead, String stateHead, String channelManager, String channelExecutive,
			String fieldServiceCoordinator, String owner, String subCall, String eventCode, String vendor,
			String downtimeHoursMin, String downtimeBucket, String ticketCreatedDateTime, int expectedTat,
			String firstDispatchDateTime, String reallocatedDocketNo, String lastAllocatedBy,
			String lastAllocatedDateTime, String cesActionDateTime, String ceEtaUpdatedDateTime,
			String callCloseDateTime, String callCloseWithinTatOrOutOfTat, String customerRemarks,
			String ceInternalRemarks, String lastActivityDateTime, String lastCommentAsPerSynergy,
			String nextFollowUpDateTime, String actionTaken, String ticketStatus) {
		super();
		this.sr_no=sr_no;
		this.ticketNumber = ticketNumber;
		this.bankAsPerSynergy = bankAsPerSynergy;
		this.bankAsPerOps360 = bankAsPerOps360;
		this.atmId = atmId;
		this.atmCategory = atmCategory;
		this.siteType = siteType;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zone = zone;
		this.zonalHead = zonalHead;
		this.stateHead = stateHead;
		this.channelManager = channelManager;
		this.channelExecutive = channelExecutive;
		this.fieldServiceCoordinator = fieldServiceCoordinator;
		this.owner = owner;
		this.subCall = subCall;
		this.eventCode = eventCode;
		this.vendor = vendor;
		this.downtimeHoursMin = downtimeHoursMin;
		this.downtimeBucket = downtimeBucket;
		this.ticketCreatedDateTime = ticketCreatedDateTime;
		this.expectedTat = expectedTat;
		this.firstDispatchDateTime = firstDispatchDateTime;
		this.reallocatedDocketNo = reallocatedDocketNo;
		this.lastAllocatedBy = lastAllocatedBy;
		this.lastAllocatedDateTime = lastAllocatedDateTime;
		this.cesActionDateTime = cesActionDateTime;
		this.ceEtaUpdatedDateTime = ceEtaUpdatedDateTime;
		this.callCloseDateTime = callCloseDateTime;
		this.callCloseWithinTatOrOutOfTat = callCloseWithinTatOrOutOfTat;
		this.customerRemarks = customerRemarks;
		this.ceInternalRemarks = ceInternalRemarks;
		this.lastActivityDateTime = lastActivityDateTime;
		this.lastCommentAsPerSynergy = lastCommentAsPerSynergy;
		this.nextFollowUpDateTime = nextFollowUpDateTime;
		this.actionTaken = actionTaken;
		this.ticketStatus = ticketStatus;
	}
    
    
    
}
