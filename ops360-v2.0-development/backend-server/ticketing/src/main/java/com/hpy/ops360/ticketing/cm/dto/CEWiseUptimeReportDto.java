package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CEWiseUptimeReportDto extends GenericDto {

    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Long SrNo; // match entity's Sr_No

    // Date and Zone Information
    private String date;       // match entity's Date
    private String zone;       // match entity's zone

    // Zonal Head Information
    private String zonalHead;              // match entity's zonalHead
    private String zonalHeadContactDetails; // match entity's zonalHeadContactDetails
    private String zonalHeadEmail;         // match entity's zonalHeadEmail

    // State Head Information
    private String stateHead;              // match entity's stateHead
    private String stateHeadContactDetails; // match entity's stateHeadContactDetails
    private String stateHeadEmail;         // match entity's stateHeadEmail

    // Channel Manager Information
    private String channelManager;         // match entity's channelManager
    private String channelManagerContactDetails; // match entity's channelManagerContactDetails
    private String channelManagerEmail;    // match entity's channelManagerEmail

    // Channel Executive Information
    private String channelExecutive;       // match entity's channelExecutive
    private String channelExecutiveContactDetails; // match entity's channelExecutiveContactDetails
    private String channelExecutiveEmail;  // match entity's channelExecutiveEmail

    // ATM and Performance Metrics
    private Integer noOfAssignedAtm;       // match entity's noOfAssignedAtm
    private Float uptimeTarget;            // match entity's uptimeTarget
    private String mtdUptime;              // match entity's mtdUptime
    private Integer mtdTxnTarget;          // match entity's mtdTxnTarget
    private Integer mtdTxnAchieved;        // match entity's mtdTxnAchieved

    // Constructor to directly map entity fields (if required)
    public CEWiseUptimeReportDto( String date, String zone, String zonalHead,
                                  String zonalHeadContactDetails, String zonalHeadEmail, String stateHead,
                                  String stateHeadContactDetails, String stateHeadEmail, String channelManager,
                                  String channelManagerContactDetails, String channelManagerEmail,
                                  String channelExecutive, String channelExecutiveContactDetails,
                                  String channelExecutiveEmail, Integer noOfAssignedAtm, Float uptimeTarget,
                                  String mtdUptime, Integer mtdTxnTarget, Integer mtdTxnAchieved) {
        this.date = date;
        this.zone = zone;
        this.zonalHead = zonalHead;
        this.zonalHeadContactDetails = zonalHeadContactDetails;
        this.zonalHeadEmail = zonalHeadEmail;
        this.stateHead = stateHead;
        this.stateHeadContactDetails = stateHeadContactDetails;
        this.stateHeadEmail = stateHeadEmail;
        this.channelManager = channelManager;
        this.channelManagerContactDetails = channelManagerContactDetails;
        this.channelManagerEmail = channelManagerEmail;
        this.channelExecutive = channelExecutive;
        this.channelExecutiveContactDetails = channelExecutiveContactDetails;
        this.channelExecutiveEmail = channelExecutiveEmail;
        this.noOfAssignedAtm = noOfAssignedAtm;
        this.uptimeTarget = uptimeTarget;
        this.mtdUptime = mtdUptime;
        this.mtdTxnTarget = mtdTxnTarget;
        this.mtdTxnAchieved = mtdTxnAchieved;
    }
}
