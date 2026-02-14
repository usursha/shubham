package com.hpy.ops360.sampatti.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFinancialYearIncentiveDto extends GenericDto {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2914268153435523471L;
	@JsonIgnore
	private Long id;
	@JsonIgnore
	protected String createdBy;
	@JsonIgnore
	protected LocalDateTime createdAt;
	@JsonIgnore
	protected String modifiedBy;
	@JsonIgnore
	protected LocalDateTime modifiedAt;
	private Long srno;
    private String year;
    private Double incentiveAmount;
	public UserFinancialYearIncentiveDto(Long srno, String year, Double incentiveAmount) {
		super();
		this.srno = srno;
		this.year = year;
		this.incentiveAmount = incentiveAmount;
	}
    
    
}
