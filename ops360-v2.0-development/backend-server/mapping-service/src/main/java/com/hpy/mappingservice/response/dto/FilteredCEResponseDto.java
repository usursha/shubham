package com.hpy.mappingservice.response.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.mappingservice.request.dto.IdNameCEDto;
import com.hpy.mappingservice.request.dto.SortDatafilterCEDto;
import com.hpy.mappingservice.request.dto.TargetRangeCEDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilteredCEResponseDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	private List<SortDatafilterCEDto> sortingData;
	private TargetRangeCEDto minMaxValues;
//    private List<IdNameDto> zoneList;
//    private List<IdNameDto> stateList;
    private List<IdNameCEDto> cityList;
	
    public FilteredCEResponseDto(List<SortDatafilterCEDto> sortingData, TargetRangeCEDto minMaxValues,
			List<IdNameCEDto> cityList) {
		super();
		this.sortingData = sortingData;
		this.minMaxValues = minMaxValues;
		this.cityList = cityList;
	}
	
    
	
    
    
    
}

