package com.hpy.ops360.ticketing.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AssignedAtmFilterResponseDto extends GenericDto{
	
	@JsonIgnore
	private Long id;

	private List<AtmDetailFilterResponseDto> atmFilters;
	private List<BankFilterResponseDTO> bankFilters;
	private List<GetLocationNameofCEFilterResponseDto> locationName;
}
