package com.hpy.ops360.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteVisitsDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
	private Long srNo;

	private String ticketNumber;

	private String visitType;

	private String siteCode;

	private String siteDesc;

	private String StartDate;

	private String endDate;
	
	public SiteVisitsDto(Long srNo, String ticketNumber, String visitType, String siteCode, String siteDesc,
			String startDate, String endDate) {
		this.srNo = srNo;
		this.ticketNumber = ticketNumber;
		this.visitType = visitType;
		this.siteCode = siteCode;
		this.siteDesc = siteDesc;
		StartDate = startDate;
		this.endDate = endDate;
	}
}
