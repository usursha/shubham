package com.hpy.ops360.ticketing.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;
import com.hpy.ops360.ticketing.cm.dto.OpenTicketsWithCategoryDto;
import com.hpy.ops360.ticketing.config.DisableSslClass;
import com.hpy.ops360.ticketing.dto.AtmDetailsWithSourceDto;
import com.hpy.ops360.ticketing.dto.TicketCategoryCountDto;
import com.hpy.ops360.ticketing.entity.UserAtmDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecentTicketPortalServiceHims {
	
	
	private UserAtmDetailsService userAtmDetailsService;
	
	private TaskService2 taskService;
	
	public RecentTicketPortalServiceHims(UserAtmDetailsService userAtmDetailsService, TaskService2 taskService) {
		super();
		this.userAtmDetailsService = userAtmDetailsService;
		this.taskService = taskService;
	}

	public OpenTicketsWithCategoryDto getTicketDetailsByCEAgainstAtm(String atmId, String ceId) {
		// Check if the given atmId belongs to the CE
		DisableSslClass.disableSSLVerification();
		List<UserAtmDetails> userAtmDetails = userAtmDetailsService.getUserAtmDetails(ceId);
		
		boolean atmBelongsToCe = userAtmDetails.stream().anyMatch(atmDetails -> atmDetails.getAtm_code().equals(atmId));

		if (!atmBelongsToCe) {
			// If the ATM doesn't belong to the CE, return an empty result
			return new OpenTicketsWithCategoryDto(Collections.emptyList(),
					new TicketCategoryCountDto(0, 0, 0, 0, 0, 0,0));
		}

		// Create a list with only the specified ATM ID
		List<AtmDetailsWithSourceDto> atmIds = userAtmDetails.stream().filter(atm -> atm.getAtm_code().equals(atmId))
				.map(atm -> new AtmDetailsWithSourceDto(atm.getAtm_code(),atm.getSource())).toList();
//				Collections.singletonList(new AtmDetailsWithSourceDto(atmId,));

		return getCeOpenTicketDetailsAgainstAtm(ceId, atmIds);
	}
	
	public OpenTicketsWithCategoryDto getCeOpenTicketDetailsAgainstAtm(String ceId, List<AtmDetailsWithSourceDto> atmIds) {
		if (atmIds.isEmpty()) {
			return new OpenTicketsWithCategoryDto(Collections.emptyList(),
					new TicketCategoryCountDto(0, 0, 0, 0, 0, 0,0));
		}

		// Fetch all tickets for the specified ATM ID
		List<AtmShortDetailsDto> allTickets = taskService.getTicketList(atmIds, Integer.MAX_VALUE,ceId);

		if (allTickets.isEmpty()) {
			return new OpenTicketsWithCategoryDto(Collections.emptyList(),
					new TicketCategoryCountDto(0, 0, 0, 0, 0, 0,0));
		}

		// Filter down calls and sort by priority and downtime
		List<AtmShortDetailsDto> sortedDownCalls = allTickets.stream().filter(t -> t.getDownCall() == 1)
				.sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()
						.thenComparing(AtmShortDetailsDto::getDownTime).reversed())
				.toList();

		return new OpenTicketsWithCategoryDto(sortedDownCalls, getEventCategoryCounts(sortedDownCalls));
	}
	
	private TicketCategoryCountDto getEventCategoryCounts(List<AtmShortDetailsDto> sortedTicketList) {
		log.info("getEventCategoryCounts()|atmTicketEventList:{}", sortedTicketList);

		// Use parallel stream for better performance
		Map<String, Long> eventGroupCounts = sortedTicketList.parallelStream()
				.collect(Collectors.groupingBy(AtmShortDetailsDto::getEventGroup, Collectors.counting()));

		return TicketCategoryCountDto.builder().total(sortedTicketList.size())
				.cash(eventGroupCounts.getOrDefault("Cash", 0L).intValue())
				.communication(eventGroupCounts.getOrDefault("Communication", 0L).intValue())
				.hardwareFault(eventGroupCounts.getOrDefault("Hardware Fault", 0L).intValue())
				.noTransactions(eventGroupCounts.getOrDefault("No Transactions", 0L).intValue())
				.supervisory(eventGroupCounts.getOrDefault("Supervisory", 0L).intValue())
				.others(eventGroupCounts.getOrDefault("Others", 0L).intValue()).build();
	}
	

}
