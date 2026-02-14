package com.hpy.ops360.ticketing.service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CMTaskDetailsApproveOrRejectTicketByKafka {

	private static final long serialVersionUID = -3328590094816811088L;

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "atm_id")
	private String atmId;

	@Column(name = "comment")
	private String comment;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "diagnosis")
	private String diagnosis;

	@JsonIgnore
	@Column(name = "document")
	private String document;

	@Column(name = "document_name")
	private String documentName;

	@Column(name = "reason")
	private String reason;

	@Column(name = "ticket_number")
	private String ticketNumber;

	@Column(name = "status")
	private String status;

	@Column(name = "ref_no")
	private String refNo;

	@JsonIgnore
	@Column(name = "document1")
	private String document1;

	@Column(name = "document1_name")
	private String document1Name;

	@JsonIgnore
	@Column(name = "document2")
	private String document2;

	@Column(name = "document2_name")
	private String document2Name;

	@JsonIgnore
	@Column(name = "document3")
	private String document3;

	@Column(name = "document3_name")
	private String document3Name;

	@JsonIgnore
	@Column(name = "document4")
	private String document4;

	@Column(name = "document4_name")
	private String document4Name;

	@Column(name = "atm_source")
	private String atmSource;

}
