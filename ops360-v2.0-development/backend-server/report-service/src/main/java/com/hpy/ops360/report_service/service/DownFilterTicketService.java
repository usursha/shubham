package com.hpy.ops360.report_service.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.report_service.config.Helper;
import com.hpy.ops360.report_service.dto.AtmidDto;
import com.hpy.ops360.report_service.dto.BankDto;
import com.hpy.ops360.report_service.dto.BusinessModelDto;
import com.hpy.ops360.report_service.dto.CENameDto;
import com.hpy.ops360.report_service.dto.EtaDateTimeDto;
import com.hpy.ops360.report_service.dto.OwnerDto;
import com.hpy.ops360.report_service.dto.SiteTypeDto;
import com.hpy.ops360.report_service.dto.StatusDto;
import com.hpy.ops360.report_service.dto.SubcallTypeDto;
import com.hpy.ops360.report_service.dto.TicketFilterResponse;
import com.hpy.ops360.report_service.dto.TicketNumberDto;
import com.hpy.ops360.report_service.entity.TicketDetails;
import com.hpy.ops360.report_service.repository.DownFilterTicketRepository;
import com.hpy.ops360.report_service.request.TicketFilterRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DownFilterTicketService {

	@Autowired
	private DownFilterTicketRepository ticketRepository;

	@Autowired
	private Helper helper;

	public TicketFilterResponse getFilteredTickets(TicketFilterRequest request) {
		log.info("Fetching filtered tickets for Manager: {}, CE: {}, Date Range: {} to {}", helper.getLoggedInUser(),
				request.getCeFullName(), request.getStartDate(), request.getEndDate());

		Integer realstatus = null;
		if (request.getStatus() == "Open") {
			realstatus = 1;
		} else if (request.getStatus() == "Close") {
			realstatus = 0;
		}

		// validation for manager name, startdate and enddate
		if (helper.getLoggedInUser() == null || helper.getLoggedInUser().isEmpty()) {
			log.error("Manager name is required");
			throw new IllegalArgumentException("Manager name is required");
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

		if (request.getStartDate() == null || request.getStartDate().isEmpty() || request.getEndDate() == null
				|| request.getEndDate().isEmpty()) {

			LocalDate today = LocalDate.now();
			String defaultStartDate = today.minusDays(5).format(formatter); // Five days ago
			String defaultEndDate = today.minusDays(1).format(formatter); // Yesterday

			request.setStartDate(defaultStartDate);
			request.setEndDate(defaultEndDate);

			log.info("Start and end dates not provided. Defaulting to: " + "StartDate = " + defaultStartDate
					+ ", EndDate = " + defaultEndDate);
		}

		log.info("request Recieved:- " + request);
		List<TicketDetails> results = ticketRepository.getFilteredTickets(request.getStartDate(), request.getEndDate(),
				helper.getLoggedInUser(), request.getCeFullName(), // CEFullName
				request.getAtmId(), // ATMID
				request.getBank(), // Bank
				realstatus, // Status
				request.getTicketNumber(), // TicketNumber
				request.getOwner(), // Owner
				request.getSubCallType(), // SubCallType
				request.getBusinessModel(), request.getSiteType(), request.getEtaDateTime());

		log.info("Total tickets fetched: {}", results.size());

		TicketFilterResponse response = new TicketFilterResponse();

		response.setAtmid(
				results.stream().map(r -> new AtmidDto(r.getAtmid())).distinct().collect(Collectors.toList()));
		log.debug("Distinct ATM IDs: {}", response.getAtmid().size());

		response.setBank(results.stream().map(r -> new BankDto(r.getBank())).distinct().collect(Collectors.toList()));
		log.debug("Distinct Banks: {}", response.getBank().size());

		response.setStatus(
				results.stream().map(r -> new StatusDto(r.getStatus())).distinct().collect(Collectors.toList()));
		log.debug("Distinct Statuses: {}", response.getStatus().size());

		response.setTicketnumber(results.stream().map(r -> new TicketNumberDto(r.getTicketnumber())).distinct()
				.collect(Collectors.toList()));
		log.debug("Distinct Ticket Numbers: {}", response.getTicketnumber().size());

		response.setOwner(
				results.stream().map(r -> new OwnerDto(r.getOwner())).distinct().collect(Collectors.toList()));
		log.debug("Distinct Owners: {}", response.getOwner().size());

		response.setSubcalltype(results.stream().map(r -> new SubcallTypeDto(r.getSubcalltype())).distinct()
				.collect(Collectors.toList()));
		log.debug("Distinct Subcall Types: {}", response.getSubcalltype().size());

		response.setCEFullname(
				results.stream().map(r -> new CENameDto(r.getCefullname())).distinct().collect(Collectors.toList()));
		log.debug("Distinct CE Names: {}", response.getCEFullname().size());

		response.setBusinessmodel(results.stream().map(r -> new BusinessModelDto(r.getBusinessmodel())).distinct()
				.collect(Collectors.toList()));
		log.debug("Distinct Business Models: {}", response.getBusinessmodel().size());

		response.setSitetype(
				results.stream().map(r -> new SiteTypeDto(r.getSitetype())).distinct().collect(Collectors.toList()));
		log.debug("Distinct Site Types: {}", response.getSitetype().size());

		response.setEtadatetime(results.stream().map(r -> new EtaDateTimeDto(r.getEtaDateTime())).distinct()
				.collect(Collectors.toList()));
		log.debug("Distinct ETA DateTimes: {}", response.getEtadatetime().size());

		List<String> sortbyfield = new ArrayList<>();
		// Add sort options
		sortbyfield.add("Latest - Earliest");
		sortbyfield.add("Earliest - Latest");
		response.setSortbyfield(sortbyfield);

		log.info("Ticket filtering completed successfully.");
		return response;
	}
}
