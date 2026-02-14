package com.hpy.ops360.ticketing.cm.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.AllocationDetailsDto;
import com.hpy.ops360.ticketing.cm.dto.AllocationDetailsRequestDto;
import com.hpy.ops360.ticketing.cm.dto.TicketAllocationGroupDto;
import com.hpy.ops360.ticketing.cm.dto.TicketAllocationResponseDto;
import com.hpy.ops360.ticketing.cm.entity.AllocationDetails;
import com.hpy.ops360.ticketing.cm.repo.AllocationDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AllocationDetailsService {

	@Autowired
	private AllocationDetailsRepository repository;

	public List<AllocationDetailsDto> getAllocationDetails(AllocationDetailsRequestDto request) {
		log.info("******* Inside getAllocationDetails Service *********");
		log.info("Request Recieved:- " + request);

		List<AllocationDetails> rawResults = repository.getallocationDetails(request.getTicket_number(), request.getAtm_id());
		if (rawResults == null) {
			log.warn("Repository returned null for allocation details. Returning empty list.");
			return new ArrayList<>(); 
		}
		return rawResults.stream().map(result -> new AllocationDetailsDto(result.getSrno(),result.getFinal_allocation_type(),result.getCreated_date(),result.getCreatefilter(),result.getAllocation_owner(),result.getSubcall_type(),result.getFollow_up(),result.getStatus())).toList();
	}


	 public TicketAllocationResponseDto fetchAllocationDetails(AllocationDetailsRequestDto request) {
		 
		 log.info("******* Inside fetchAllocationDetails Service *********");
	        log.info("Request Received:- {}", request);

	        List<AllocationDetails> rawResults = repository.getallocationDetails(request.getTicket_number(), request.getAtm_id());
	        if (rawResults == null || rawResults.isEmpty()) {
	            log.warn("Repository returned null or empty for allocation details");
	            return new TicketAllocationResponseDto(new ArrayList<>());
	        }

	        // Convert to DTO and group by date
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
	        LocalDate today = LocalDate.now();
	        LocalDate yesterday = today.minusDays(1);

	        Map<String, List<AllocationDetailsDto>> groupedByDate = rawResults.stream()
	                .map(result -> new AllocationDetailsDto(
	                        result.getSrno(),
	                        result.getFinal_allocation_type(),
	                        result.getCreated_date(),
	                        result.getCreatefilter(),
	                        result.getAllocation_owner(),
	                        result.getSubcall_type(),
	                        result.getFollow_up(),
	                        result.getStatus()
	                ))
	                .collect(Collectors.groupingBy(dto -> {
	                    LocalDate createdDate = dto.getRawCreatedDate() != null
	                            ? dto.getRawCreatedDate().toLocalDateTime().toLocalDate()
	                            : today;
	                    String formattedDate = createdDate.format(formatter);
	                    if (createdDate.equals(today)) {
	                        return "Today | " + formattedDate;
	                    } else if (createdDate.equals(yesterday)) {
	                        return "Yesterday | " + formattedDate;
	                    } else {
	                        return formattedDate;
	                    }
	                }));

	        // Convert grouped data to response DTO
	        List<TicketAllocationGroupDto> ticketAllocation = groupedByDate.entrySet().stream()
	                .map(entry -> new TicketAllocationGroupDto(entry.getKey(), entry.getValue()))
	                .sorted((a, b) -> {
	                    String dateA = a.getDate();
	                    String dateB = b.getDate();
	                    if (dateA.startsWith("Today")) return -2;
	                    if (dateB.startsWith("Today")) return 2;
	                    if (dateA.startsWith("Yesterday")) return -1;
	                    if (dateB.startsWith("Yesterday")) return 1;
	                    return dateB.compareTo(dateA); // Descending order for other dates
	                })
	                .toList();

	        return new TicketAllocationResponseDto(ticketAllocation);
	 
	 
	 }
	 

}



