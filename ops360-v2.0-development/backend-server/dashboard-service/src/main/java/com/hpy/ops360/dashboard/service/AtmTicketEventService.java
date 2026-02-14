package com.hpy.ops360.dashboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.dto.AtmTicketsEventDto;
import com.hpy.ops360.dashboard.entity.AtmTicketEvent;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.repository.AtmTicketEventRepository;
import com.hpy.ops360.dashboard.util.Helper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AtmTicketEventService {

	private final AtmTicketEventRepository atmTicketEventRepository;

	private final Helper helper;
	
	private LoginService loginservice;

	@Loggable
	public List<AtmTicketsEventDto> getAtmTicketEvent(String atmTicketEvent) {
		String username=loginservice.getLoggedInUser();
//		String username = "mahesh.more";
		List<AtmTicketEvent> atmTicketEvents=atmTicketEventRepository.getAtmTicketEvent(username, atmTicketEvent);
		return atmTicketEvents.stream()
	            .map(result -> new AtmTicketsEventDto(
	                result.getSrNo(),
	                result.getAtmId(),
	                result.getTicketId(),
	                result.getEventCode(),
	                result.getPriorityScore(),
	                result.getEventGroup(),
	                result.getIsBreakdown(),
	                result.getIsUpdated(),
	                result.getIsTimedOut(),
	                result.getIsTravelling(),
	                result.getTravelTime(),
	                result.getTravelEta(),
	                result.getDownCall(),
	                result.getEtaDateTime(),
	                result.getOwner(),
	                result.getSubcall(),
	                result.getEtaTimeout(),
	                result.getFlagStatus(),
	                result.getFlagStatusInsertTime()
	            ))
	            .toList();
	}

}
