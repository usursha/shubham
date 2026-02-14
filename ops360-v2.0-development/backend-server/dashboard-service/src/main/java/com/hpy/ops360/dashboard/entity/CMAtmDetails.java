package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Entity
@Data
@NoArgsConstructor
public class CMAtmDetails {

	@Id
	private Long id;

	private String atmid;

	private String bankname;

	private String grade;

	private String address;

	private String machineStatus;

	private Integer openTickets;

//	private String errorCategory;

//	private String ownership;

	private String uptimeStatus;

	private String transactionTrend;

	private String mtdPerformance;

	private String uptimeTrend;

	private String mtdUptime;

	private String nameOfChannelExecutive;

	private String nameOfSecondaryChannelExecutive;

	private String lastVisitedOn;

}
