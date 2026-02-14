package com.hpy.ops360.atmservice.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AtmListData_forSearchResponseDto extends GenericDto {

	private String atmid;
	private String bankname;
	private String grade;
	private String address;
	private String machineStatus;
	private String uptimeStatus;
	private Integer openTickets;
	private String transactionTrend;
	private String mtdPerformance;
	private String uptimeTrend;
	private Double mtdUptime;
	private String nameOfChannelExecutive;
	private String nameOfSecondaryChannelExecutive;
	private String lastVisitedOn;
}