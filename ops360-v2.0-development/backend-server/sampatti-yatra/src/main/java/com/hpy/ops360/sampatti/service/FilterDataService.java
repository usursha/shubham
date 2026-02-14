package com.hpy.ops360.sampatti.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hpy.ops360.sampatti.dto.CityDataDto;
import com.hpy.ops360.sampatti.dto.FilterDataRequestDto;
import com.hpy.ops360.sampatti.dto.FilterDataResponseDto;
import com.hpy.ops360.sampatti.dto.SortDataDto;
import com.hpy.ops360.sampatti.dto.StateDataDto;
import com.hpy.ops360.sampatti.dto.ZoneDataDto;
import com.hpy.ops360.sampatti.entity.FilterStateCityData;
import com.hpy.ops360.sampatti.repository.FilterStateCityDataRepostiory;
import com.hpy.ops360.sampatti.repository.SortFilterDataRepostiory;
import com.hpy.ops360.sampatti.repository.ZoneDataRepository;

@Service
public class FilterDataService {

	private FilterStateCityDataRepostiory filterStateCityDataRepostiory;

	private SortFilterDataRepostiory sortFilterDataRepostiory;

	public FilterDataService(FilterStateCityDataRepostiory filterStateCityDataRepostiory,
			ZoneDataRepository zoneDataRepository, SortFilterDataRepostiory sortFilterDataRepostiory) {
		super();
		this.filterStateCityDataRepostiory = filterStateCityDataRepostiory;
		this.sortFilterDataRepostiory = sortFilterDataRepostiory;
	}

	public FilterDataResponseDto getLeaderBoardCeFilterData(FilterDataRequestDto filterDataRequestDto) {
		List<FilterStateCityData> filterData = filterStateCityDataRepostiory.getFilterStateCityData(
				filterDataRequestDto.getSelectedZone(), filterDataRequestDto.getSelectedState(),
				filterDataRequestDto.getSelectedCity(), filterDataRequestDto.getRole());
		FilterDataResponseDto filterResponse = new FilterDataResponseDto();
		filterResponse.setSortingData(sortFilterDataRepostiory.findAll().stream()
				.map(sortData -> new SortDataDto(sortData.getSortId(), sortData.getFilterData())).toList());
//		filterResponse.setZoneDataList(filterData.stream().filter(data -> data.getRecordType().equalsIgnoreCase("ZONE")).map(this::convertToZoneDataDto).distinct().filter(data -> data!=null).toList());
//		filterResponse.setStateDtoList(filterData.stream().filter(data -> data.getRecordType().equalsIgnoreCase("STATE")).map(this::convertToStateDataDto).distinct().filter(data -> data!=null).toList());
//		filterResponse.setCityDataDtoList(filterData.stream().filter(data -> data.getRecordType().equalsIgnoreCase("CITY")).map(this::convertToCityDataDto).distinct().filter(data -> data!=null).toList());
		List<ZoneDataDto> zoneList = filterData.stream().filter(row -> "ZONE".equalsIgnoreCase(row.getRecordType()))
				.map(r -> {
					ZoneDataDto dto = new ZoneDataDto();
					dto.setId(r.getId());
					dto.setZoneData(r.getName());
					dto.setUserCount(r.getUserCount());
					return dto;
				}).collect(Collectors.toList());

		List<StateDataDto> stateList = filterData.stream().filter(r -> "STATE".equalsIgnoreCase(r.getRecordType()))
				.map(r -> {
					StateDataDto dto = new StateDataDto();
					dto.setId(r.getId());
					dto.setStateData(r.getName());
					dto.setUserCount(r.getUserCount());
					return dto;
				}).collect(Collectors.toList());

		List<CityDataDto> cityList = filterData.stream().filter(r -> "CITY".equalsIgnoreCase(r.getRecordType()))
				.map(r -> {
					CityDataDto dto = new CityDataDto();
					dto.setId(r.getId());
					dto.setCityData(r.getName());
					dto.setUserCount(r.getUserCount());
					return dto;
				}).collect(Collectors.toList());

		filterResponse.setZoneDataList(zoneList);
		filterResponse.setStateDtoList(stateList);
		filterResponse.setCityDataDtoList(cityList);

		return filterResponse;
	}

}
