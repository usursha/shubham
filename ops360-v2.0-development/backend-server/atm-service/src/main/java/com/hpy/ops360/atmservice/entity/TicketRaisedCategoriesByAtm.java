package com.hpy.ops360.atmservice.entity;

import java.sql.Timestamp;

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
public class TicketRaisedCategoriesByAtm {

	@Id
	private String id;

	@Column(name = "total_open_tickets")
	private String totalOpenTickets;

	@Column(name = "todays_tickets_color")
	private String todaysTicketsColor;

}
