package com.hpy.ops360.sampatti.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncentiveResponse extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
    private List<FinancialYearSummaryResponse> financialYearSummaries;
    private MinMaxValuesResponse minMaxValues;
    private List<SortingData> sortingData;
    private List<UserIncentiveRecordResponse> userIncentives;
    
	public IncentiveResponse(List<FinancialYearSummaryResponse> financialYearSummaries,
			MinMaxValuesResponse minMaxValues, List<SortingData> sortingData,
			List<UserIncentiveRecordResponse> userIncentives) {
		super();
		this.financialYearSummaries = financialYearSummaries;
		this.minMaxValues = minMaxValues;
		this.sortingData = sortingData;
		this.userIncentives = userIncentives;
	}
    
    
}