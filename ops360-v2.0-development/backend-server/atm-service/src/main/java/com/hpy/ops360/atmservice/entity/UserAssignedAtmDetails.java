package com.hpy.ops360.atmservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserAssignedAtmDetails {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	private Long id;

	@Column(name = "ATM ID")
	private String atmiId;

	@Column(name = "Grade")
	private String grade;

	@Column(name = "Bank")
	private String bankname;

	@Column(name = "Location")
	private String location;

	@Column(name = "MTD performance")
	private Double mtdPerformance;

	@Column(name = "Uptime Trend")
	private Double uptimeTrend;

	@Column(name = "Transaction trend")
	private Double trendCash;

	@Column(name = "address")
	private String address;
}
