package com.hpy.ops360.ticketing.cm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TicketWithoutDocument {
	
	@Id
	@Column(name = "srno")
	private Integer srno;
	
	@Column(name = "ticket_number")
	private String ticketNumber;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "ticket_type")
	private Integer ticketType;
	
	@Column(name = "eta_expired")
	private Integer etaExpired;
	
	@Column(name = "created_date")
	private String createdDate;
}
