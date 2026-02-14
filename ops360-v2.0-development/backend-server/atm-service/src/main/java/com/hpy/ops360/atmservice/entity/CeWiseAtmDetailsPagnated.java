package com.hpy.ops360.atmservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CeWiseAtmDetailsPagnated {

	@Id
	@Column(name = "atmid")
	private String atmid;

	@Column(name = "bankname")
	private String bankname;
	
	@Column(name = "grade")
	private String grade;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "machine_status")
	private String machineStatus;
	
	@Column(name = "uptime_status")
	private String uptimeStatus;
	
	@Column(name = "open_tickets")
	private Integer openTickets;
	
	@Column(name = "transaction_trend")
	private String transactionTrend;

	@Column(name = "mtd_performance")
	private String mtdPerformance;

	@Column(name = "uptime_trend")
	private String uptimeTrend;

	@Column(name = "mtd_uptime")
	private Double mtdUptime;

//	@Column(name = "name_of_channel_executive")
//	private String nameOfChannelExecutive;

	@Column(name = "name_of_secondary_channel_executive")
	private String nameOfSecondaryChannelExecutive;

	@Column(name = "last_visited_on")
	private String lastVisitedOn;
	
	@Column(name = "total_records")
	private Integer totalRecords;

	@Column(name = "total_pages")
	private Integer totalPages;
}
