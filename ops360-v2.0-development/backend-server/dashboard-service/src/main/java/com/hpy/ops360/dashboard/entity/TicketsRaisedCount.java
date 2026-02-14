package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TicketsRaisedCount {
	@Id
	@Column(name = "sr_no")
	private int sr_no;

	@Column(name = "Breakdown")
	private int breakdown;
	@Column(name = "Service")
	private int service;
	@Column(name = "Updated")
	private int updated;
	@Column(name = "Total")
	private int total;

}
