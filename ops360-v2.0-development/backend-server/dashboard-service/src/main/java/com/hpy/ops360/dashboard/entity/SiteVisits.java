package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class SiteVisits{

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "ticket_no")
	private String ticketNumber;

	@Column(name = "visit_type")
	private String visitType;

	@Column(name = "code_of_site")
	private String siteCode;

	@Column(name = "site_description")
	private String siteDesc;

	@Column(name = "start_date")
	private String StartDate;

	@Column(name = "end_date")
	private String endDate;
}
