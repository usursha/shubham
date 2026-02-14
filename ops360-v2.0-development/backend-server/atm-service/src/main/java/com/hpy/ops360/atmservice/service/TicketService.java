package com.hpy.ops360.atmservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.LoginService;
import com.hpy.ops360.atmservice.dto.TicketFilterDataDto;
import com.hpy.ops360.atmservice.dto.TicketFilterMainResponseDto;
import com.hpy.ops360.atmservice.dto.TicketFilterPagenationDto;
import com.hpy.ops360.atmservice.dto.TicketFilterRequestDTO;
import com.hpy.ops360.atmservice.dto.TicketsFilterResponseDto;
import com.hpy.ops360.atmservice.entity.TicketEntity;
import com.hpy.ops360.atmservice.repository.TicketDetailsFilteRepository;
import com.hpy.ops360.atmservice.utils.CustomDateFormattor;
import com.hpy.ops360.atmservice.utils.CustomDateFormattor.FormatStyle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TicketService {

	@Autowired
	private TicketDetailsFilteRepository ticketRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private LoginService loginService;

//	public TicketFilterMainResponseDto getTicketsForCm(TicketFilterRequestDTO request) {
//		String userid = loginService.getLoggedInUser();
//		log.info("Fetching tickets for user: {} with request: {}", userid, request);
//
//		try {
//			// Convert String parameters to appropriate types with validation
//			Integer pageSize = parseInteger(request.getPageSize(), "pageSize");
//			Integer pageNumber = parseInteger(request.getPageNumber(), "pageNumber");
//
//			// Validate pagination parameters
//			if (pageSize <= 0) {
//				throw new IllegalArgumentException("Page size must be greater than 0");
//			}
//			if (pageNumber <= 0) {
//				throw new IllegalArgumentException("Page number must be greater than 0");
//			}
//
//			List<TicketEntity> ticketDetailsList = ticketRepository.getTicketsForCm(userid, pageSize, pageNumber,
//					request.getSearchText(), request.getAtmId(), request.getSort(), request.getCategory(),
//					request.getOwner(), request.getSubCallType(), request.getStatus(), request.getVendor(),
//					request.getTicketAgingHr(), request.getTicketAgingHrFrom(), request.getTicketAgingHrTo(),
//					request.getTicketAgingDay(), request.getTicketAgingDayStart(), request.getTicketAgingDayEnd(),
//					request.getCreationDate(), request.getCreationDateFrom(), request.getCreationDateTo());
//
//			// Convert entities to DTOs
//			List<TicketsFilterResponseDto> ticketDtos = ticketDetailsList.stream().map(this::convertToTicketData)
//					.collect(Collectors.toList());
//
//			// Extract pagination info from first entity (if available)
//			Integer totalRecords = 0;
//			Integer currentPage = pageNumber;
//			Integer totalPages = 0;
//
//			if (!ticketDetailsList.isEmpty()) {
//				TicketEntity firstEntity = ticketDetailsList.get(0);
//				totalRecords = parseInteger(firstEntity.getTotal_records(), "totalRecords");
//				currentPage = parseInteger(firstEntity.getCurrent_page(), "currentPage");
//				totalPages = parseInteger(firstEntity.getTotal_pages(), "totalPages");
//			}
//
//			// Build nested response structure
//			TicketFilterDataDto dataDto = new TicketFilterDataDto();
//			dataDto.setTotalCount(totalRecords);
//			dataDto.setTicketData(ticketDtos);
//
//			TicketFilterMainResponseDto response = new TicketFilterMainResponseDto();
//			response.setData(dataDto);
//			response.setTotalRecords(totalRecords);
//			response.setCurrentPage(currentPage);
//			response.setPageSize(pageSize);
//			response.setTotalPages(totalPages);
//
//			log.info("Successfully fetched {} tickets for user: {}", ticketDtos.size(), userid);
//			return response;
//
//		} catch (NumberFormatException e) {
//			log.error("Invalid number format in request parameters: {}", e.getMessage());
//			throw new IllegalArgumentException("Invalid number format in request parameters: " + e.getMessage());
//		} catch (Exception e) {
//			log.error("Error fetching tickets for user: {}", userid, e);
//			throw new RuntimeException("Failed to fetch tickets: " + e.getMessage(), e);
//		}
//	}
//
//	private TicketsFilterResponseDto convertToTicketData(TicketEntity entity) {
//		if (entity == null) {
//			return null;
//		}
//
//		TicketsFilterResponseDto dto = new TicketsFilterResponseDto();
//
//		dto.setRno(entity.getRno());
//		dto.setTicketNumber(entity.getSrno());
//		dto.setBank(entity.getCustomer());
//		dto.setEquipmentId(entity.getEquipmentId());
//		dto.setModel(entity.getModel());
//		dto.setAtmCategory(entity.getAtmCategory());
//		dto.setAtmClassification(entity.getAtmClassification());
//		dto.setCallDate(CustomDateFormattor.convert(entity.getCallDate(), FormatStyle.SHORT));
//		dto.setCreatedDate(CustomDateFormattor.convert(entity.getCreatedDate(), FormatStyle.SHORT));
//		dto.setCallType(entity.getCallType());
//		dto.setSubCallType(entity.getSubCallType());
//		dto.setCompletionDateWithTime(entity.getCompletionDateWithTime());
//		dto.setDowntimeInMins(entity.getDowntimeInMins());
//		dto.setVendor(entity.getVendor());
//		dto.setServiceCode(entity.getServiceCode());
//		dto.setDiagnosis(entity.getDiagnosis());
//		dto.setEventCode(entity.getEventCode());
//		dto.setHelpdeskName(entity.getHelpdeskName());
//		dto.setLastAllocatedTime(entity.getLastAllocatedTime());
//		dto.setLastComment(entity.getLastComment());
//		dto.setLastActivity(entity.getLastActivity());
//		dto.setStatus(entity.getStatus());
//		dto.setSubStatus(entity.getSubStatus());
//		dto.setRo(entity.getRo());
//		dto.setSite(entity.getSite());
//		dto.setAddress(entity.getAddress());
//		dto.setCity(entity.getCity());
//		dto.setLocationName(entity.getLocationName());
//		dto.setState(entity.getState());
//		dto.setNextFollowUp(entity.getNextFollowUp());
//		dto.setEtaDateTime(CustomDateFormattor.convert(entity.getEtaDateTime(), FormatStyle.SHORT));
//		dto.setOwner(entity.getOwner());
//		dto.setCustomerRemark(entity.getCustomerRemark());
//		dto.setAgeingDays(entity.getAgeingDays());
//		dto.setHimsTicketStatus(entity.getAtm_status());
//		dto.setCeName(entity.getCe_user_id());
//		dto.setFlagStatus(entity.getPinStatus());
//
//		return dto;
//	}
//
//	private Integer parseInteger(String value, String fieldName) {
//		if (value == null || value.trim().isEmpty()) {
//			throw new IllegalArgumentException(fieldName + " cannot be null or empty");
//		}
//		try {
//			return Integer.parseInt(value.trim());
//		} catch (NumberFormatException e) {
//			throw new NumberFormatException("Invalid " + fieldName + ": " + value);
//		}
//	}

	public TicketFilterMainResponseDto getTicketsForCM(TicketFilterRequestDTO request) {
		String userid = loginService.getLoggedInUser();
		try {
			List<TicketEntity> ticketEntities = ticketRepository.getTicketsForCM(userid, request.getPageSize(),
					request.getPageNumber(), request.getSearchText(), request.getAtmId(), request.getSort(),
					request.getCategory(), request.getOwner(), request.getSubCallType(), request.getStatus(),
					request.getVendor(), request.getTicketAgingHr(), request.getTicketAgingHrFrom(),
					request.getTicketAgingHrTo(), request.getTicketAgingDay(), request.getTicketAgingDayStart(),
					request.getTicketAgingDayEnd(), request.getCreationDate(), request.getCreationDateFrom(),
					request.getCreationDateTo());

			return buildResponse(ticketEntities);

		} catch (Exception e) {
			throw new RuntimeException("Error fetching tickets: " + e.getMessage(), e);
		}
	}

	private TicketFilterMainResponseDto buildResponse(List<TicketEntity> ticketEntities) {
		TicketFilterMainResponseDto response = new TicketFilterMainResponseDto();

		if (!ticketEntities.isEmpty()) {
			TicketEntity firstEntity = ticketEntities.get(0);

			// Map entities to DTOs
			List<TicketsFilterResponseDto> ticketDTOs = ticketEntities.stream().map(this::convertToTicketDTO)
					.collect(Collectors.toList());

			// Build data object
			TicketFilterMainResponseDto.DataDTO dataDTO = new TicketFilterMainResponseDto.DataDTO();
			dataDTO.setTotalCount(firstEntity.getTotal_records());
			dataDTO.setTicketData(ticketDTOs);

			// Set response fields
			response.setData(dataDTO);
			response.setTotalRecords(firstEntity.getTotal_records());
			response.setCurrentPage(firstEntity.getCurrent_page());
			response.setPageSize(firstEntity.getPage_size());
			response.setTotalPages(firstEntity.getTotal_pages());
		} else {
			// Empty response
			TicketFilterMainResponseDto.DataDTO dataDTO = new TicketFilterMainResponseDto.DataDTO();
			dataDTO.setTotalCount("0");
			dataDTO.setTicketData(List.of());

			response.setData(dataDTO);
			response.setTotalRecords("0");
			response.setCurrentPage("1");
			response.setPageSize("10");
			response.setTotalPages("0");
		}

		return response;
	}

	private TicketsFilterResponseDto convertToTicketDTO(TicketEntity entity) {
		TicketsFilterResponseDto dto = new TicketsFilterResponseDto();

		dto.setRno(entity.getRno());
		dto.setSrno(entity.getSrno());
		dto.setCustomer(entity.getCustomer());
		dto.setEquipmentId(entity.getEquipmentid());
		dto.setModel(entity.getModel());
		dto.setAtmCategory(entity.getAtmcategory());
		dto.setAtmClassification(entity.getAtmclassification());
		dto.setCallDate(entity.getCalldate());
		dto.setCreatedDate(entity.getCreateddate());
		dto.setCallType(entity.getCalltype());
		dto.setSubCallType(entity.getSubcalltype());
		dto.setCompletionDateWithTime(entity.getCompletiondatewithtime());
		dto.setDowntimeInMins(entity.getDowntimeinmins());
		dto.setVendor(entity.getVendor());
		dto.setServiceCode(entity.getServicecode());
		dto.setDiagnosis(entity.getDiagnosis());
		dto.setEventCode(entity.getEventcode());
		dto.setHelpdeskName(entity.getHelpdeskname());
		dto.setLastAllocatedTime(entity.getLastallocatedtime());
		dto.setLastComment(entity.getLastcomment());
		dto.setLastActivity(entity.getLastactivity());
		dto.setStatus(entity.getStatus());
		dto.setSubStatus(entity.getSubstatus());
		dto.setRo(entity.getRo());
		dto.setSite(entity.getSite());
		dto.setAddress(entity.getAddress());
		dto.setCity(entity.getCity());
		dto.setLocationName(entity.getLocationname());
		dto.setState(entity.getState());
		dto.setNextFollowUp(entity.getNextfollowup());
		dto.setEtaDateTime(entity.getEtadatetime());
		dto.setOwner(entity.getOwner());
		dto.setCustomerRemark(entity.getCustomerRemark());
		dto.setTicketSource(entity.getTicketSource());
		dto.setFormattedDowntime(entity.getFormattedDowntime());
		dto.setAgeingDays(entity.getAgeingDays());
		dto.setAtmStatus(entity.getAtmStatus());
		dto.setCeUserId(entity.getCeUserId());
		dto.setFlagStatus(entity.getFlagStatus());

		return dto;
	}
}