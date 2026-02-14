package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketsRaisedEntity {

	@Id
	private Long id;
	
	@Column(name = "total_open_tickets")
	private String totalOpenTickets;

	@Column(name = "todays_tickets_color")
	private String todaysTicketColor;
}
