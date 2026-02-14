package com.hpy.ops360.ticketing.cm.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class TicketdetailsEntity {
	
	@Id
	@Column(name = "sr_no")
    private Long srNo;
	@Column(name = "ticket_number")
    private String ticketNumber;
    @Column(name = "ce_user_name")
    private String ceUserName;
    @Column(name = "atm_id")
    private String atmId;
    private String owner;
    @Column(name = "subcall_type")
    private String subcallType;
    @Column(name = "eta_date_time")
    private LocalDateTime etaDateTime;
    private String vendor;
    private String eventcode;
    @Column(name = "travel_eta_datetime")
    private String travelEtaDatetime;
    @Column(name = "down_time")
    private String downTime;
    @Column(name = "travel_hours")
    private String travelHours;
    @Column(name = "flag_status")
    private String flagStatus;
    private LocalDateTime flag_status_inserttime;
    @Column(name = "created_time")
    private String createdTime;
    private String border;
    private String fill;
}
