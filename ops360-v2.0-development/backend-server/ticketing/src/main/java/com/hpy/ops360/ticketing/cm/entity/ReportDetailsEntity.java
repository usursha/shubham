package com.hpy.ops360.ticketing.cm.entity;
import java.util.Date;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReportDetailsEntity {

    @Id
    @Column(name = "sr_no")
    private String sr_no;
    
    @Column(name = "Ticket Number")
    private String ticketNumber; // Ticket Number

    @Column(name = "BANK as per Synergy")
    private String bankAsPerSynergy; // BANK as per Synergy

    @Column(name = "BANK as per Ops360")
    private String bankAsPerOps360; // BANK as per Ops360

    @Column(name = "ATM ID")
    private String atmId; // ATM ID

    @Column(name = "ATM Category")
    private String atmCategory; // ATM Category

    @Column(name = "Site Type")
    private String siteType; // Site Type

    @Column(name = "Address")
    private String address; // Address

    @Column(name = "City")
    private String city; // City

    @Column(name = "State")
    private String state; // State

    @Column(name = "Zone")
    private String zone; // Zone

    @Column(name = "Zonal Head")
    private String zonalHead; // Zonal Head

    @Column(name = "State Head")
    private String stateHead; // State Head

    @Column(name = "Channel Manager")
    private String channelManager; // Channel Manager

    @Column(name = "Channel Executive")
    private String channelExecutive; // Channel Executive

    @Column(name = "Field Service Coordinator")
    private String fieldServiceCoordinator; // Field Service Coordinator

    @Column(name = "Owner")
    private String owner; // Owner

    @Column(name = "sub_call")
    private String SubCall; // SubCall

    @Column(name = "Event Code")
    private String eventCode; // Event Code

    @Column(name = "Vendor")
    private String vendor; // Vendor

    @Column(name = "Downtime (Hours/Min)")
    private String downtimeHoursMin; // Downtime (Hours/Min)

    @Column(name = "Downtime (Bucket)")
    private String downtimeBucket; // Downtime (Bucket)

    @Column(name = "Ticket Created Date & Time")
    private String ticketCreatedDateTime; // Ticket Created Date & Time

    @Column(name = "Expected TaT")
    private int expectedTat; // Expected TaT

    @Column(name = "First Dispatch Date & Time")
    private String firstDispatchDateTime; // First Dispatch Date & Time

    @Column(name = "Re-allocated Docket No")
    private String reallocatedDocketNo; // Re-allocated Docket No

    @Column(name = "Last Allocated By")
    private String lastAllocatedBy; // Last Allocated By

    @Column(name = "Last Allocated Date & Time")
    private String lastAllocatedDateTime; // Last Allocated Date & Time

    @Column(name = "CE's Action Date & Time")
    private String cesActionDateTime; // CE's Action Date & Time

    @Column(name = "CE ETA Updated Date & Time")
    private String ceEtaUpdatedDateTime; // CE ETA Updated Date & Time

    @Column(name = "Call Close Date & Time")
    private String callCloseDateTime; // Call Close Date & Time

    @Column(name = "Call Close within TaT/Out of TaT")
    private String callCloseWithinTatOrOutOfTat; // Call Close within TaT/Out of TaT

    @Column(name = "Customer Remarks")
    private String customerRemarks; // Customer Remarks

    @Column(name = "CE Internal Remarks")
    private String ceInternalRemarks; // CE Internal Remarks

    @Column(name = "Last Activity Date & Time")
    private String lastActivityDateTime; // Last Activity Date & Time

    @Column(name = "Last comment as per Synergy")
    private String lastCommentAsPerSynergy; // Last comment as per Synergy

    @Column(name = "Next Follow up Date & Time")
    private String nextFollowUpDateTime; // Next Follow up Date & Time

    @Column(name = "Action Taken")
    private String actionTaken; // Action Taken

    @Column(name = "Ticket Status (Open/Updated/Time Out/Close)")
    private String ticketStatus; // Ticket Status (Open/Updated/Time Out/Close)

}

