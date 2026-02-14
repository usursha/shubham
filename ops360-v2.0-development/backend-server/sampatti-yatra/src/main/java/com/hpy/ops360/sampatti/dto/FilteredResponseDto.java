package com.hpy.ops360.sampatti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilteredResponseDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	private List<SortDatafilterDto> sortingData;
	private TargetRangeDto minMaxValues;
    private List<IdNameDto> zoneList;
    private List<IdNameDto> stateList;
    private List<IdNameDto> cityList;
	public FilteredResponseDto(List<SortDatafilterDto> sortingData, TargetRangeDto minMaxValues, List<IdNameDto> zoneList,
			List<IdNameDto> stateList, List<IdNameDto> cityList) {
		super();
		this.sortingData = sortingData;
		this.minMaxValues = minMaxValues;
		this.zoneList = zoneList;
		this.stateList = stateList;
		this.cityList = cityList;
	}
    
    
}

