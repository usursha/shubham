package com.hpy.ops360.dashboard.dto;

import lombok.Data;

@Data
public class TargetMtdSummaryDto {

	private Double uptimePer;
	private Double uptimeTargetPer;
	private Double differenceUptimePer;

	private Integer transactionCount;
	private Integer transactionTarget;
	private Double differenceTransactionPer;

}
