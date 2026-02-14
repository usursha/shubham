package com.hpy.ops360.ticketing.cm.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hpy.ops360.ticketing.cm.dto.CreatedDateDTO;
import com.hpy.ops360.ticketing.cm.dto.FilterDTO;
import com.hpy.ops360.ticketing.cm.dto.StatusDto;
import com.hpy.ops360.ticketing.cm.dto.TicketTypeDTO;
import com.hpy.ops360.ticketing.cm.entity.TicketWithoutDocument;
import com.hpy.ops360.ticketing.cm.repo.TicketImageRepository;

@Service
@Transactional
public class TicketFilterService {

	@Autowired
	private TicketImageRepository ticketImageRepository;

	private static final Map<String, Integer> TICKET_TYPE_MAP = Map.of("Down", 1, "Site Visit", 2, "Incident", 3,
			"Adhoc", 4);

	public FilterDTO getTicketFilters(String atmId) {
		// Get data from stored procedure using existing repository
		List<TicketWithoutDocument> tickets = ticketImageRepository.getTicketsByAtmId(atmId);

		// Build filter response
		FilterDTO filterDTO = new FilterDTO();
		filterDTO.setTicketType(buildTicketTypeFilters(tickets));
		filterDTO.setStatus(buildStatusFilters(tickets));
		filterDTO.setCreatedDate(buildCreatedDateFilters(tickets));

		return filterDTO;
	}

	private List<TicketTypeDTO> buildTicketTypeFilters(List<TicketWithoutDocument> tickets) {
		Map<String, Integer> ticketTypeCounts = new HashMap<>();

		// Initialize all ticket types with 0 count
		ticketTypeCounts.put("Down", 0);
		ticketTypeCounts.put("Site Visit", 0);
		ticketTypeCounts.put("Incident", 0);
		ticketTypeCounts.put("Adhoc", 0);

		// Count tickets by type
		for (TicketWithoutDocument ticket : tickets) {
			String ticketType = getTicketTypeById(ticket.getTicketType());
			if (ticketType != null) {
				ticketTypeCounts.put(ticketType, ticketTypeCounts.get(ticketType) + 1);
			}
		}
		
		int totalCount = ticketTypeCounts.values().stream().mapToInt(Integer::intValue).sum();
	    ticketTypeCounts.put("All", totalCount);

		// Convert to DTO list
		List<TicketTypeDTO> ticketTypeList = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : ticketTypeCounts.entrySet()) {
			Integer id = TICKET_TYPE_MAP.get(entry.getKey());
			ticketTypeList.add(new TicketTypeDTO(id, entry.getKey(), entry.getValue()));
		}

		return ticketTypeList;
	}

	private List<StatusDto> buildStatusFilters(List<TicketWithoutDocument> tickets) {
		int openCount = 0;
		int closedCount = 0;
		int etaExpiredCount = 0;

		for (TicketWithoutDocument ticket : tickets) {
			// Count by status (0 = Closed, 1 = Open)
			if (ticket.getStatus() != null && ticket.getStatus() == 0) {
				closedCount++;
			} else if (ticket.getStatus() != null && ticket.getStatus() == 1) {
				openCount++;
			}

			// Count ETA expired tickets
			if (ticket.getEtaExpired() != null && ticket.getEtaExpired() == 1) {
				etaExpiredCount++;
			}
		}

		List<StatusDto> statusList = new ArrayList<>();
		statusList.add(new StatusDto("Open", openCount));
		statusList.add(new StatusDto("Closed", closedCount));
		statusList.add(new StatusDto("ETA Expired", etaExpiredCount));

		return statusList;
	}

	private List<CreatedDateDTO> buildCreatedDateFilters(List<TicketWithoutDocument> tickets) {
		LocalDate today = LocalDate.now();
		LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate last30Days = today.minusDays(30);
		LocalDate last90Days = today.minusDays(90);

		int todayCount = 0;
		int thisWeekCount = 0;
		int thisMonthCount = 0;
		int last30DaysCount = 0;
		int last90DaysCount = 0;

		for (TicketWithoutDocument ticket : tickets) {
			if (ticket.getCreatedDate() != null) {

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
				LocalDateTime dateTime = LocalDateTime.parse(ticket.getCreatedDate(), formatter);
				LocalDate createdDate = dateTime.toLocalDate();

//				LocalDate createdDate = ticket.getCreatedDate().toInstant().atZone(ZoneId.systemDefault())
//						.toLocalDate();

				if (createdDate.equals(today)) {
					todayCount++;
				}

				if (!createdDate.isBefore(startOfWeek)) {
					thisWeekCount++;
				}

				if (!createdDate.isBefore(startOfMonth)) {
					thisMonthCount++;
				}

				if (!createdDate.isBefore(last30Days)) {
					last30DaysCount++;
				}

				if (!createdDate.isBefore(last90Days)) {
					last90DaysCount++;
				}
			}
		}

		List<CreatedDateDTO> createdDateList = new ArrayList<>();
		createdDateList.add(new CreatedDateDTO("Today", todayCount));
		createdDateList.add(new CreatedDateDTO("This Week", thisWeekCount));
		createdDateList.add(new CreatedDateDTO("This Month", thisMonthCount));
		createdDateList.add(new CreatedDateDTO("Last 30 Days", last30DaysCount));
		createdDateList.add(new CreatedDateDTO("Last 90 Days", last90DaysCount));

		return createdDateList;
	}

	private String getTicketTypeById(Integer ticketTypeId) {
		if (ticketTypeId == null)
			return null;

		switch (ticketTypeId) {
		case 1:
			return "Down";
		case 2:
			return "Site Visit";
		case 3:
			return "Incident";
		case 4:
			return "Adhoc";
		default:
			return null;
		}
	}
}