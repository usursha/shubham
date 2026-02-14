package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

public class SummaryReportResponseDto extends GenericDto {
	
	@JsonIgnore
	private Long id;

	private static final long serialVersionUID = 1L;
	private String base64Excel;

	public SummaryReportResponseDto(String base64Excel) {
		this.base64Excel = base64Excel;
	}

	// Getter and setter
	public String getBase64Excel() {
		return base64Excel;
	}

	public void setBase64Excel(String base64Excel) {
		this.base64Excel = base64Excel;
	}
}
