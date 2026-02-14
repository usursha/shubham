package com.hpy.ops360.atmservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.CMUnderCEAtmDetailsDTO;
import com.hpy.ops360.atmservice.dto.UserStatusDto;
import com.hpy.ops360.atmservice.entity.CMAtmDetails;
import com.hpy.ops360.atmservice.logapi.Loggable;
import com.hpy.ops360.atmservice.repository.CMAtmDetailsRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CmSynopsisService {

    @Autowired
    private CMAtmDetailsRepository cmAtmDetailsRepository;

    @Loggable
    public List<CMUnderCEAtmDetailsDTO> getCMAtmDetails(UserStatusDto userStatusDto) {
        log.info("Entering getCMAtmDetails | userId: {}, status: {}", userStatusDto.getUserId(), userStatusDto.getStatus());

        // Fetch all ATM details for the given userId
        List<CMAtmDetails> detailsList = cmAtmDetailsRepository.getCMAtmDetails(userStatusDto.getUserId());
        log.info("Fetched {} ATM details for userId: {}", detailsList.size(), userStatusDto.getUserId());

        // Validate input status
        if (userStatusDto.getStatus() == null || userStatusDto.getStatus().isBlank()) {
            log.warn("Invalid status received: '{}'. Returning empty list.", userStatusDto.getStatus());
            return List.of();
        }

        // Filter based on requested status (Operational or Down)
        List<CMUnderCEAtmDetailsDTO> filteredList = detailsList.stream()
            .filter(details -> {
                boolean match = userStatusDto.getStatus().equalsIgnoreCase(details.getMachineStatus());
                if (!match) {
                    log.debug("Filtering out ATM ID: {} with MachineStatus: {}", details.getAtmid(), details.getMachineStatus());
                }
                return match;
            })
            .map(this::fillCMAtmDetails)
            .collect(Collectors.toList());

        if (filteredList.isEmpty()) {
            log.warn("No ATM details found for userId: {} with status: {}", userStatusDto.getUserId(), userStatusDto.getStatus());
        } else {
            log.info("Returning {} ATM records for userId: {} with status: {}", filteredList.size(), userStatusDto.getUserId(), userStatusDto.getStatus());
        }

        log.info("Exiting getCMAtmDetails");
        return filteredList;
    }

    private CMUnderCEAtmDetailsDTO fillCMAtmDetails(CMAtmDetails details) {
        CMUnderCEAtmDetailsDTO dto = new CMUnderCEAtmDetailsDTO();
        dto.setId(details.getId());
        dto.setAtmid(details.getAtmid());
        dto.setBankname(details.getBankname());
        dto.setGrade(details.getGrade());
        dto.setAddress(details.getAddress());
        dto.setMachineStatus(details.getMachineStatus());
        dto.setOpenTickets(details.getOpenTickets());
        dto.setTransactionTrend(details.getTransactionTrend() == null ? "" : details.getTransactionTrend());
        dto.setMtdPerformance(details.getMtdPerformance() == null ? "" : details.getMtdPerformance());
        dto.setUptimeTrend(details.getUptimeTrend());
        dto.setUptimeStatus(details.getUptimeStatus() == null ? "" : details.getUptimeStatus());
        dto.setMtdUptime(details.getMtdUptime());
        dto.setNameOfChannelExecutive(details.getNameOfChannelExecutive());
        dto.setNameOfSecondaryChannelExecutive(details.getNameOfSecondaryChannelExecutive());
        dto.setLastVisitedOn(details.getLastVisitedOn() == null ? "" : details.getLastVisitedOn());
        log.debug("Mapped ATM ID: {} to DTO", details.getAtmid());
        return dto;
    }
}
