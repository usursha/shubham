package com.hpy.ops360.ticketing.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class AssignedAtmMtdDto implements Serializable {

	private static final long serialVersionUID = 6706830354374045058L;
	private String atmId;
	private String customer; // alias bank
	private String location;
	private String atmCategory; // alias grade
	private Double mtdPerformancePer;
	private Double uptimeTrendPer;
	private Double transactionTrendPer;
}
