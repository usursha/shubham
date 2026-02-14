package com.hpy.ops360.sampatti.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterDataResponseDto extends GenericDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	
	
	private List<SortDataDto> sortingData;
	private List<ZoneDataDto> zoneDataList;
	private List<StateDataDto> stateDtoList;
	private List<CityDataDto> cityDataDtoList;
	
	public FilterDataResponseDto(List<SortDataDto> sortingData, List<ZoneDataDto> zoneDataList,
			List<StateDataDto> stateDtoList, List<CityDataDto> cityDataDtoList) {
		super();
		this.sortingData = sortingData;
		this.zoneDataList = zoneDataList;
		this.stateDtoList = stateDtoList;
		this.cityDataDtoList = cityDataDtoList;
	}
	
	
	

}
