package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IndexOfUpDawnAtmForCmResponse {

	private String atmid;
	private String machine_status;
	private String bank_name;
	private String atm_address;
	private String grade;
	//Added Field
	private String OpenTickets;
	private String ErrorCategory;// AS eventCode-
	
	private String TransactionTrend;
	private String UptimeStatus;
	private String UptimeTrend;
	private String MTDTxnPerformance;
	private String MTDUptime;
	private String NameOfChannelExecutive;
	private String NameOfSecondaryChannelExcecutive;
	private String LastVisitedOn;
	
	
}
