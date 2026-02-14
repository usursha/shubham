package com.hpy.ops360.ticketing.dto;

import java.util.List;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AssignedAtmforCeFilterResponseDto extends GenericDto {

	private static final long serialVersionUID = 1L;
	private List<GradeFilterResponseDTO> gradeFilter;
	private List<TransactionTrendFilterResponseDTO> transactionTrendFilter;
	private List<UptimeTrendFilterResponseDTO> UptimetrendFilter;
	private List<BankFilterResponseDTO> bankFilters;
	private List<SiteNameFilterResponseDTO> locationFilters;

}
