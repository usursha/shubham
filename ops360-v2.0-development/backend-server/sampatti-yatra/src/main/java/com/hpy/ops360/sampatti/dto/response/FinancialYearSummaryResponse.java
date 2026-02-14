package com.hpy.ops360.sampatti.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialYearSummaryResponse {
	private Long yearId;
	private String financialYear;
	private Double totalIncentiveAmount;
}
