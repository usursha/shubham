package com.hpy.ops360.ticketing.response;

import java.util.List;


import com.hpy.ops360.ticketing.dto.GenericResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterOptionsResponse extends GenericResponseDto {
	private List<SortOption> sortOptions;
	private List<CreationDateOption> creationDateOptions;
//
}