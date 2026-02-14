package com.hpy.ops360.ticketing.cm.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CEWiseUptimeReportEntity {
	
	@Id
	@JsonIgnore
    private Long srno;

    // Date and Zone Information
	@Column(name ="date")
    private String date;       // Transaction Date
	
	@Column(name ="zone")
    private String zone;                  // Zone

    // Zonal Head Information
	@Column(name ="zonal_head")
    private String zonalHead;             // Zonal Head
	
	@Column(name ="zonal_head_contact_details")
    private String zonalHeadContactDetails; // Zonal Head Contact Details
	
	@Column(name ="zonal_head_email")
    private String zonalHeadEmail;        // Zonal Head Email

    // State Head Information
	@Column(name ="state_head")
    private String stateHead;             // State Head
	
	@Column(name ="state_head_contact_details")
    private String stateHeadContactDetails; // State Head Contact Details
	
	@Column(name ="state_head_email")
    private String stateHeadEmail;        // State Head Email

    // Channel Manager Information
	@Column(name ="channel_manager")
    private String channelManager;         // Channel Manager
	
	@Column(name ="channel_manager_contact_details")
    private String channelManagerContactDetails; // Channel Manager Contact Details
	
	@Column(name ="channel_manager_email")
    private String channelManagerEmail;   // Channel Manager Email

    // Channel Executive Information
	@Column(name ="channel_executive")
    private String channelExecutive;      // Channel Executive
	
	@Column(name ="channel_executive_contact_details")
    private String channelExecutiveContactDetails; // Channel Executive Contact Details
	
	@Column(name ="channel_executive_email")
    private String channelExecutiveEmail; // Channel Executive Email

    // ATM and Performance Metrics
	@Column(name ="no_of_assigned_atm")
    private Integer noOfAssignedAtm;       // Number of Assigned ATMs
	
	@Column(name ="uptime_target")
    private Float uptimeTarget;            // Uptime Target
	
	@Column(name ="mtd_uptime")
    private String mtdUptime;              // MTD Uptime
	
	@Column(name ="mtd_txn_target")
    private Integer mtdTxnTarget;          // MTD Transaction Target
	
	@Column(name ="mtd_txn_achieved")
    private Integer mtdTxnAchieved;        // MTD Transactions Achieved
}
