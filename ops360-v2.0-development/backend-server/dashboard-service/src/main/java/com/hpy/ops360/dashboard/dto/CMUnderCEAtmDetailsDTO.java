package com.hpy.ops360.dashboard.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CMUnderCEAtmDetailsDTO extends GenericDto{

	private Long id;

	private String atmid;

	private String bankname;

	private String grade;

	private String address;

	private String machineStatus;

	private Integer openTickets;

	private String errorCategory;

	private String ownership;

	private String ticketAging;

	private String transactionTrend;

	private String mtdPerformance;

	private String uptimeTrend;

	 private String uptimeStatus;

	private String mtdUptime;

	private String nameOfChannelExecutive;

	private String nameOfSecondaryChannelExecutive;

	private String lastVisitedOn;

	// Getters and Setters
}
