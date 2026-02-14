package com.hpy.mappingservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.google.common.base.Optional;
import com.hpy.mappingservice.entity.FilteredItemCEEntity;
import com.hpy.mappingservice.entity.IndexOfCeEntity;
import com.hpy.mappingservice.entity.SortFilterCEDataEntity;
import com.hpy.mappingservice.entity.TargetRangeCE;
import com.hpy.mappingservice.repository.FilterUserMasterCERepository;
import com.hpy.mappingservice.repository.IndexOfCeRepository;
import com.hpy.mappingservice.repository.SortFilterCEDataRepository;
import com.hpy.mappingservice.repository.TargetRangeCERepository;
import com.hpy.mappingservice.request.dto.IdNameCEDto;
import com.hpy.mappingservice.request.dto.IndexOfCeRequestDto;
import com.hpy.mappingservice.request.dto.IndexOfCeSearchRequestDto;
import com.hpy.mappingservice.request.dto.SortDatafilterCEDto;
import com.hpy.mappingservice.request.dto.TargetRangeCEDto;
import com.hpy.mappingservice.response.dto.CEIndexWrapperDto;
import com.hpy.mappingservice.response.dto.FilteredCEResponseDto;
import com.hpy.mappingservice.response.dto.IndexOfCeResponseDto;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class IndexOfCeService {
	
	
	@Autowired
	private IndexOfCeRepository indexOfCeRepository;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private SortFilterCEDataRepository sortFilterCEDataRepository;
	
	@Autowired
	private TargetRangeCERepository targetRangeCERepository;
	
	@Autowired
	private FilterUserMasterCERepository CEfilterRepository;
	
    
    
    public CEIndexWrapperDto getIndexOfCeDetails(IndexOfCeRequestDto requestDTO) {
        log.info("Attempting to retrieve CE index details for cmUserid: {}"); 
        List<IndexOfCeEntity> entities = indexOfCeRepository.getIndexOfCeDetails(
            loginService.getLoggedInUser(),
            requestDTO.getPageNumber(),
            requestDTO.getPageSize(),
            requestDTO.getSortOption(), 
            requestDTO.getAvailability(),
            requestDTO.getMtdTxnTargetStart(),
            requestDTO.getMtdTxnTargetEnd(),
            requestDTO.getCity(),
            requestDTO.getArea(),
            requestDTO.getSearch()
        );

        CEIndexWrapperDto wrapperDto = new CEIndexWrapperDto();
        
        if (entities == null || entities.isEmpty()) {
            log.info("No CE index entities found for cmUserid: {} city: {}", 
                    loginService.getLoggedInUser(), requestDTO.getCity());
            wrapperDto.setTotalCount(0);
        //    wrapperDto.setSampatiTxnDate("");
            wrapperDto.setRecord(List.of());
            return wrapperDto;
        }  
        
        List<IndexOfCeResponseDto> responseList = entities.stream()
                .map(entity -> {                   
                	IndexOfCeResponseDto dto = new IndexOfCeResponseDto();
                    dto.setSrNo(entity.getSrNo());
                    dto.setChannelExecutiveName(entity.getChannelExecutiveName() != null ? 
                        entity.getChannelExecutiveName() : "");
                    dto.setEmployeeCode(entity.getEmployeeCode() != null ? 
                        entity.getEmployeeCode() : "");
                    dto.setCircleArea(entity.getCircleArea() != null ? 
                        entity.getCircleArea() : "");
                    dto.setCity(entity.getCity() != null ? entity.getCity() : "");
                    dto.setAvailabilityStatus(entity.getAvailabilityStatus() != null ? 
                        entity.getAvailabilityStatus() : "");
                    dto.setAssignedNumberOfATMs(entity.getAssignedNumberOfATMs() != null ? 
                        entity.getAssignedNumberOfATMs() : "");
                    
                    // Handle atmAssignedIDs as array
                    if (entity.getAtmAssignedIDs() != null && !entity.getAtmAssignedIDs().trim().isEmpty()) {
                        dto.setAtmAssignedIDs(Arrays.asList(entity.getAtmAssignedIDs().split(",")));
                    } else {
                        dto.setAtmAssignedIDs(List.of());
                    }
                    
                    dto.setDownMachines(entity.getDownMachines() != null ? 
                        entity.getDownMachines() : "");
                    dto.setUptimeStatus(entity.getUptimeStatus() != null ? 
                        entity.getUptimeStatus() : "");
                    dto.setMtdUptime(entity.getMtdUptime() != null ? 
                        entity.getMtdUptime() : "");
                    dto.setTransaction(entity.getTransaction() != null ? 
                        entity.getTransaction() : "");
                    dto.setTarget(entity.getTarget() != null ? 
                        entity.getTarget() : "");
                    dto.setPercentageChange(entity.getPercentageChange() != null ? 
                        entity.getPercentageChange() : "");
                        
                    return dto;
                })
                .collect(Collectors.toList());

        wrapperDto.setTotalCount(!entities.isEmpty() && entities.get(0).getTotalRecords() != null ? 
            Integer.parseInt(entities.get(0).getTotalRecords()) : responseList.size());
    //    wrapperDto.setSampatiTxnDate(""); // Set appropriate value or leave empty as per requirement
        wrapperDto.setRecord(responseList);

        log.info("Successfully retrieved and mapped {} CE index entries for cmUserid: {}", 
                responseList.size(), loginService.getLoggedInUser());
        return wrapperDto;
    }
    
    
    public List<String> getIndexOfCeSearchDetails(IndexOfCeSearchRequestDto requestDTO) {
        log.info("Attempting to retrieve CE index details for cmUserid: {}"); 
        List<IndexOfCeEntity> searchListData = indexOfCeRepository.getIndexOfCeSearchDetails(
            loginService.getLoggedInUser(),
            requestDTO.getSortOption(), 
            requestDTO.getAvailability(),
            requestDTO.getMtdTxnTargetStart(),
            requestDTO.getMtdTxnTargetEnd(),
            requestDTO.getCity(),
            requestDTO.getArea()
        );

        if (searchListData == null || searchListData.isEmpty()) {
            log.info("No CE index entities found for cmUserid: {}", loginService.getLoggedInUser());
            return new ArrayList<>();
        }
        
        List<String> atmIds = new ArrayList<>();
        Set<String> ceNames = new LinkedHashSet<>();
        
        searchListData.forEach(entity -> {
            // Collect channel executive names
            if (entity.getChannelExecutiveName() != null && !entity.getChannelExecutiveName().trim().isEmpty()) {
                ceNames.add(entity.getChannelExecutiveName().trim());
            }
            
            // Collect ATM IDs (split by comma and clean up)
            if (entity.getAtmAssignedIDs() != null && !entity.getAtmAssignedIDs().trim().isEmpty()) {
                String[] atmIdArray = entity.getAtmAssignedIDs().split(",");
                for (String atmId : atmIdArray) {
                    String cleanAtmId = atmId.trim();
                    if (!cleanAtmId.isEmpty()) {
                        atmIds.add(cleanAtmId);
                    }
                }
            }
        });

        List<String> uniqueAtmIds = atmIds.stream()
                .distinct()
                .collect(Collectors.toList());

        // Combine ATM IDs first, then CE names at the end
        List<String> uniqueSearchData = new ArrayList<>();
        uniqueSearchData.addAll(uniqueAtmIds);
        uniqueSearchData.addAll(ceNames);

        log.info("Successfully retrieved and processed {} unique search entries for cmUserid: {}", 
                 uniqueSearchData.size(), loginService.getLoggedInUser());
        
        return uniqueSearchData;
        
    }
    
    public FilteredCEResponseDto getfilterData() {
        log.info("******* Inside getfilterData Method *********");
        
        try {
            String loggedInUser = loginService.getLoggedInUser();
            if (loggedInUser == null || loggedInUser.trim().isEmpty()) {
                log.warn("Logged in user is null or empty, returning empty filter data");
                return createEmptyFilteredCEResponse();
            }
            
            List<SortDatafilterCEDto> sortFilterList = new ArrayList<>();
            try {
                List<SortFilterCEDataEntity> sortEntities = sortFilterCEDataRepository.findAll();
                if (sortEntities != null && !sortEntities.isEmpty()) {
                    sortFilterList = sortEntities.stream()
                            .filter(Objects::nonNull)
                            .filter(e -> e.getSortId() != null && e.getFilterData() != null)
                            .map(e -> new SortDatafilterCEDto(e.getSortId(), e.getFilterData()))
                            .collect(Collectors.toList());
                }
            } catch (Exception e) {
                log.error("Error fetching sort filter data", e);
            }
            
            TargetRangeCEDto targetRangeto = new TargetRangeCEDto();
            try {
                Optional<TargetRangeCE> optTargetRange = targetRangeCERepository.findFirstByOrderBySrnoAsc();
                if (optTargetRange.isPresent()) {
                    TargetRangeCE targetRange = optTargetRange.get();
                    if (targetRange != null) {
                        targetRangeto.setTargetStart(targetRange.getTargetStart());
                        targetRangeto.setTargetEnd(targetRange.getTargetEnd());
                    }
                } else {
                    log.warn("No target range data found, using default values");
                    targetRangeto.setTargetStart(0);
                    targetRangeto.setTargetEnd(200);
                }
            } catch (Exception e) {
                log.error("Error fetching target range data", e);
                targetRangeto.setTargetStart(0);
                targetRangeto.setTargetEnd(200);
            }
            
            List<IdNameCEDto> cityList = new ArrayList<>();
            try {
                List<FilteredItemCEEntity> resultList = CEfilterRepository.getCEfilteredUserMaster(loggedInUser);
                log.info("Raw data fetched from repository: {}", resultList);

                if (resultList != null && !resultList.isEmpty()) {
                    cityList = resultList.stream()
                            .filter(Objects::nonNull)
                            .filter(e -> "CITY".equalsIgnoreCase(e.getRecordType()))
                            .filter(e -> e.getCityId() != null && e.getCity() != null)
                            .filter(e -> !e.getCity().trim().isEmpty())
                            .map(e -> new IdNameCEDto(e.getCityId(), e.getCity(), e.getCount()))
                            .collect(Collectors.toList());
                }
            } catch (Exception e) {
                log.error("Error fetching filtered CE data for user: {}", loggedInUser, e);
            }

            log.info("Filtered city list: {}", cityList);
            
            return new FilteredCEResponseDto(sortFilterList, targetRangeto, cityList);
            
        } catch (Exception e) {
            log.error("Unexpected error in getfilterData method", e);
            return createEmptyFilteredCEResponse();
        }
    }
    
    private FilteredCEResponseDto createEmptyFilteredCEResponse() {
        TargetRangeCEDto defaultTargetRange = new TargetRangeCEDto();
        defaultTargetRange.setTargetStart(0);
        defaultTargetRange.setTargetEnd(100);
        
        return new FilteredCEResponseDto(
            new ArrayList<>(),  
            defaultTargetRange, 
            new ArrayList<>()  
        );
    
    
    } 
    
}
    
    