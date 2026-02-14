package com.hpy.ops360.report_service.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.report_service.config.Helper;
import com.hpy.ops360.report_service.dto.ATMUptimeFilterResponse;
import com.hpy.ops360.report_service.dto.AtmUptimeRequestFilter;
import com.hpy.ops360.report_service.dto.AtmidDto;
import com.hpy.ops360.report_service.dto.BankDto;
import com.hpy.ops360.report_service.dto.CityDto;
import com.hpy.ops360.report_service.dto.SiteTypeDto;
import com.hpy.ops360.report_service.dto.StatusDto;
import com.hpy.ops360.report_service.dto.UptimeRangeDTO;
import com.hpy.ops360.report_service.entity.AtmUptimeEntity;
import com.hpy.ops360.report_service.entity.UptimeRange;
import com.hpy.ops360.report_service.repository.AtmUptimeReportRepository;
import com.hpy.ops360.report_service.repository.UptimeRangeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ATMUptimeFilterTicketService {

	@Autowired
	private AtmUptimeReportRepository repository;

	@Autowired
	private UptimeRangeRepository uptimeRangeRepository;

	@Autowired
	private Helper helper;

	public ATMUptimeFilterResponse getFilteredTickets(AtmUptimeRequestFilter request) {

		log.info("Inside Services layer filter Ticket");

		Integer realstatus = null;
		if ("Operational".equals(request.getStatus())) {
			realstatus = 1;
		} else if ("Not Operational".equals(request.getStatus())) {
			realstatus = 0;
		}

		String user = helper.getLoggedInUser();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

		if (request.getStartDate() == null || request.getStartDate().isEmpty() || request.getEndDate() == null
				|| request.getEndDate().isEmpty()) {

			LocalDate today = LocalDate.now();
			String defaultStartDate = today.minusDays(2).format(formatter); // Day before yesterday
			String defaultEndDate = today.minusDays(1).format(formatter); // Yesterday

			request.setStartDate(defaultStartDate);
			request.setEndDate(defaultEndDate);

			log.info("Start and end dates not provided. Defaulting to: " + "StartDate = " + defaultStartDate
					+ ", EndDate = " + defaultEndDate);
		}

		log.info("request Recieved:- " + request);
		List<AtmUptimeEntity> results = repository.getAtmUptimeReportFilter(user, request.getStartDate(),
				request.getEndDate(), request.getUptimeRange(), request.getLiveDate(), request.getAtmId(),
				request.getBankName(), request.getCity(), request.getSite(), request.getSiteId(), realstatus);

		log.info("Total tickets fetched: {}", results.size());

		ATMUptimeFilterResponse response = new ATMUptimeFilterResponse();

		List<UptimeRange> uptimedata = uptimeRangeRepository.findAll();
		// map to dto UptimeRangeDTO
		List<UptimeRangeDTO> uptimeRangeDTOs = uptimedata.stream()
				.map(range -> new UptimeRangeDTO(range.getId(), range.getName(), range.getRange())).toList();

		response.setUptimeRanges(uptimeRangeDTOs);

		response.setStatus(
				results.stream().map(r -> new StatusDto(r.getStatus())).distinct().collect(Collectors.toList()));
		log.info("Distinct Statuses: {}", response.getStatus().size());
		log.info("Statuses: {}", response.getStatus());

		response.setAtmid(
				results.stream().map(r -> new AtmidDto(r.getAtmId())).distinct().collect(Collectors.toList()));
		log.info("Distinct ATM IDs: {}", response.getAtmid().size());
		log.info("ATM IDs: {}", response.getAtmid());

		response.setBank(
				results.stream().map(r -> new BankDto(r.getBankName())).distinct().collect(Collectors.toList()));
		log.info("Distinct Banks: {}", response.getBank().size());
		log.info("Banks: {}", response.getBank());

		response.setPSId(
				results.stream().map(r -> new AtmidDto(r.getSiteId())).distinct().collect(Collectors.toList()));
		log.info("Distinct Banks: {}", response.getBank().size());
		log.info("Banks: {}", response.getBank());

		response.setSitetype(
				results.stream().map(r -> new SiteTypeDto(r.getSite())).distinct().collect(Collectors.toList()));
		log.info("Distinct Site Types: {}", response.getSitetype().size());
		log.info("Site Types: {}", response.getSitetype());

		response.setCity(results.stream().map(r -> new CityDto(r.getCity())).distinct().collect(Collectors.toList()));
		log.info("Distinct Ticket Numbers: {}", response.getCity().size());
		log.info("Cities: {}", response.getCity());

		List<String> sortbyfield = new ArrayList<>();
		// Add sort options
		sortbyfield.add("Date (Latest - Earliest)");
		sortbyfield.add("Date (Earliest - Latest)");
		response.setSortbyfield(sortbyfield);

		log.info("SORT DATA" + response.getSortbyfield());
		log.info("Ticket filtering completed successfully.");
		log.info("Response Data:- " + response);
		return response;
	}
}
