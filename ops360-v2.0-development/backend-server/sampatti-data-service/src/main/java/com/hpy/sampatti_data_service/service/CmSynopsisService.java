package com.hpy.sampatti_data_service.service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;


import com.hpy.sampatti_data_service.entity.CMUnderCEAtmDetails;
import com.hpy.sampatti_data_service.repository.CMUnderCEAtmDetailsRepository;
import com.hpy.sampatti_data_service.response.ATMDataResp;
import com.hpy.sampatti_data_service.response.BankDataDto;
import com.hpy.sampatti_data_service.response.CMUnderCEAtmDetailsDTO;


@Log4j2
@Service
public class CmSynopsisService {

	@Autowired
	private CMUnderCEAtmDetailsRepository cmUnderCEAtmDetailsRepository;
	
	@Autowired
	private SampattiService sampattiService;
	
	
	public List<CMUnderCEAtmDetailsDTO> getCMUnderCEAtmDetails(String userId) {
	    // Step 1: Get ATM data for the user from SampattiService
	    ATMDataResp atmDataResp = sampattiService.getAtmData(userId); // Fetch the ATM data response
	    
	    // Step 2: Check if data is present
	    if (atmDataResp == null || atmDataResp.getData().isEmpty()) {
	        log.error("No ATM data found for userId: {}", userId);
	        throw new RuntimeException("No ATM data found for user.");
	    }

	    // Step 3: Convert the details list to CMUnderCEAtmDetailsDTO and set uptimeTrend from SampattiService response
	    List<CMUnderCEAtmDetails> detailsList = cmUnderCEAtmDetailsRepository.getCMUnderCEAtmDetails(userId);
	    return detailsList.stream().map(details -> {
	        return fillCEAtmDetails(details, atmDataResp); // Pass SampattiService response here
	    }).collect(Collectors.toList());
	}

	private CMUnderCEAtmDetailsDTO fillCEAtmDetails(CMUnderCEAtmDetails details, ATMDataResp atmDataResp) {
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
	    
	    // Step 4: Match atmId with SampattiService data and set uptimeTrend
	    BankDataDto matchedBankData = atmDataResp.getData().stream()
	        .filter(bankData -> bankData.getAtmId().equals(details.getAtmid()))
	        .findFirst()
	        .orElse(null);

	    if (matchedBankData != null) {
	        dto.setUptimeTrend(matchedBankData.getUptimeTrend()); // Set the uptimeTrend
	    } else {
	        log.warn("No matching ATM data found for atmId: {}", details.getAtmid());
	        dto.setUptimeTrend(0); // Set a default value if no match is found
	    }

	    dto.setUptimeStatus(details.getUptimeStatus() == null ? "" : details.getUptimeStatus());
	    dto.setMtdUptime(details.getMtdUptime());
	    dto.setNameOfChannelExecutive(details.getNameOfChannelExecutive());
	    dto.setNameOfSecondaryChannelExecutive(details.getNameOfSecondaryChannelExecutive());
	    dto.setLastVisitedOn(details.getLastVisitedOn() == null ? "" : details.getLastVisitedOn());
	    return dto;
	}

	
//	public List<CMUnderCEAtmDetailsDTO> getCMUnderCEAtmDetails(String ceUserId) {
//		List<CMUnderCEAtmDetails> detailsList = cmUnderCEAtmDetailsRepository.getCMUnderCEAtmDetails(ceUserId);
//		return detailsList.stream().map(details -> {
//			return fillCEAtmDetails(details);
//		}).collect(Collectors.toList());
//	}
//
//	private CMUnderCEAtmDetailsDTO fillCEAtmDetails(CMUnderCEAtmDetails details) {
//		CMUnderCEAtmDetailsDTO dto = new CMUnderCEAtmDetailsDTO();
//		dto.setId(details.getId());
//		dto.setAtmid(details.getAtmid());
//		dto.setBankname(details.getBankname());
//		dto.setGrade(details.getGrade());
//		dto.setAddress(details.getAddress());
//		dto.setMachineStatus(details.getMachineStatus());
//		dto.setOpenTickets(details.getOpenTickets());
////		dto.setErrorCategory(details.getErrorCategory());
////		dto.setOwnership(details.getOwnership());
////		dto.setTicketAging(details.getTicketAging());
//		dto.setTransactionTrend(details.getTransactionTrend() == null ? "" : details.getTransactionTrend());
//		dto.setMtdPerformance(details.getMtdPerformance() == null ? "" : details.getMtdPerformance());
//		dto.setUptimeTrend(details.getUptimeTrend());
//		// Map uptimeTrend based on atmid
//		//dto.setUptimeStatus("");
//		dto.setUptimeStatus(details.getUptimeStatus() == null ? "" : details.getUptimeStatus());
//		dto.setMtdUptime(details.getMtdUptime());
//		dto.setNameOfChannelExecutive(details.getNameOfChannelExecutive());
//		dto.setNameOfSecondaryChannelExecutive(details.getNameOfSecondaryChannelExecutive());
//		dto.setLastVisitedOn(details.getLastVisitedOn() == null ? "" : details.getLastVisitedOn());
//		return dto;
//	}
}

