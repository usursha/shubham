package com.hpy.ops360.ticketing.cm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TicketSearchSuggestion {
	
	@Id
	private Long srno;
	
	@Column(name = "ticket_number")
	private String ticketNumber;

}
