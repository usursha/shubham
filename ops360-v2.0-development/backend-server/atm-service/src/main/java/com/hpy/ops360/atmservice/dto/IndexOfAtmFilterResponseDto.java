package com.hpy.ops360.atmservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class IndexOfAtmFilterResponseDto extends GenericDto {

	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private Long id;
	public List<SortFilterResponseDto> sortIndexOfAtmFilters;
	public List<BankFilterResponseDTO> bankIndexOfAtmFilters;
	public List<GradeFilterResponseDTO> gradeIndexOfAtmFilters;
	public List<ATMStatusFilterResponseDTO> atmStatusIndexOfAtmFilters;
	//public List<OwnerFilterResponseDTO> ownerIndexOfAtmFilters;
	public List<UptimeStatusFilterResponseDTO> uptimeStatusIndexOfAtmFilter;
}
