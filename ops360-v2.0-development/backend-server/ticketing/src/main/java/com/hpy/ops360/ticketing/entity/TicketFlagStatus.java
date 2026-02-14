package com.hpy.ops360.ticketing.entity;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "ticket_flag_status")
public class TicketFlagStatus {

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	// private Long id;

	@Column(name = "ce_user_id")
	private String ceUserId;

	@Column(name = "ticket_number")
	private String ticketNumber;

	@Column(name = "flag_status")
	private int flagStatus;

	// @Column(name = "flag_status_inserttime")
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date flag_status_inserttime;

	@Column(name = "flag_status_inserttime")
	private LocalDateTime flagStatusInsertTime;
}
