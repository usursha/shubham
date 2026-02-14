package com.hpy.ops360.ticketing.cm.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.TicketWithDocumentsDTO;
import com.hpy.ops360.ticketing.cm.entity.TicketWithDocumentsEntity;
import com.hpy.ops360.ticketing.cm.repo.TicketWithDocumentsRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketWithDocumentsService {

	@Autowired
	private TicketWithDocumentsRepo repo;

	public List<TicketWithDocumentsDTO> fetchTicketsWithDocuments(String atmId, String status, String ticketType,
			String dateStr, String startDate, String endDate) {
		log.info("Fetching tickets with documents for ATM ID: {}", atmId);

		log.info("Fetching tickets with documents for ATM ID: {}", atmId);

		// Override startDate and endDate based on dateStr
		if (dateStr != null && !dateStr.isEmpty()) {
			LocalDate today = LocalDate.now();
			switch (dateStr) {
			case "Today":
				startDate = today.toString();
				endDate = today.toString();
				break;
			case "This Week":
				LocalDate monday = today.with(DayOfWeek.MONDAY);
				startDate = monday.toString();
				endDate = today.toString();
				break;
			case "This Month":
				LocalDate firstOfMonth = today.withDayOfMonth(1);
				startDate = firstOfMonth.toString();
				endDate = today.toString();
				break;
			case "Last 90 Days":
				LocalDate ninetyDaysAgo = today.minusDays(90);
				startDate = ninetyDaysAgo.toString();
				endDate = today.toString();
				break;
			default:
				log.warn("Unknown dateStr value: {}", dateStr);
			}
		}

		List<TicketWithDocumentsEntity> rawResults = repo.getTicketsWithDocuments(atmId, status, ticketType,
				startDate, endDate);
		log.info("Fetched {} ticket records", rawResults.size());

		return rawResults.stream()
				.map(entity -> new TicketWithDocumentsDTO(entity.getSrno(), entity.getTicketNumber(),
						entity.getStatus(), entity.getTicketType(), entity.getIsFlagged(), entity.getHoursPassed(),
						entity.getNoOfAttachments(), entity.getThumbnailImage()))
				.collect(Collectors.toList());
	}
}
