package com.hpy.ops360.ticketing.cm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.AllocationETADetailsDto;
import com.hpy.ops360.ticketing.cm.dto.AllocationEtaDetailsRequestDto;
import com.hpy.ops360.ticketing.cm.entity.AllocationETADetails;
import com.hpy.ops360.ticketing.cm.repo.AllocationEtaDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AllocationEtaDetailsService {

	@Autowired
	private AllocationEtaDetailsRepository repository;

	public List<AllocationETADetailsDto> getAllocationEtaDetails(AllocationEtaDetailsRequestDto request) {
		log.info("******* Inside getAllocationEtaDetails Service *********");
		log.info("Request Recieved:- " + request);

		List<AllocationETADetails> rawResults = repository.getallocationDetails(request.getTicket_number(), request.getAtm_id());
		
		return rawResults.stream().map(result -> new AllocationETADetailsDto(result.getSrno(),result.getAllocation_type(),result.getCreated_date(),result.getAllocation_owner(),result.getSubcall_type(),result.getFollow_up(),result.getStatus())).toList();
	}


}

