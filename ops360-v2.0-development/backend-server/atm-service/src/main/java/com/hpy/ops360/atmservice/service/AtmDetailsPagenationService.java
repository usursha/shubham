package com.hpy.ops360.atmservice.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.AtmDataDetailsDto;
import com.hpy.ops360.atmservice.dto.CeWiseAtmDataDetailsDto;
import com.hpy.ops360.atmservice.dto.LoginService;
import com.hpy.ops360.atmservice.entity.AtmDetailsPagnated;
import com.hpy.ops360.atmservice.entity.CeWiseAtmDetailsPagnated;
import com.hpy.ops360.atmservice.entity.CeWiseAtmSearchDetails;
import com.hpy.ops360.atmservice.repository.AtmDetailsPagnatedRepository;
import com.hpy.ops360.atmservice.repository.CeWiseAtmDetailsPagnatedRepository;
import com.hpy.ops360.atmservice.repository.CeWiseAtmSearchDetailsRepository;
import com.hpy.ops360.atmservice.request.AtmPaginationRequestDTO;
import com.hpy.ops360.atmservice.request.CeWiseAtmPaginationRequestDTO;
import com.hpy.ops360.atmservice.request.CeWiseAtmSearchRequestDTO;
import com.hpy.ops360.atmservice.response.AtmDetailsPagenationResponseDTO;
import com.hpy.ops360.atmservice.response.CeWiseAtmDetailsPagenationResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AtmDetailsPagenationService {

	@Autowired
	private AtmDetailsPagnatedRepository atmDetailsRepository;
	
	@Autowired
	private CeWiseAtmDetailsPagnatedRepository ceWiseAtmDetailsPagnatedRepository;
	
	@Autowired
	private CeWiseAtmSearchDetailsRepository searchDetailsRepository;

	@Autowired
	private LoginService loginService;

	public AtmDetailsPagenationResponseDTO getAtmData(AtmPaginationRequestDTO request) {
        String userId = loginService.getLoggedInUser();
        
        log.info("Search request - UserId: {}, SearchText: {}, PageNumber: {}, PageSize: {}", 
                userId, request.getSearchText(), request.getPageNumber(), request.getPageSize());
        
        List<AtmDetailsPagnated> result = atmDetailsRepository.getAtmData(
            userId,
            request.getPageNumber(),
            request.getPageSize(),
            request.getSortOption(),
            request.getBank(),
            request.getGrade(),
            request.getStatus(),
            request.getUptimeStatus(),
            request.getSearchText() 
        );

        // Convert entity results to DTOs
        List<AtmDataDetailsDto> atmDataList = result.stream()
            .map(this::mapToAtmDataDTO)
            .collect(Collectors.toList());

        // Create pagination 
        int totalRecords = result.isEmpty() ? 0 : result.get(0).getTotalRecords();
        int totalPages = (int) Math.ceil((double) totalRecords / request.getPageSize());

        AtmDetailsPagenationResponseDTO.PaginationMetadataDTO pagination = 
            new AtmDetailsPagenationResponseDTO.PaginationMetadataDTO();
        pagination.setCurrentPage(request.getPageNumber());
        pagination.setPageSize(request.getPageSize());
        pagination.setTotalRecords(totalRecords);
        pagination.setTotalPages(totalPages);

        AtmDetailsPagenationResponseDTO response = new AtmDetailsPagenationResponseDTO();
        response.setData(atmDataList);
        response.setPagination(pagination);

        return response;
    }

    private AtmDataDetailsDto mapToAtmDataDTO(AtmDetailsPagnated entity) {
        AtmDataDetailsDto dto = new AtmDataDetailsDto();
        dto.setAtmId(entity.getAtmid()); 
        dto.setBankName(entity.getBankname()); 
        dto.setGrade(entity.getGrade()); 
        dto.setAddress(entity.getAddress()); 
        dto.setMachineStatus(entity.getMachineStatus()); 
        dto.setUptimeStatus(entity.getUptimeStatus()); 
        dto.setOpenTickets(entity.getOpenTickets()); 
        dto.setTransactionTrend(entity.getTransactionTrend()); 
        dto.setMtdPerformance(entity.getMtdPerformance()); 
        dto.setUptimeTrend(entity.getUptimeTrend());
        dto.setMtdUptime(entity.getMtdUptime()); 
        dto.setChannelExecutive(entity.getNameOfChannelExecutive()); 
        dto.setSecondaryChannelExecutive(entity.getNameOfSecondaryChannelExecutive()); 
        dto.setLastVisitedOn(entity.getLastVisitedOn()); 
        return dto;
    }
    
    public CeWiseAtmDetailsPagenationResponseDTO getCeWiseAtmDetailsData(CeWiseAtmPaginationRequestDTO request) {
        log.info("******* Inside getCeWiseAtmDetailsData Method Service Class *********");
        log.info("Search request - UserId: {}, SearchText: {}, PageNumber: {}, PageSize: {}", 
                request.getUserId(), request.getSearchText(), request.getPageNumber(), request.getPageSize());
        
        List<CeWiseAtmDetailsPagnated> result = ceWiseAtmDetailsPagnatedRepository.getCeWiseAtmDetailsData(
            request.getUserId(),
            request.getPageNumber(),
            request.getPageSize(),
            request.getSortOption(),
            request.getBank(),
            request.getGrade(),
            request.getStatus(),
            request.getUptimeStatus(),
            request.getSearchText() 
        );
        
        if (result == null) {
            log.info("Repository returned null result for userId: {}", request.getUserId());
            CeWiseAtmDetailsPagenationResponseDTO response = new CeWiseAtmDetailsPagenationResponseDTO();
            response.setTotalRecords(0);
            response.setRecord(new ArrayList<>());
            return response;
        }

        List<CeWiseAtmDataDetailsDto> atmDataList = result.stream()
                .filter(Objects::nonNull)
                .map(this::mapToCeWiseAtmDataDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        int totalRecords = (!result.isEmpty() && result.get(0) != null && result.get(0).getTotalRecords() != null) ? 
                result.get(0).getTotalRecords() : 0;

        CeWiseAtmDetailsPagenationResponseDTO response = new CeWiseAtmDetailsPagenationResponseDTO();
        response.setTotalRecords(totalRecords);
        response.setRecord(atmDataList != null ? atmDataList : new ArrayList<>());

        log.info("Successfully processed {} ATM records out of {} total records",
                 atmDataList != null ? atmDataList.size() : 0, totalRecords);

        return response;
    }
    
    private CeWiseAtmDataDetailsDto mapToCeWiseAtmDataDTO(CeWiseAtmDetailsPagnated entity) {
        CeWiseAtmDataDetailsDto dto = new CeWiseAtmDataDetailsDto();
        dto.setAtmId(entity.getAtmid()); 
        dto.setBankName(entity.getBankname()); 
        dto.setGrade(entity.getGrade()); 
        dto.setAddress(entity.getAddress()); 
        dto.setMachineStatus(entity.getMachineStatus()); 
        dto.setUptimeStatus(entity.getUptimeStatus()); 
        dto.setOpenTickets(entity.getOpenTickets()); 
        dto.setTransactionTrend(entity.getTransactionTrend()); 
        dto.setMtdPerformance(entity.getMtdPerformance()); 
        dto.setUptimeTrend(entity.getUptimeTrend());
        dto.setMtdUptime(entity.getMtdUptime()); 
   //     dto.setChannelExecutive(entity.getNameOfChannelExecutive()); 
        dto.setSecondaryChannelExecutive(entity.getNameOfSecondaryChannelExecutive()); 
        dto.setLastVisitedOn(entity.getLastVisitedOn()); 
        return dto;
    }
    
    public List<String> getCeWiseAtmSearchData(CeWiseAtmSearchRequestDTO request) {
    	log.info("******* Inside getCeWiseAtmSearchData Method Service Class *********");
    	log.info("Attempting to retrieve CE ATM details for userId: {}", request.getUserId());
        
        List<CeWiseAtmSearchDetails> result = searchDetailsRepository.getCeWiseAtmSearchData(
            request.getUserId(),
            request.getSortOption(),
            request.getBank(),
            request.getGrade(),
            request.getStatus(),
            request.getUptimeStatus()
        );

        if (result == null || result.isEmpty()) {
            log.info("No CE ATM entities found for userId: {}", request.getUserId());
            return new ArrayList<>();
        }

        List<String> atmIds = new ArrayList<>();
        Set<String> bankNames = new LinkedHashSet<>();

        result.forEach(entity -> {
            if (entity.getAtmid() != null && !entity.getAtmid().trim().isEmpty()) {
                atmIds.add(entity.getAtmid().trim());
            }

            if (entity.getBankname() != null && !entity.getBankname().trim().isEmpty()) {
                bankNames.add(entity.getBankname().trim());
            }
        });

        List<String> uniqueAtmIds = atmIds.stream()
            .distinct()
            .collect(Collectors.toList());

        // Combine ATM IDs first, then Bank names at the end
        List<String> combinedList = new ArrayList<>();
        combinedList.addAll(uniqueAtmIds);
        combinedList.addAll(bankNames);

        log.info("Successfully retrieved and processed {} unique search entries for userId: {}",
            combinedList.size(), request.getUserId());

        return combinedList;
    }
}
