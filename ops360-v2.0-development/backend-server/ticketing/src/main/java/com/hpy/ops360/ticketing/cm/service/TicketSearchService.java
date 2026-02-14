package com.hpy.ops360.ticketing.cm.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.GroupedTicketDTO;
import com.hpy.ops360.ticketing.cm.dto.SearchTicketsDto;
import com.hpy.ops360.ticketing.cm.dto.TicketDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketDataWrapper;
import com.hpy.ops360.ticketing.cm.dto.TicketMainResponseDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketRequestDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketSearchRequestDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketSearchResponseDTO;
import com.hpy.ops360.ticketing.cm.dto.TicketSuggestionReq;
import com.hpy.ops360.ticketing.cm.entity.SearchTickets;
import com.hpy.ops360.ticketing.cm.entity.TicketSearchSuggestion;
import com.hpy.ops360.ticketing.cm.entity.Tickets;
import com.hpy.ops360.ticketing.cm.repo.SearchTicketsRepository;
import com.hpy.ops360.ticketing.cm.repo.Ticket_Repository;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.TicketSearchSuggestionRepository;
import com.hpy.ops360.ticketing.utils.CustomDateFormattor;
import com.hpy.ops360.ticketing.utils.CustomDateFormattor.FormatStyle;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TicketSearchService {
	@Autowired
	private Ticket_Repository ticketRepository;

	
	@Autowired
	private SearchTicketsRepository searchTicketsRepository ;
		
	@Autowired
	private LoginService loginService;

	@Autowired
	private TicketSearchSuggestionRepository ticketSearchSuggestionRepository;
//	public List<TicketResponseDTO> getTicketsGroupedByDate(TicketRequestDTO request) {
//
//		String CmUserId = loginService.getLoggedInUser();
//		log.info("Fetching tickets for cmUserId: {}, pageSize: {}, pageNumber: {}, flag: {}", CmUserId,
//				request.getPageSize(), request.getPageNumber(), request.getFlag());
//
//		try {
//			List<Tickets> tickets = ticketRepository.getTicketsForCm(request.getCmUserId(), request.getPageSize(),
//					request.getPageNumber(), request.getFlag(), request.getSearchText());
//
//			log.info("Retrieved {} tickets from database", tickets.size());
//
//			// Convert entities to DTOs
//			List<TicketDTO> ticketDTOs = tickets.stream().map(this::convertToDTO).collect(Collectors.toList());
//
//			// Group tickets by date
//			Map<LocalDate, List<TicketDTO>> groupedByDate = ticketDTOs.stream()
//					.collect(Collectors.groupingBy(ticket -> {
//						try {
//							LocalDateTime createdDate = LocalDateTime.parse(ticket.getCreatedDate().replace(" ", "T"));
//							return createdDate.toLocalDate();
//						} catch (Exception e) {
//							log.warn("Error parsing created date: {}, using current date", ticket.getCreatedDate());
//							return LocalDate.now();
//						}
//					}));
//
//			// Sort dates in descending order and create response
//			List<TicketResponseDTO> result = new ArrayList<>();
//			LocalDate today = LocalDate.now();
//			LocalDate yesterday = today.minusDays(1);
//
//			// Sort the map by date in descending order
//			groupedByDate.entrySet().stream().sorted(Map.Entry.<LocalDate, List<TicketDTO>>comparingByKey().reversed())
//					.forEach(entry -> {
//						LocalDate date = entry.getKey();
//						List<TicketDTO> dateTickets = entry.getValue();
//
//						String dateLabel = formatDateLabel(date, today, yesterday);
//						result.add(new TicketResponseDTO(null, dateLabel, dateTickets));
//
//						log.debug("Added {} tickets for date: {}", dateTickets.size(), dateLabel);
//					});
//
//			log.info("Successfully grouped tickets into {} date groups", result.size());
//			return result;
//
//		} catch (Exception e) {
//			log.error("Error fetching tickets: ", e);
//			throw new RuntimeException("Failed to fetch tickets", e);
//		}
//	}
//
//	private String formatDateLabel(LocalDate date, LocalDate today, LocalDate yesterday) {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
//
//		if (date.equals(today)) {
//			return "Today | " + date.format(formatter);
//		} else if (date.equals(yesterday)) {
//			return "Yesterday | " + date.format(formatter);
//		} else {
//			return date.format(formatter);
//		}
//	}
//
//	private TicketDTO convertToDTO(Tickets entity) {
//		log.debug("Converting ticket entity to DTO for srno: {}", entity.getSrno());
//
//		TicketDTO dto = new TicketDTO();
//
//		try {
//			// Generate unique request ID using timestamp and srno
//			// dto.setRequestId("COMBINED-" + System.currentTimeMillis() + "-" +
//			// entity.getSrno());
//			dto.setRno(entity.getRno());
//			dto.setTicketNumber(entity.getSrno());
//			dto.setBank(entity.getCustomer());
//			dto.setEquipmentId(entity.getEquipmentId());
//			dto.setModel(entity.getModel());
//			dto.setAtmCategory(entity.getAtmCategory());
//			dto.setAtmClassification(entity.getAtmClassification());
//			dto.setCallDate(entity.getCallDate());
//			dto.setCreatedDate(entity.getCreatedDate());
//			dto.setCallType(entity.getCallType());
//			dto.setSubCallType(entity.getSubCallType());
//			dto.setCompletionDateWithTime(entity.getCompletionDateWithTime());
//			dto.setDowntimeInMins(entity.getDowntimeInMins());
//			dto.setVendor(entity.getVendor());
//			dto.setServiceCode(entity.getServiceCode());
//			dto.setDiagnosis(entity.getDiagnosis());
//			dto.setEventCode(entity.getEventCode());
//			dto.setHelpdeskName(entity.getHelpdeskName());
//			dto.setLastAllocatedTime(entity.getLastAllocatedTime());
//			dto.setLastComment(entity.getLastComment());
//			dto.setLastActivity(entity.getLastActivity());
//			dto.setStatus(entity.getStatus());
//			dto.setSubStatus(entity.getSubStatus());
//			dto.setRo(entity.getRo());
//			dto.setSite(entity.getSite());
//			dto.setAddress(entity.getAddress());
//			dto.setCity(entity.getCity());
//			dto.setLocationName(entity.getLocationName());
//			dto.setState(entity.getState());
//			dto.setNextFollowUp(entity.getNextFollowUp());
//			dto.setEtaDateTime(entity.getEtaDateTime());
//			dto.setOwner(entity.getOwner());
//			dto.setCustomerRemark(entity.getCustomerRemark());
//			dto.setAgeingDays(entity.getAgeingDays());
//			dto.setHimsTicketStatus(entity.getAtm_status());
//			// Handle date formatting
//
//			dto.setCreatedDate(entity.getCreatedDate());
//
//			dto.setCompletionDateWithTime(entity.getCompletionDateWithTime());
//
//			dto.setTotalRecords(entity.getTotal_records());
//			dto.setCurrentPage(entity.getCurrent_page());
//			dto.setPageSize(entity.getPage_size());
//			dto.setTotalPages(entity.getTotal_pages());
//			dto.setCeName(entity.getCe_user_id());
//			
//
//		} catch (Exception e) {
//			log.error("Error converting entity to DTO for srno: {}", entity.getSrno(), e);
//			throw new RuntimeException("Error converting ticket data", e);
//		}
//
//		return dto;
//	}

	// -------------------presous logic -------------------------

//	
//	
//	public TicketMainResponseDTO getTicketsGroupedByDate(TicketRequestDTO request) {
//        String CmUserId = loginService.getLoggedInUser();
//        log.info("Fetching tickets for cmUserId: {}, pageSize: {}, pageNumber: {}, flag: {}", 
//                CmUserId, request.getPageSize(), request.getPageNumber(), request.getFlag());
//
//        try {
//            List<Tickets> tickets = ticketRepository.getTicketsForCm(
//                request.getCmUserId(), 
//                request.getPageSize(),
//                request.getPageNumber(), 
//                request.getFlag(), 
//                request.getSearchText()
//            );
//
//            log.info("Retrieved {} tickets from database", tickets.size());
//
//            // Convert entities to DTOs
//            List<TicketDTO> ticketDTOs = tickets.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//
//            // Group tickets by date
//            Map<LocalDate, List<TicketDTO>> groupedByDate = ticketDTOs.stream()
//                .collect(Collectors.groupingBy(ticket -> {
//                    try {
//                        LocalDateTime createdDate = LocalDateTime.parse(ticket.getCreatedDate().replace(" ", "T"));
//                        return createdDate.toLocalDate();
//                    } catch (Exception e) {
//                        log.warn("Error parsing created date: {}, using current date", ticket.getCreatedDate());
//                        return LocalDate.now();
//                    }
//                }));
//
//            // Create grouped tickets list
//            List<GroupedTicketDTO> groupedTickets = new ArrayList<>();
//            LocalDate today = LocalDate.now();
//            LocalDate yesterday = today.minusDays(1);
//
//            // Sort the map by date in descending order and create GroupedTicketDTO objects
//            groupedByDate.entrySet().stream()
//                .sorted(Map.Entry.<LocalDate, List<TicketDTO>>comparingByKey().reversed())
//                .forEach(entry -> {
//                    LocalDate date = entry.getKey();
//                    List<TicketDTO> dateTickets = entry.getValue();
//
//                    String dateLabel = formatDateLabel(date, today, yesterday);
//                    groupedTickets.add(new GroupedTicketDTO(dateLabel, dateTickets));
//
//                    log.debug("Added {} tickets for date: {}", dateTickets.size(), dateLabel);
//                });
//
//            // Calculate total count
//            int totalCount = ticketDTOs.size();
//
//            // Create the complete response structure
//            TicketDataWrapper dataWrapper = new TicketDataWrapper(totalCount, groupedTickets);
//            TicketMainResponseDTO mainResponse = new TicketMainResponseDTO(dataWrapper);
//
//            log.info("Successfully grouped {} tickets into {} date groups", totalCount, groupedTickets.size());
//            return mainResponse;
//
//        } catch (Exception e) {
//            log.error("Error fetching tickets: ", e);
//            throw new RuntimeException("Failed to fetch tickets", e);
//        }
//    }
//
//    private String formatDateLabel(LocalDate date, LocalDate today, LocalDate yesterday) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
//
//        if (date.equals(today)) {
//            return "Today | " + date.format(formatter);
//        } else if (date.equals(yesterday)) {
//            return "Yesterday | " + date.format(formatter);
//        } else {
//            return date.format(formatter);
//        }
//    }
//
//    private TicketDTO convertToDTO(Tickets entity) {
//        log.debug("Converting ticket entity to DTO for srno: {}", entity.getSrno());
//
//        TicketDTO dto = new TicketDTO();
//
//        try {
//            dto.setRno(entity.getRno());
//            dto.setTicketNumber(entity.getSrno());
//            dto.setBank(entity.getCustomer());
//            dto.setEquipmentId(entity.getEquipmentId());
//            dto.setModel(entity.getModel());
//            dto.setAtmCategory(entity.getAtmCategory());
//            dto.setAtmClassification(entity.getAtmClassification());
//            dto.setCallDate(entity.getCallDate());
//            dto.setCreatedDate(entity.getCreatedDate());
//            dto.setCallType(entity.getCallType());
//            dto.setSubCallType(entity.getSubCallType());
//            dto.setCompletionDateWithTime(entity.getCompletionDateWithTime());
//            dto.setDowntimeInMins(entity.getDowntimeInMins());
//            dto.setVendor(entity.getVendor());
//            dto.setServiceCode(entity.getServiceCode());
//            dto.setDiagnosis(entity.getDiagnosis());
//            dto.setEventCode(entity.getEventCode());
//            dto.setHelpdeskName(entity.getHelpdeskName());
//            dto.setLastAllocatedTime(entity.getLastAllocatedTime());
//            dto.setLastComment(entity.getLastComment());
//            dto.setLastActivity(entity.getLastActivity());
//            dto.setStatus(entity.getStatus());
//            dto.setSubStatus(entity.getSubStatus());
//            dto.setRo(entity.getRo());
//            dto.setSite(entity.getSite());
//            dto.setAddress(entity.getAddress());
//            dto.setCity(entity.getCity());
//            dto.setLocationName(entity.getLocationName());
//            dto.setState(entity.getState());
//            dto.setNextFollowUp(entity.getNextFollowUp());
//            dto.setEtaDateTime(entity.getEtaDateTime());
//            dto.setOwner(entity.getOwner());
//            dto.setCustomerRemark(entity.getCustomerRemark());
//            dto.setAgeingDays(entity.getAgeingDays());
//            dto.setHimsTicketStatus(entity.getAtm_status());
//            dto.setTotalRecords(entity.getTotal_records());
//            dto.setCurrentPage(entity.getCurrent_page());
//            dto.setPageSize(entity.getPage_size());
//            dto.setTotalPages(entity.getTotal_pages());
//            dto.setCeName(entity.getCe_user_id());
//
//        } catch (Exception e) {
//            log.error("Error converting entity to DTO for srno: {}", entity.getSrno(), e);
//            throw new RuntimeException("Error converting ticket data", e);
//        }
//
//        return dto;
//    }

	// --------------------

	public TicketMainResponseDTO getTicketsGroupedByDate(TicketRequestDTO request) {
		String CmUserId = loginService.getLoggedInUser();
		log.info("Fetching tickets for cmUserId: {}, pageSize: {}, pageNumber: {}, flag: {}", CmUserId,
				request.getPageSize(), request.getPageNumber(), request.getFlag());

		try {
			List<Tickets> tickets = ticketRepository.getTicketsForCm(request.getCmUserId(), request.getPageSize(),
					request.getPageNumber(), request.getFlag(), request.getSearchText());

			log.info("Retrieved {} tickets from database", tickets.size());

			// Convert entities to DTOs
			List<TicketDTO> ticketDTOs = tickets.stream().map(this::convertToDTO).collect(Collectors.toList());

			// Group tickets by date
			Map<LocalDate, List<TicketDTO>> groupedByDate = ticketDTOs.stream()
					.collect(Collectors.groupingBy(ticket -> {
						try {
							String createdDate = ticket.getCreatedDate();
							LocalDate ticketDate = null;

							// Handle different date formats
							if (createdDate.contains(",")) {
								String datePart = createdDate.split(",")[0].trim(); // "07 Aug 2025"
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
								ticketDate = LocalDate.parse(datePart, formatter);
							} else if (createdDate.contains("-") && createdDate.contains(":")) {
								LocalDateTime dateTime = LocalDateTime.parse(createdDate.replace(" ", "T"));
								ticketDate = dateTime.toLocalDate();
							} else {
								LocalDateTime dateTime = LocalDateTime.parse(createdDate.replace(" ", "T"));
								ticketDate = dateTime.toLocalDate();
							}

							log.debug("Parsed date: {} from: {}", ticketDate, createdDate);
							return ticketDate;

						} catch (Exception e) {
							log.warn("Error parsing created date: {}, using current date. Error: {}",
									ticket.getCreatedDate(), e.getMessage());
							return LocalDate.now();
						}
					}));

			// Create grouped tickets list
			List<GroupedTicketDTO> groupedTickets = new ArrayList<>();
			LocalDate today = LocalDate.now();
			LocalDate yesterday = today.minusDays(1);

			// Sort the map by date in descending order and create GroupedTicketDTO objects
			groupedByDate.entrySet().stream().sorted(Map.Entry.<LocalDate, List<TicketDTO>>comparingByKey().reversed())
					.forEach(entry -> {
						LocalDate date = entry.getKey();
						List<TicketDTO> dateTickets = entry.getValue();

						String dateLabel = formatDateLabel(date, today, yesterday);
						groupedTickets.add(new GroupedTicketDTO(dateLabel, dateTickets));

						log.debug("Added {} tickets for date: {}", dateTickets.size(), dateLabel);
					});

			// Get total count from totalRecords field of first ticket
			int totalCount = 0;
			if (!ticketDTOs.isEmpty() && ticketDTOs.get(0).getTotalRecords() != null) {
				try {
					totalCount = Integer.parseInt(ticketDTOs.get(0).getTotalRecords());
				} catch (NumberFormatException e) {
					log.warn("Error parsing totalRecords: {}, falling back to list size",
							ticketDTOs.get(0).getTotalRecords());
					totalCount = ticketDTOs.size();
				}
			} else {
				totalCount = ticketDTOs.size();
			}

			// Create the flattened response - no wrapper needed
			TicketMainResponseDTO mainResponse = new TicketMainResponseDTO(null, totalCount, groupedTickets);

			log.info("Successfully grouped {} tickets into {} date groups", totalCount, groupedTickets.size());
			return mainResponse;

		} catch (Exception e) {
			log.error("Error fetching tickets: ", e);
			throw new RuntimeException("Failed to fetch tickets", e);
		}
	}

	private String formatDateLabel(LocalDate date, LocalDate today, LocalDate yesterday) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

		if (date.equals(today)) {
			return "Today | " + date.format(formatter);
		} else if (date.equals(yesterday)) {
			return "Yesterday | " + date.format(formatter);
		} else {
			return date.format(formatter);
		}
	}

	private TicketDTO convertToDTO(Tickets entity) {
		log.debug("Converting ticket entity to DTO for srno: {}", entity.getSrno());

		TicketDTO dto = new TicketDTO();

		try {
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
			dto.setCompletionDateWithTime(entity.getCompletionDateWithTime());
			dto.setDowntimeInMins(entity.getDowntimeInMins());
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
			dto.setAgeingDays(entity.getAgeingDays());
			dto.setHimsTicketStatus(entity.getAtm_status());
			dto.setTotalRecords(entity.getTotal_records());
			dto.setCurrentPage(entity.getCurrent_page());
			dto.setPageSize(entity.getPage_size());
			dto.setTotalPages(entity.getTotal_pages());
			dto.setCeName(entity.getCe_user_id());
			dto.setFlagStatus(entity.getPinStatus());

		} catch (Exception e) {
			log.error("Error converting entity to DTO for srno: {}", entity.getSrno(), e);
			throw new RuntimeException("Error converting ticket data", e);
		}

		return dto;
	}
	
	//---------------------for search ----Api----------------------
	
	
	
	
	private SearchTicketsDto searchConvertToDTO(SearchTickets entity) {
		log.debug("Converting ticket entity to DTO for srno: {}", entity.getSrno());

		SearchTicketsDto dto = new SearchTicketsDto();

		try {
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
            dto.setCompletionDateWithTime(entity.getCompletionDateWithTime());
            dto.setDowntimeInMins(entity.getDowntimeInMins());
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
            dto.setAgeingDays(entity.getAgeingDays());
            dto.setCeUserId(entity.getCeUserId());
            dto.setFlag(entity.getFlag());
            dto.setAtmStatus(entity.getAtmStatus());
            dto.setTotalRecords(entity.getTotalRecords());

		} catch (Exception e) {
			log.error("Error converting entity to DTO for srno: {}", entity.getSrno(), e);
			throw new RuntimeException("Error converting ticket data", e);
		}

		return dto;
	}
	
	public List<SearchTicketsDto> getSearchTickets(TicketSearchRequestDTO request) {
        log.info("Fetching all tickets for cmUserId: {}, flag: {}", 
                request.getCmUserId(), request.getFlag());

        try {
            // Call stored procedure
            List<SearchTickets> tickets = searchTicketsRepository.getTicketsForCmNative(
                    request.getCmUserId(),
                    request.getFlag()
            );

            log.info("Retrieved {} tickets from database", tickets.size());

            // Convert entities to DTOs
            List<SearchTicketsDto> ticketDTOs =  tickets.stream()
                    .map(this::searchConvertToDTO)
                    .collect(Collectors.toList());

            log.info("Successfully converted {} tickets to DTOs", ticketDTOs.size());
            
            // Create TicketSearchResponseDTO and set the list as data
            //TicketSearchResponseDTO response = new TicketSearchResponseDTO();
            //response.setData(ticketDTOs);
            return ticketDTOs;

        } catch (Exception e) {
            log.error("Error fetching tickets for cmUserId: {}, flag: {}", 
                    request.getCmUserId(), request.getFlag(), e);
            throw new RuntimeException("Failed to fetch tickets: " + e.getMessage(), e);
        }
    }

	public List<String> getOpenClosedTicketSuggestion(TicketSuggestionReq request) {
		log.info("getOpenClosedTicketSuggestion request: {}",request);
		if (request == null || request.getCmUserId().isEmpty() || request.getFlag().isEmpty()) {
			return Collections.emptyList();
		}
		List<TicketSearchSuggestion> ticket=ticketSearchSuggestionRepository.getTicketSearchSuggestion(request.getCmUserId(), request.getFlag());
		
		if (ticket.isEmpty() || ticket == null) {
			return Collections.emptyList();
		}
		log.info("ticket suggestions:{}",ticket);
		return ticket.stream()
	    .map(TicketSearchSuggestion::getTicketNumber)
	    .filter(Objects::nonNull) // optional: skip nulls
	    .toList();
	}
}
