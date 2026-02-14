package com.hpy.ops360.sampatti.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.hpy.ops360.sampatti.dto.FilterUserMasterRequestDto;
import com.hpy.ops360.sampatti.dto.FilteredResponseDto;
import com.hpy.ops360.sampatti.dto.IdNameDto;
import com.hpy.ops360.sampatti.dto.SortDatafilterDto;
import com.hpy.ops360.sampatti.dto.TargetRangeDto;
import com.hpy.ops360.sampatti.entity.FilteredItemEntity;
import com.hpy.ops360.sampatti.repository.FilterUserMasterRepository;
import com.hpy.ops360.sampatti.repository.SortFilterDataRepository;
import com.hpy.ops360.sampatti.repository.TargetRangeRepository;
import com.hpy.ops360.sampatti.entity.TargetRange;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FilterUserMasterService {

    
    private FilterUserMasterRepository repository;
    
    
    private SortFilterDataRepository sortFilterDataRepository;
    
    
    private TargetRangeRepository rangeRepository;

    public FilterUserMasterService(FilterUserMasterRepository repository,
			SortFilterDataRepository sortFilterDataRepository, TargetRangeRepository rangeRepository) {
		super();
		this.repository = repository;
		this.sortFilterDataRepository = sortFilterDataRepository;
		this.rangeRepository = rangeRepository;
	}



	public FilteredResponseDto getfilterData(FilterUserMasterRequestDto request) {
        log.info("Fetching filtered user master data with request: zoneList={}, stateList={}, cityList={}",
                request.getZoneList(), request.getStateList(), request.getCityList());

        List<FilteredItemEntity> resultList = repository.getFilteredUserMaster(
                request.getZoneList(), request.getStateList(), request.getCityList(), request.getDesignation());

        log.info("Raw data fetched from repository: {}", resultList);

        List<IdNameDto> zoneList = resultList.stream()
                .filter(e -> "ZONE".equalsIgnoreCase(e.getRecordType()))
                .map(e -> new IdNameDto(e.getId(), e.getName(), e.getCount()))
                .collect(Collectors.toList());

        List<IdNameDto> stateList = resultList.stream()
                .filter(e -> "STATE".equalsIgnoreCase(e.getRecordType()))
                .map(e -> new IdNameDto(e.getId(), e.getName(), e.getCount()))
                .collect(Collectors.toList());

        List<IdNameDto> cityList = resultList.stream()
                .filter(e -> "CITY".equalsIgnoreCase(e.getRecordType()))
                .map(e -> new IdNameDto(e.getId(), e.getName(), e.getCount()))
                .collect(Collectors.toList());
        
        
        List<SortDatafilterDto> sortFilterList = sortFilterDataRepository.findAll().stream()
                .map(e -> new SortDatafilterDto(e.getSortId(), e.getFilterData()))
                .collect(Collectors.toList());
        
//        TargetRangeDto targetRangeto=new TargetRangeDto();
        Optional<TargetRange> optionalTargetRange = rangeRepository.findFirstByOrderBySrnoAsc();
        TargetRangeDto targetRangeto = new TargetRangeDto();

        if (optionalTargetRange.isPresent()) {
            TargetRange targetRange = optionalTargetRange.get();
            targetRangeto.setMinAchieved(targetRange.getMinAchieved());
            targetRangeto.setMaxAchieved(targetRange.getMaxAchieved());
        }

        log.info("Filtered zone list: {}", zoneList);
        log.info("Filtered state list: {}", stateList);
        log.info("Filtered city list: {}", cityList);

        return new FilteredResponseDto(sortFilterList,targetRangeto,zoneList, stateList, cityList);
    }
}
