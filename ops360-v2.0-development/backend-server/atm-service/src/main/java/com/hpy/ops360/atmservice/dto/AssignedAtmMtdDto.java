package com.hpy.ops360.atmservice.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssignedAtmMtdDto extends GenericDto implements Serializable {

	@JsonIgnore
	private Long id;
	
	private static final long serialVersionUID = 6706830354374045058L;
	private String atmId;
	private String customer; // alias bank
	private String location;
	private String atmCategory; // alias grade
	private Double mtdPerformancePer;
	private Double uptimeTrendPer;
	private Double transactionTrendPer;
	public AssignedAtmMtdDto(String atmId, String customer, String location, String atmCategory,
			Double mtdPerformancePer, Double uptimeTrendPer, Double transactionTrendPer) {
		super();
		this.atmId = atmId;
		this.customer = customer;
		this.location = location;
		this.atmCategory = atmCategory;
		this.mtdPerformancePer = mtdPerformancePer;
		this.uptimeTrendPer = uptimeTrendPer;
		this.transactionTrendPer = transactionTrendPer;
	}
	
	
}
