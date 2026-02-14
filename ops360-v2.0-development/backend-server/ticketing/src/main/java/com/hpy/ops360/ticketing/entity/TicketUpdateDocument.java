package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TicketUpdateDocument {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "atm_id")
	private String atmId;

	@Column(name = "ticket_number")
	private String ticketNumber;

	@Column(name = "document")
	private String document;

	@Column(name = "document_name")
	private String documentName;

	@Column(name = "document1")
	private String document1;

	@Column(name = "document1_name")
	private String document1Name;

	@Column(name = "document2")
	private String document2;

	@Column(name = "document2_name")
	private String document2Name;

	@Column(name = "document3")
	private String document3;

	@Column(name = "document3_name")
	private String document3Name;

	@Column(name = "document4")
	private String document4;

	@Column(name = "document4_name")
	private String document4Name;

}
