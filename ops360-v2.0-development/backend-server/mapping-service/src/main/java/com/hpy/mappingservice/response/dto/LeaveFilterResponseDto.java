package com.hpy.mappingservice.response.dto;

import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

@Data
public class LeaveFilterResponseDto extends GenericDto{
	@JsonIgnore
	private Long id;
    private List<LeaveDateRangeFilterDto> leaveDateRangeFilters;
    private List<CityFilterDto> cityFilters;
}
