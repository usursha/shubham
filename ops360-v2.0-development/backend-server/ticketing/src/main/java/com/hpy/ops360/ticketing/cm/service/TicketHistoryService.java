package com.hpy.ops360.ticketing.cm.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.TicketArchiveListDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketHistoryDto;
import com.hpy.ops360.ticketing.cm.dto.TicketHistoryGroupDto;
import com.hpy.ops360.ticketing.cm.dto.TicketHistoryRequestDto;
import com.hpy.ops360.ticketing.cm.dto.TicketHistoryResponseDto;
import com.hpy.ops360.ticketing.cm.dto.TicketHistorySearchRequestDto;
import com.hpy.ops360.ticketing.cm.entity.TicketArchiveListEntity;
import com.hpy.ops360.ticketing.cm.entity.TicketHistoryEntity;
import com.hpy.ops360.ticketing.cm.entity.TicketHistorySearchEntity;
import com.hpy.ops360.ticketing.cm.repo.TicketArchiveListRepository;
import com.hpy.ops360.ticketing.cm.repo.TicketHistoryRepository;
import com.hpy.ops360.ticketing.cm.repo.TicketHistorySearchRepository;
import com.hpy.ops360.ticketing.utils.CustomDateFormattor;
import com.hpy.ops360.ticketing.utils.CustomDateFormattor.FormatStyle;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TicketHistoryService {

	@Autowired
	private TicketHistoryRepository ticketHistoryRepository;

	@Autowired
	private TicketArchiveListRepository ticketArchiveListRepository;
	
	@Autowired
	private TicketHistorySearchRepository ticketHistorySearchRepository;


	public TicketHistoryDto getTicketHistory(TicketHistoryRequestDto requestDto) {
		log.info("Ticket History Request: {}", requestDto);

		TicketHistoryDto response = new TicketHistoryDto();

		List<TicketHistoryEntity> ticketHistoryDetailsList = ticketHistoryRepository.getTicketHistoryDetails(
				requestDto.getCm_user_id(), requestDto.getPageNumber(), requestDto.getPageSize(), requestDto.getFlag(),
				requestDto.getSearchText());

		log.info("Fetch ticket history details: {}", ticketHistoryDetailsList);

		// Get total count from the first record (since stored procedure returns it)
		int totalCount = 0;
	//	int totalCounttickets=0;
		if (!ticketHistoryDetailsList.isEmpty()) {
	//		 totalCounttickets = ticketHistoryDetailsList.size();
			totalCount = Integer.parseInt(ticketHistoryDetailsList.get(0).getTotal_records());
		}

		log.info("Total count of data is: {}", totalCount);

		// Group tickets by date
		Map<String, List<TicketHistoryResponseDto>> tempGroupedByDate = new LinkedHashMap<>();

		DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);

		for (TicketHistoryEntity entity : ticketHistoryDetailsList) {
			LocalDate completionDate = null;
			try {

				// Parse the datetime string to extract date part

				completionDate = parseCompletionDate(entity.getCompletionDateWithTime());

			} catch (Exception e) {

				log.error("Error parsing completion date: {}. Skipping entity.", entity.getCompletionDateWithTime(), e);

				continue;

			}

			// Determine the group key (Today, Yesterday, or actual date)

			String groupKey;
			if (completionDate.equals(today)) {
				groupKey = "Today | " + today.format(displayFormatter);

			} else if (completionDate.equals(yesterday)) {
				groupKey = "Yesterday | " + yesterday.format(displayFormatter);

			} else {
				groupKey = completionDate.format(displayFormatter);
			}

			TicketHistoryResponseDto dto = mapEntityToResponseDto(entity);
			tempGroupedByDate.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(dto);

		}

		// Convert grouped map to list of TicketHistoryGroupDto

		List<TicketHistoryGroupDto> finalGroupedList = tempGroupedByDate.entrySet().stream()

				.map(entry -> new TicketHistoryGroupDto(entry.getKey(), entry.getValue())).collect(Collectors.toList());

		response.setGroupedTicketHistory(finalGroupedList);

		response.setTotalCount(totalCount);

		return response;

	}


	private TicketHistoryResponseDto mapEntityToResponseDto(TicketHistoryEntity entity) {

		TicketHistoryResponseDto dto = new TicketHistoryResponseDto();

		dto.setRno(entity.getRno());

		dto.setTicketNumber(entity.getSrno());

		dto.setBank(entity.getCustomer());

		dto.setEquipmentId(entity.getEquipmentId());

		dto.setModel(entity.getModel());

		dto.setAtmCategory(entity.getAtmCategory());

		dto.setAtmClassification(entity.getAtmClassification());

		dto.setCallDate(CustomDateFormattor.convert(entity.getCallDate(), FormatStyle.SHORT));

		dto.setCreatedDate(CustomDateFormattor.convert(entity.getCreatedDate(), FormatStyle.SHORT));

		dto.setCallType(entity.getCallType());

		dto.setSubCallType(entity.getSubCallType());

		dto.setCompletionDateWithTime(

				CustomDateFormattor.convert(entity.getCompletionDateWithTime(), FormatStyle.SHORT));

		dto.setDowntimeInMins(entity.getDowntime());

		dto.setVendor(entity.getVendor());

		dto.setServiceCode(entity.getServiceCode());

		dto.setDiagnosis(entity.getDiagnosis());

		dto.setEventCode(entity.getEventCode());

		dto.setHelpdeskName(entity.getHelpdeskName());

		dto.setLastAllocatedTime(entity.getLastAllocatedTime());

		dto.setLastComment(entity.getLastComment());

		dto.setLastActivity(entity.getLastActivity());

		dto.setStatus(entity.getStatus());

		dto.setSubStatus(entity.getSubStatus());

		dto.setRo(entity.getRo());

		dto.setSite(entity.getSite());

		dto.setAddress(entity.getAddress());

		dto.setCity(entity.getCity());

		dto.setLocationName(entity.getLocationName());

		dto.setState(entity.getState());

		dto.setNextFollowUp(entity.getNextFollowUp());

		dto.setEtaDateTime(CustomDateFormattor.convert(entity.getEtaDateTime(), FormatStyle.SHORT));

		dto.setOwner(entity.getOwner());

		dto.setCustomerRemark(entity.getCustomerRemark());

		dto.setInsertDate(entity.getInsertDate());

		dto.setCeName(entity.getCe_user_id());

		dto.setRemark(entity.getRemark());

		dto.setAtmAddress(entity.getAtm_address());

		dto.setSiteId(entity.getSite_id());

		dto.setHimsTicketStatus(entity.getAtm_status());

		dto.setFlagStatus(entity.getFlag_status());


		return dto;

	}


	/**

	 * Parse completion date from various possible formats

	 */

	private LocalDate parseCompletionDate(String dateTimeString) {

		if (dateTimeString == null || dateTimeString.trim().isEmpty()) {

			return LocalDate.now();

		}

		String trimmedDate = dateTimeString.trim();

		try {

			// Try parsing full datetime format: 2024-12-10 12:12:00.0

			if (trimmedDate.contains(" ") && trimmedDate.contains(":")) {

				// Extract date part before space

				String datePart = trimmedDate.split(" ")[0];

				return LocalDate.parse(datePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			}

			// Try parsing date format: yyyy-MM-dd

			if (trimmedDate.matches("\\d{4}-\\d{2}-\\d{2}")) {

				return LocalDate.parse(trimmedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			}

			// Try parsing format: dd MMM yyyy

			if (trimmedDate.matches("\\d{2} \\w{3} \\d{4}")) {

				return LocalDate.parse(trimmedDate, DateTimeFormatter.ofPattern("dd MMM yyyy"));

			}

			// Try parsing format: dd-MM-yyyy

			if (trimmedDate.matches("\\d{2}-\\d{2}-\\d{4}")) {

				return LocalDate.parse(trimmedDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

			}

			// Try parsing format: dd/MM/yyyy

			if (trimmedDate.matches("\\d{2}/\\d{2}/\\d{4}")) {

				return LocalDate.parse(trimmedDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			}

		} catch (Exception e) {

			log.warn("Failed to parse date: {}. Using current date.", dateTimeString);

		}

		// Fallback to current date

		return LocalDate.now();

	}


	public TicketArchiveListDTO getTicketArchiveListData(String cmUserId,String bankname, String atmcode, String ticketNumber, String ceUserId) {

		log.info(
				"Fetching Ticket Archive List Data for User ID: {}, Bank Name: {}, ATM Code: {}, Ticket Number: {}, CE User ID: {}",
				cmUserId, bankname, atmcode, ticketNumber, ceUserId);

		List<String> banks = new ArrayList<>();

		List<String> atms = new ArrayList<>();

		List<String> ticketNumbers = new ArrayList<>();

		List<String> channelExecutives = new ArrayList<>();

		List<TicketArchiveListEntity> ticketArcList = ticketArchiveListRepository.getTicketArchiveListDataPerDropDown(cmUserId.trim().equals("")?null:cmUserId, bankname.trim().equals("")?null:bankname , atmcode.trim().equals("")?null:atmcode, ticketNumber.trim().equals("")?null:ticketNumber, ceUserId.trim().equals("")?null:ceUserId);

		if (ticketArcList != null && !ticketArcList.isEmpty()) {

			for (TicketArchiveListEntity entity : ticketArcList) {

				if (entity.getBankName() != null && !banks.contains(entity.getBankName())) {

					banks.add(entity.getBankName());

				}

				if (entity.getAtmCode() != null && !atms.contains(entity.getAtmCode())) {

					atms.add(entity.getAtmCode());

				}

				if (entity.getTicketNumber() != null && !ticketNumbers.contains(entity.getTicketNumber())) {

					ticketNumbers.add(entity.getTicketNumber());

				}

				if (entity.getChennaleExecutive() != null

						&& !channelExecutives.contains(entity.getChennaleExecutive())) {

					channelExecutives.add(entity.getChennaleExecutive());

				}

			}
			
			// Populate and return the DTO

			TicketArchiveListDTO responseDTO = new TicketArchiveListDTO();

			responseDTO.setBankName(banks);

			responseDTO.setAtmId(atms);

			responseDTO.setTicketNumber(ticketNumbers);

			responseDTO.setChannelExecutive(channelExecutives);

			return responseDTO;

		}
		
		log.info("No data found for Ticket Archive List");
		
		return new TicketArchiveListDTO(0L,Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),Collections.emptyList()); // Return empty DTO if no data found
		

	}

	public List<TicketHistoryResponseDto> getTicketHistorySearch(TicketHistoryRequestDto request) {
		log.info("Ticket History Request: {}", request);
        try {
            // Call stored procedure
			List<TicketHistoryEntity> tickets = ticketHistoryRepository.getTicketHistoryDetails(
					request.getCm_user_id(), request.getPageNumber(), request.getPageSize(), request.getFlag(),
					request.getSearchText());

            log.info("Retrieved {} tickets from database", tickets.size());

            // Convert entities to DTOs
//            List<SearchTicketsDto> ticketDTOs =  tickets.stream()
//                    .map(this::searchConvertToDTO)
//                    .collect(Collectors.toList());
            
            List<TicketHistoryResponseDto> ticketDTOs =tickets.stream()
                    .map(this::mapEntityToResponseDto)
                    .collect(Collectors.toList());

            log.info("Successfully converted {} tickets to DTOs", ticketDTOs.size());
            
            return ticketDTOs;

        } catch (Exception e) {
            log.error("Error fetching tickets History", e);
            throw new RuntimeException("Failed to fetch tickets: " + e.getMessage(), e);
        }
    }
	
	public List<String> getTicketHistorySearchByTicketNo(TicketHistorySearchRequestDto request) {
	    log.info("Ticket History Request: {}", request);
	   
	    if (request.getCmUserId() == null) {
	        log.error("cmUserId in request is null");
	        throw new IllegalArgumentException("cmUserId cannot be null");
	    }

	    try {
	       
	    	List<TicketHistorySearchEntity> tickets = ticketHistorySearchRepository.getTicketHistoryDetailsByCmUserId(
	                request.getCmUserId());

	        // Check if tickets is null
	        if (tickets == null) {
	            log.warn("No tickets found for cmUserId: {}", request.getCmUserId());
	            return Collections.emptyList();
	        }

	        log.info("Retrieved {} tickets from database", tickets.size());

	        List<String> ticketIds = tickets.stream()
	                .filter(Objects::nonNull) // Filter out null entities
	                .map(TicketHistorySearchEntity::getSrno)
	                .filter(Objects::nonNull) // Filter out null srno values
	                .collect(Collectors.toList());

	        log.info("Successfully retrieved {} ticket IDs", ticketIds.size());
	        
	        return ticketIds;

	    } catch (Exception e) {
	        log.error("Error fetching tickets History for cmUserId: {}", request.getCmUserId(), e);
	        throw new RuntimeException("Failed to fetch tickets: " + e.getMessage(), e);
	    }
	}
	
}	 
