package com.hpy.ops360.ticketing.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;
import com.hpy.ops360.ticketing.cm.entity.TicketdetailsEntityHims;
import com.hpy.ops360.ticketing.config.AssetServiceClient;
import com.hpy.ops360.ticketing.dto.AtmHistoryNTicketsResponse;
import com.hpy.ops360.ticketing.dto.TicketDetailsDto;
import com.hpy.ops360.ticketing.dto.TicketHistoryDto;
import com.hpy.ops360.ticketing.entity.AtmTicketEvent;
import com.hpy.ops360.ticketing.entity.BroadCategory;
import com.hpy.ops360.ticketing.entity.CheckAtmSource;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.AtmTicketEventRepository;
import com.hpy.ops360.ticketing.repository.BroadCategoryRepository;
import com.hpy.ops360.ticketing.repository.CheckAtmSourceRepository;
import com.hpy.ops360.ticketing.repository.ManualTicketValidationRepository;
import com.hpy.ops360.ticketing.repository.TaskRepository;
import com.hpy.ops360.ticketing.repository.TickethistoryForCEHism;
import com.hpy.ops360.ticketing.utils.DateUtil;
import com.hpy.ops360.ticketing.utils.TicketColorUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NTicketHistoryService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TicketColorUtils ticketColorUtils;

	@Autowired
	private LoginService loginService;
	@Autowired
	private SynergyService synergyService;
	@Autowired
	private AtmTicketEventRepository atmTicketEventRepository;

	@Autowired
	private BroadCategoryRepository broadCategoryRepository;

	@Autowired
	private ManualTicketValidationRepository manualTicketValidationRepository;

	@Autowired
	private AssetServiceClient assetServiceClient;

	@Autowired
	private CheckAtmSourceRepository checkAtmSourceRepository;

	@Autowired
	private TickethistoryForCEHism nTickethistoryForCEHism;

	public List<AtmShortDetailsDto> getnTicketList(TicketHistoryDto atms, int maxLimit) {
		try {
			List<CheckAtmSource> atm = checkAtmSourceRepository.checkAtmSource(atms.getAtmid());
			List<AtmShortDetailsDto> downCallList = new ArrayList<>();
			Map<String, TicketDetailsDto> ticketDetailsMap = new HashMap<>();
			Map<String, TicketdetailsEntityHims> ticketDetailsHimsMap = new HashMap<>();
			StringBuilder atmTicketEventCode = new StringBuilder();

			if (atm.isEmpty()) {
				log.error("Atm list with source is empty insert valid atm id");
				throw new IllegalArgumentException("Atm list with source is empty");
			}
			log.info("Atm list with source response: {}", atm);
			int source = atm.get(0).getSourceCode();

			if (source == 0) { // synergy
				AtmHistoryNTicketsResponse nTicketsResponse = synergyService.getntickets(atms);
				if (nTicketsResponse.getTicketDetails().isEmpty()) {
					return Collections.emptyList();
				}

				for (TicketDetailsDto ticketDetailsDTO : nTicketsResponse.getTicketDetails()) {
					String uniqueCode = String.format("%s|%s|%s|%s|%s", ticketDetailsDTO.getEquipmentid(),
							ticketDetailsDTO.getSrno(), ticketDetailsDTO.getEventcode(),
							ticketDetailsDTO.getNextfollowup(), ticketDetailsDTO.getCalldate());
					atmTicketEventCode.append(uniqueCode).append(",");
					ticketDetailsMap.put(ticketDetailsDTO.getEquipmentid() + ticketDetailsDTO.getSrno(),
							ticketDetailsDTO);
				}

				String atmTicketEventCodeList = atmTicketEventCode.toString();
				atmTicketEventCodeList = atmTicketEventCodeList.substring(0, atmTicketEventCodeList.length() - 1);
				log.info("atmTicketEventCode:{}", atmTicketEventCodeList);

				List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
						.getAtmTicketEvent(loginService.getLoggedInUser(), atmTicketEventCodeList);

				for (AtmTicketEvent atmTicketEvent : atmTicketEventList) {
					TicketDetailsDto ticketDetailsDto = ticketDetailsMap
							.get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());
					String ticketStatus = ticketDetailsDto.getStatus().toString();

					log.info("********** Created date is ********** " + ticketDetailsDto.getCreateddate());
					log.info(
							"********** Completion date is ********** " + ticketDetailsDto.getCompletiondatewithtime());
					AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
							.atmId(ticketDetailsDto.getEquipmentid()).ticketNumber(ticketDetailsDto.getSrno())
							.bank(ticketDetailsDto.getCustomer()).siteName(ticketDetailsDto.getEquipmentid())
							.owner(ticketDetailsDto.getSubcalltype() != null
									? getSelectedBroadCategory(ticketDetailsDto.getSubcalltype(),
											ticketDetailsDto.getSrno(), ticketDetailsDto.getEquipmentid())
									: "")
							.subcall(ticketDetailsDto.getSubcalltype()).vendor(ticketDetailsDto.getVendor())
							.error(ticketDetailsDto.getEventcode())
							.downTime(getDownTimeInHrs1(ticketDetailsDto.getCreateddate(),
									ticketDetailsDto.getCompletiondatewithtime() == null ? ""
											: ticketDetailsDto.getCompletiondatewithtime()))
							.priorityScore(atmTicketEvent.getPriorityScore()).eventGroup(atmTicketEvent.getEventGroup())
							.isBreakdown(atmTicketEvent.getIsBreakdown() == null ? 0 : atmTicketEvent.getIsBreakdown())
							.isUpdated(atmTicketEvent.getIsUpdated() == null ? 0 : atmTicketEvent.getIsUpdated())
							.isTimedOut(atmTicketEvent.getIsTimedOut() == null ? 0 : atmTicketEvent.getIsTimedOut())
							.isTravelling(
									atmTicketEvent.getIsTravelling() == null ? 0 : atmTicketEvent.getIsTravelling())
							.travelTime(atmTicketEvent.getTravelTime()).travelEta(atmTicketEvent.getTravelEta())
							.downCall(atmTicketEvent.getDownCall() == null ? 0 : atmTicketEvent.getDownCall())
							.etaDateTime((atmTicketEvent.getFormattedEtaDateTime() == null ? ""
									: atmTicketEvent.getFormattedEtaDateTime()))
							.etaTimeout(atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout())
							.CreatedDate(DateUtil.formatDateStringIndexOfATM(
									ticketDetailsDto.getCreateddate() == null ? "" : ticketDetailsDto.getCreateddate()))
							.closeDate(DateUtil.formatDateStringIndexOfATM(
									ticketDetailsDto.getCompletiondatewithtime() == null ? ""
											: ticketDetailsDto.getCompletiondatewithtime()))
							.ceName(atmTicketEvent.getCeName())
							.createdTime(extractTimeInAmPmRobust(
									ticketDetailsDto.getCreateddate() == null ? "" : ticketDetailsDto.getCreateddate()))
							.closedTime(
									extractTimeInAmPmRobust(ticketDetailsDto.getCompletiondatewithtime() == null ? ""
											: ticketDetailsDto.getCompletiondatewithtime()))
							.SynergyStatus(ticketStatus).remark(atmTicketEvent.getInternalRemark())
							.hr(calculateHoursBetween(
									DateUtil.formatDateStringIndexOfATM(ticketDetailsDto.getCreateddate() == null ? ""
											: ticketDetailsDto.getCreateddate()),
									DateUtil.formatDateStringIndexOfATM(
											ticketDetailsDto.getCompletiondatewithtime() == null ? ""
													: ticketDetailsDto.getCompletiondatewithtime())))
							.build();
					downCallList.add(atmShortDetailsDto);
				}

				return downCallList.stream().sorted((a, b) -> {
					if (a.getCloseDate().isEmpty() || b.getCloseDate().isEmpty()) {
						return 0;
					}
					DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a",
							Locale.US);
					LocalDateTime dt1 = LocalDateTime.parse(a.getCloseDate(), displayFormatter);
					LocalDateTime dt2 = LocalDateTime.parse(b.getCloseDate(), displayFormatter);
					return dt1.compareTo(dt2);
				}).collect(Collectors.toList());

			} else {
				String atmid = atms.getAtmid();
				List<TicketdetailsEntityHims> openTicketsByEquipmentId = nTickethistoryForCEHism
						.findOpenTicketsByEquipmentId(atmid);
				System.out.println("openTicketsByEquipmentId :" + openTicketsByEquipmentId);

				if (openTicketsByEquipmentId.isEmpty()) {
					return Collections.emptyList();
				}

				for (TicketdetailsEntityHims ticketdetailsEntityHims : openTicketsByEquipmentId) {
					String uniqueCode = String.format("%s|%s|%s|%s|%s", ticketdetailsEntityHims.getEquipmentid(),
							ticketdetailsEntityHims.getSrno(), ticketdetailsEntityHims.getEventcode(),
							ticketdetailsEntityHims.getNextfollowup(), ticketdetailsEntityHims.getCalldate());
					atmTicketEventCode.append(uniqueCode).append(",");
					ticketDetailsHimsMap.put(
							ticketdetailsEntityHims.getEquipmentid() + ticketdetailsEntityHims.getSrno(),
							ticketdetailsEntityHims);
				}

				String atmTicketEventCodeList = atmTicketEventCode.toString();
				atmTicketEventCodeList = atmTicketEventCodeList.substring(0, atmTicketEventCodeList.length() - 1);
				log.info("atmTicketEventCode:{}", atmTicketEventCodeList);

				List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
						.getAtmTicketEvent(loginService.getLoggedInUser(), atmTicketEventCodeList);

				for (AtmTicketEvent atmTicketEvent : atmTicketEventList) {
					TicketdetailsEntityHims ticketDetailsDto = ticketDetailsHimsMap
							.get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());
					String ticketStatus = ticketDetailsDto.getStatus().toString();

					log.info("********** Created date is ********** " + ticketDetailsDto.getCreateddate());
					log.info(
							"********** Completion date is ********** " + ticketDetailsDto.getCompletiondatewithtime());
					AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
							.atmId(ticketDetailsDto.getEquipmentid()).ticketNumber(ticketDetailsDto.getSrno()!= null?ticketDetailsDto.getSrno():"")
							.bank(ticketDetailsDto.getCustomer()).siteName(ticketDetailsDto.getEquipmentid())
							.owner(ticketDetailsDto.getSubcalltype() != null
									? getSelectedBroadCategory(ticketDetailsDto.getSubcalltype(),
											ticketDetailsDto.getSrno(), ticketDetailsDto.getEquipmentid())
									: "")
							.subcall(ticketDetailsDto.getSubcalltype()).vendor(ticketDetailsDto.getVendor())
							.error(ticketDetailsDto.getEventcode())
							.downTime(getDownTimeInHrs1(ticketDetailsDto.getCreateddate(),
									ticketDetailsDto.getCompletiondatewithtime() == null ? ""
											: ticketDetailsDto.getCompletiondatewithtime()))
							.priorityScore(atmTicketEvent.getPriorityScore()).eventGroup(atmTicketEvent.getEventGroup())
							.isBreakdown(atmTicketEvent.getIsBreakdown() == null ? 0 : atmTicketEvent.getIsBreakdown())
							.isUpdated(atmTicketEvent.getIsUpdated() == null ? 0 : atmTicketEvent.getIsUpdated())
							.isTimedOut(atmTicketEvent.getIsTimedOut() == null ? 0 : atmTicketEvent.getIsTimedOut())
							.isTravelling(
									atmTicketEvent.getIsTravelling() == null ? 0 : atmTicketEvent.getIsTravelling())
							.travelTime(atmTicketEvent.getTravelTime()).travelEta(atmTicketEvent.getTravelEta())
							.downCall(atmTicketEvent.getDownCall() == null ? 0 : atmTicketEvent.getDownCall())
							.etaDateTime((atmTicketEvent.getFormattedEtaDateTime() == null ? ""
									: atmTicketEvent.getFormattedEtaDateTime()))
							.etaTimeout(atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout())
							.CreatedDate(DateUtil.formatDateStringIndexOfATM(
									ticketDetailsDto.getCreateddate() == null ? "" : ticketDetailsDto.getCreateddate()))
							.closeDate(DateUtil.formatDateStringIndexOfATM(
									ticketDetailsDto.getCompletiondatewithtime() == null ? ""
											: ticketDetailsDto.getCompletiondatewithtime()))
							.ceName(atmTicketEvent.getCeName())
							.createdTime(extractTimeInAmPmRobust(
									ticketDetailsDto.getCreateddate() == null ? "" : ticketDetailsDto.getCreateddate()))
							.closedTime(
									extractTimeInAmPmRobust(ticketDetailsDto.getCompletiondatewithtime() == null ? ""
											: ticketDetailsDto.getCompletiondatewithtime()))
							.himsStatus(ticketStatus).remark(atmTicketEvent.getInternalRemark())
							.hr(calculateHoursBetween(
									DateUtil.formatDateStringIndexOfATM(ticketDetailsDto.getCreateddate() == null ? ""
											: ticketDetailsDto.getCreateddate()),
									DateUtil.formatDateStringIndexOfATM(
											ticketDetailsDto.getCompletiondatewithtime() == null ? ""
													: ticketDetailsDto.getCompletiondatewithtime())))
							.build();
					downCallList.add(atmShortDetailsDto);
				}

				return downCallList.stream().sorted((a, b) -> {
					if (a.getCloseDate().isEmpty() || b.getCloseDate().isEmpty()) {
						return 0;
					}
					DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a",
							Locale.US);
					LocalDateTime dt1 = LocalDateTime.parse(a.getCloseDate(), displayFormatter);
					LocalDateTime dt2 = LocalDateTime.parse(b.getCloseDate(), displayFormatter);
					return dt1.compareTo(dt2);
				}).collect(Collectors.toList());
			}
		} catch (Exception e) {
			log.error("Unexpected error in getnTicketList", e);
			return Collections.emptyList(); // Return empty list instead of null
		}
	}

	public String getSelectedBroadCategory(String subcallType, String ticketNumber, String atmId) {
		if (subcallType.isEmpty() || subcallType == null) {
			return "";
		}
		List<BroadCategory> broadCategory = broadCategoryRepository.getBroadCategory(loginService.getLoggedInUser(),
				subcallType, ticketNumber, atmId);
		String selectedCategory = broadCategory.get(0).getCurrentValue();
		return selectedCategory;
	}
	
	private String getDownTimeInHrs1(String createdDate, String closeDate) {
		log.info("Calculating downtime between Created: {} and Closed: {}", createdDate, closeDate);

		try {

			// parsing of created date
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.US);
			LocalDateTime startTime = LocalDateTime.parse(createdDate.trim(), formatter);

			// For endTime, use closeDate if available, otherwise current time
			LocalDateTime endTime;
			if (closeDate != null && !closeDate.trim().isEmpty()) {
				endTime = LocalDateTime.parse(closeDate.trim(), formatter);
			} else {
				// Get current time in the same zone
				endTime = LocalDateTime.now(ZoneId.systemDefault());

				// Format current time to match input format for consistency
				String currentFormatted = endTime.format(formatter);
				endTime = LocalDateTime.parse(currentFormatted, formatter);
			}

			// Calculate total minutes between dates
			long totalMinutes = Math.abs(ChronoUnit.MINUTES.between(startTime, endTime));

			// Return early if no significant time difference
			if (totalMinutes == 0) {
				return "0 Min";
			}

			// Calculate hours and remaining minutes
			long hours = totalMinutes / 60;
			long remainingMinutes = totalMinutes % 60;

			// Format the output
			if (hours > 0) {
				return String.format("%d Hrs %d Min", hours, remainingMinutes);
			} else {
				return String.format("%d Min", totalMinutes);
			}

		} catch (DateTimeParseException e) {
			log.error("Failed to parse dates - Created: {}, Closed: {}. Error: {}", createdDate, closeDate,
					e.getMessage());
			return calculateFallbackDowntime(createdDate);
		} catch (Exception e) {
			log.error("Error calculating downtime", e);
			return calculateFallbackDowntime(createdDate);
		}
	}

	private String calculateFallbackDowntime(String createdDate) {
		try {
			// Use downtimeinmins as fallback if available
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.US);
			LocalDateTime startTime = LocalDateTime.parse(createdDate.trim(), formatter);
			LocalDateTime endTime = LocalDateTime.now();

			long minutes = ChronoUnit.MINUTES.between(startTime, endTime);
			if (minutes <= 0)
				return "0 Min";

			long hours = minutes / 60;
			long remainingMinutes = minutes % 60;

			return hours > 0 ? String.format("%d Hrs %d Min", hours, remainingMinutes)
					: String.format("%d Min", minutes);
		} catch (Exception e) {
			log.error("Fallback calculation failed", e);
			return "0 Min";
		}
	}

	private Integer getMonthValue(String month) {
		switch (month.toLowerCase()) {
		case "jan":
			return 1;
		case "feb":
			return 2;
		case "mar":
			return 3;
		case "apr":
			return 4;
		case "may":
			return 5;
		case "jun":
			return 6;
		case "jul":
			return 7;
		case "aug":
			return 8;
		case "sep":
			return 9;
		case "oct":
			return 10;
		case "nov":
			return 11;
		case "dec":
			return 12;
		default:
			return 0;
		}
	}

	private int compareTime(String time1, String time2) {
		try {
			// Example inputs: "11:45 pm", "07:52 pm"
			String[] t1Parts = time1.split(" "); // ["11:45", "pm"]
			String[] t2Parts = time2.split(" "); // ["07:52", "pm"]

			// Convert to military time for easier comparison
			int time1Value = convertToMinutes(t1Parts[0], t1Parts[1]);
			int time2Value = convertToMinutes(t2Parts[0], t2Parts[1]);

			// Return comparison (negative for descending order)
			return Integer.compare(time2Value, time1Value);
		} catch (Exception e) {
			System.err.println("Error comparing times: " + time1 + " and " + time2);
			return 0;
		}
	}

	private String calculateHoursBetween(String createdDate, String closeDate) {
		try {
			// Handle null or empty inputs
			if (createdDate == null || createdDate.trim().isEmpty() || closeDate == null
					|| closeDate.trim().isEmpty()) {
				return "0h 0m 0s";
			}

			// Parse the dates using the correct format
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a", Locale.US);
			LocalDateTime startTime = LocalDateTime.parse(createdDate.trim(), formatter);
			LocalDateTime endTime = LocalDateTime.parse(closeDate.trim(), formatter);

			// Calculate duration between dates
			Duration duration = Duration.between(startTime, endTime).abs();

			// Extract time components
			long hours = duration.toHours();
			long minutes = duration.toMinutesPart();
			long seconds = duration.toSecondsPart();

			return String.format("%dh %dm %ds", hours, minutes, seconds);
		} catch (Exception e) {
			log.error("Error calculating detailed time difference: {}", e.getMessage());
			return "0h 0m 0s";
		}
	}
	
	
	// Alternative: More robust version with multiple date format support
			public String extractTimeInAmPmRobust(String dateTimeString) {
			    if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
			        return "";
			    }
			    
			    // List of possible date formats from your data
			    DateTimeFormatter[] formatters = {
			        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
			        DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm"),
			        DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm"),
			        DateTimeFormatter.ofPattern("dd/M/yyyy HH:mm"),
			        DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"),
			        DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm a")
			    };
			    
			    for (DateTimeFormatter formatter : formatters) {
			        try {
			            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString.trim(), formatter);
			            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
			            return dateTime.format(timeFormatter);
			        } catch (DateTimeParseException e) {
			            // Try next formatter
			            continue;
			        }
			    }
			    
			    log.warn("Unable to parse date with any known format: '{}'", dateTimeString);
			    return "";
			}

		public static final DateTimeFormatter dateGroupFormatter = DateTimeFormatter.ofPattern("dd MMM ''yy");

		public static String formatGroupDate(LocalDate date) {
			return date.format(dateGroupFormatter);
		}
		
		private int convertToMinutes(String time, String meridiem) {
			String[] parts = time.split(":"); // ["11", "45"]
			int hours = Integer.parseInt(parts[0]);
			int minutes = Integer.parseInt(parts[1]);

			// Convert to 24 hour format
			if (meridiem.equalsIgnoreCase("pm") && hours != 12) {
				hours += 12;
			} else if (meridiem.equalsIgnoreCase("am") && hours == 12) {
				hours = 0;
			}

			// Convert to total minutes
			return hours * 60 + minutes;
		}

}
