package com.hpy.ops360.ticketing.cm.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.TravelEtaDetailsDTO;
import com.hpy.ops360.ticketing.cm.entity.TravelEtaDetails;
import com.hpy.ops360.ticketing.cm.repo.TravelEtaDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TravelEtaDetailsService {

	@Autowired
	private TravelEtaDetailsRepository repository;

	public Optional<TravelEtaDetailsDTO> getTravelDetails(String atmId, String ticketNumber) {
		log.debug("getTravelDetails {}, {}", atmId, ticketNumber);
		Optional<TravelEtaDetails> travelDetails = repository.getTravelDetails(atmId, ticketNumber);
		log.debug("getTravelDetails.travelDetails {}", travelDetails);
		Optional<TravelEtaDetailsDTO> result = travelDetails.map(this::convertToDTO);
		log.debug("getTravelDetails.travelDetails.result {}", result);
		return result;
	}

	private TravelEtaDetailsDTO convertToDTO(TravelEtaDetails travelDetails) {
		TravelEtaDetailsDTO dto = new TravelEtaDetailsDTO();
		dto.setOwner(travelDetails.getOwner());
		dto.setCustomerRemark(travelDetails.getCustomerRemark());
		dto.setInternalRemark(travelDetails.getInternalRemark());
		dto.setTravelEtaDate(travelDetails.getTravelEtaDate());
		dto.setTravelEtaTime(travelDetails.getTravelEtaTime());
		dto.setTravelDuration(travelDetails.getTravelDuration());
		dto.setTravelEtaDateResolve(travelDetails.getTravelEtaDateResolve());
		dto.setTravelDurationResolve(travelDetails.getTravelDurationResolve());
		return dto;
	}
}
