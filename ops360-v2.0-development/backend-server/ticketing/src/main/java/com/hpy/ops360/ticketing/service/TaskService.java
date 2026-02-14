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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.hpy.generic.impl.GenericService;
import com.hpy.ops360.ticketing.cm.dto.AtmDetailsWithTickets;
import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;
import com.hpy.ops360.ticketing.cm.dto.NotificationFilterRequest;
import com.hpy.ops360.ticketing.cm.entity.TicketdetailsEntityHims;
import com.hpy.ops360.ticketing.config.AssetServiceClient;
import com.hpy.ops360.ticketing.dto.AtmDetailsDto;
import com.hpy.ops360.ticketing.dto.AtmHistoryNTicketsResponse;
import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.LeaveRequestDTO;
import com.hpy.ops360.ticketing.dto.TaskDto;
import com.hpy.ops360.ticketing.dto.TicketDetailsDto;
import com.hpy.ops360.ticketing.dto.TicketHistoryDto;
import com.hpy.ops360.ticketing.dto.TicketTaskDTO;
import com.hpy.ops360.ticketing.entity.AtmTicketEvent;
import com.hpy.ops360.ticketing.entity.BroadCategory;
import com.hpy.ops360.ticketing.entity.CheckAtmSource;
import com.hpy.ops360.ticketing.entity.LeaveRequest;
import com.hpy.ops360.ticketing.entity.ManualTicketValidation;
import com.hpy.ops360.ticketing.entity.Task;
import com.hpy.ops360.ticketing.entity.TicketTask;
import com.hpy.ops360.ticketing.enums.TicketCategory;
import com.hpy.ops360.ticketing.enums.TicketStatus;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.AtmTicketEventRepository;
import com.hpy.ops360.ticketing.repository.BroadCategoryRepository;
import com.hpy.ops360.ticketing.repository.CheckAtmSourceRepository;
import com.hpy.ops360.ticketing.repository.LeaveRequestRepository;
import com.hpy.ops360.ticketing.repository.ManualTicketValidationRepository;
import com.hpy.ops360.ticketing.repository.TaskRepository;
import com.hpy.ops360.ticketing.repository.TicketTaskRepository;
import com.hpy.ops360.ticketing.repository.TickethistoryForCEHism;
import com.hpy.ops360.ticketing.request.NotificationSearch;
import com.hpy.ops360.ticketing.request.NotificationSearchResponse;
import com.hpy.ops360.ticketing.response.CreationDateOption;
import com.hpy.ops360.ticketing.response.FilterOptionsResponse;
import com.hpy.ops360.ticketing.response.NotificationPaginatedResponse;
import com.hpy.ops360.ticketing.response.OpenTicketsResponse;
import com.hpy.ops360.ticketing.response.SortOption;
import com.hpy.ops360.ticketing.ticket.dto.NotificationCountDTO;
import com.hpy.ops360.ticketing.utils.CustomDateFormattor;
import com.hpy.ops360.ticketing.utils.CustomDateFormattor.FormatStyle;
import com.hpy.ops360.ticketing.utils.DateUtil;
import com.hpy.ops360.ticketing.utils.TicketColorUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class TaskService extends GenericService<TaskDto, Task> {

	private final TaskRepository taskRepository;

	private TicketTaskRepository ticketTaskRepository;

	private LeaveRequestRepository leaveRequestRepository;

	@Autowired
	private TicketColorUtils ticketColorUtils;

	private final LoginService loginService;

	private final SynergyService synergyService;

	private final AtmTicketEventRepository atmTicketEventRepository;

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

	public GenericResponseDto createTask(TaskDto ticket, String token) {
		String ticketNumber = getGenereatedTicketNumber(TicketCategory.CE_MANUAL);
		ManualTicketValidation manualTicketValidationResp = manualTicketValidationRepository
				.validateManualTicket(ticket.getAtmid(), ticket.getReason());

		if (manualTicketValidationResp.getCode() == 0) {
			return new GenericResponseDto("Failed", manualTicketValidationResp.getMessage());
		}

		taskRepository.addTask(ticket.getAtmid(), ticket.getComment(), ticket.getCreatedBy(), "",
				ticket.getTaskDocumentDto().getDocument(), ticket.getTaskDocumentDto().getDocumentName(),
				ticket.getReason(), " ", TicketStatus.OPEN.toString(), ticketNumber, loginService.getLoggedInUser(),
				ticket.getTaskDocumentDto().getDocument1(), ticket.getTaskDocumentDto().getDocument1Name(),
				ticket.getTaskDocumentDto().getDocument2(), ticket.getTaskDocumentDto().getDocument2Name(),
				ticket.getTaskDocumentDto().getDocument3(), ticket.getTaskDocumentDto().getDocument3Name(),
				ticket.getTaskDocumentDto().getDocument4(), ticket.getTaskDocumentDto().getDocument4Name());

		return new GenericResponseDto("Success", "Ticket Created Successfully:" + ticketNumber);
	}

	protected String getGenereatedTicketNumber(TicketCategory ticketCategory) {
		// FIXME get max by TicketCategory wise
		Integer retrivedTicketNumber = taskRepository.findMaxTicketNumber();

		if (retrivedTicketNumber == null) {
			retrivedTicketNumber = 0;
		}

		log.info("retrivedTicketNumber:{} ", retrivedTicketNumber);

		return ticketCategory.name() + ++retrivedTicketNumber;
	}

	public List<AtmShortDetailsDto> getTicketList(List<AtmDetailsDto> atms, int maxLimit) {
		OpenTicketsResponse openTicketsResponse = synergyService.getOpenTicketDetails(atms);
		if (openTicketsResponse.getAtmdetails().isEmpty()) {
			return Collections.emptyList();
		}

		Set<String> atmTicketEventCodes = ConcurrentHashMap.newKeySet();
		Map<String, TicketDetailsDto> ticketDetailsMap = new ConcurrentHashMap<>();

		openTicketsResponse.getAtmdetails().parallelStream().forEach(atmDetail -> {

			atmDetail.getOpenticketdetails().stream().forEach(ticketDetailsDto -> {
				String uniqueCode = String.format("%s|%s|%s|%s|%s", ticketDetailsDto.getEquipmentid(),
						ticketDetailsDto.getSrno(), ticketDetailsDto.getEventcode(), ticketDetailsDto.getNextfollowup(),
						ticketDetailsDto.getCalldate());

				atmTicketEventCodes.add(uniqueCode);
				ticketDetailsMap.put(ticketDetailsDto.getEquipmentid() + ticketDetailsDto.getSrno(), ticketDetailsDto);
			});
		});

		String atmTicketEventCodeList = String.join(",", atmTicketEventCodes);

		List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
				.getAtmTicketEvent(loginService.getLoggedInUser(), atmTicketEventCodeList);
		log.info("event atmTicketEventCodeList ===================" + atmTicketEventCodeList);

		List<AtmShortDetailsDto> downCallList = atmTicketEventList.parallelStream().map(atmTicketEvent -> {
			TicketDetailsDto ticketDetailsDto = ticketDetailsMap
					.get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());

			AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
					.requestid(openTicketsResponse.getRequestid()).atmId(ticketDetailsDto.getEquipmentid())
					.ticketNumber(ticketDetailsDto.getSrno()).bank(ticketDetailsDto.getCustomer())
					.siteName(ticketDetailsDto.getEquipmentid())
					.owner(atmTicketEvent.getOwner().isEmpty() ? "" : atmTicketEvent.getOwner())
					.subcall(ticketDetailsDto.getSubcalltype()).vendor(ticketDetailsDto.getVendor())
					.error(atmTicketEvent.getEventCode() == null ? "Default" : atmTicketEvent.getEventCode())
					.downTime(getDownTimeInHrs(ticketDetailsDto.getCreateddate()))
					.priorityScore(atmTicketEvent.getPriorityScore()).eventGroup(atmTicketEvent.getEventGroup())
					.isBreakdown(atmTicketEvent.getIsBreakdown() == null ? 0 : atmTicketEvent.getIsBreakdown())
					.isUpdated(atmTicketEvent.getIsUpdated() == null ? 0 : atmTicketEvent.getIsUpdated())
					.isTimedOut(atmTicketEvent.getIsTimedOut() == null ? 0 : atmTicketEvent.getIsTimedOut())
					.isTravelling(atmTicketEvent.getIsTravelling() == null ? 0 : atmTicketEvent.getIsTravelling())
					.travelTime(atmTicketEvent.getTravelTime())
					.travelEta(atmTicketEvent.getTravelEta() == null ? 0 : atmTicketEvent.getTravelEta())
					.downCall(atmTicketEvent.getDownCall() == null ? 0 : atmTicketEvent.getDownCall())
					.etaDateTime(atmTicketEvent.getEtaDateTime() == null ? "" : atmTicketEvent.getEtaDateTime())
					.etaTimeout(atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout())
					.CreatedDate(CustomDateFormattor.convert(ticketDetailsDto.getCreateddate(), FormatStyle.DATABASE))
					.flagStatus(atmTicketEvent.getFlagStatus())
//					.CreatedDate(ticketDetailsDto.getCreateddate())
					.flagStatusInsertTime(atmTicketEvent.getFlagStatusInsertTime()).ceName(atmTicketEvent.getCeName())
					.build();

			// Apply color logic
			atmShortDetailsDto.setColor(ticketColorUtils.getColor(atmShortDetailsDto.getIsUpdated(),
					atmShortDetailsDto.getIsTimedOut(), atmShortDetailsDto.getIsTravelling()));

			return atmShortDetailsDto;
		}).sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()).toList();

		return downCallList.stream().filter(e -> e.getDownCall() == 1)
				.sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()
						.thenComparing(AtmShortDetailsDto::getDownTime).reversed())
				.toList();

	}

	private String getDownTimeInHrs(String createdDate) {
		log.info("Calculating downtime for created date------------------->: {}", createdDate);
		DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.US);
		LocalDateTime parsedCreatedDate = LocalDateTime.parse(createdDate, formatterInput);
		Duration duration = Duration.between(parsedCreatedDate, LocalDateTime.now());

		return String.format("%d Hrs", duration.toHours());
	}

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

//		String atmTicketEventCodeList = atmTicketEventCode.toString();
//		log.info("atmTicketEventCode:{}", atmTicketEventCodeList);
//		atmTicketEventCodeList = atmTicketEventCodeList.substring(0, atmTicketEventCodeList.length() - 1);
//
//		List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
//				.getAtmTicketEvent(loginService.getLoggedInUser(), atmTicketEventCodeList);
//
//		for (AtmTicketEvent atmTicketEvent : atmTicketEventList) {
//			TicketDetailsDto ticketDetailsDto = ticketDetailsMap
//					.get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());
//			String ticketStatus = ticketDetailsDto.getStatus().toString(); // Assuming Status is an Enum in
//																			// TicketDetailsDto
//
//			// if(!ticketDetailsDto.getCompletiondatewithtime().isEmpty()) {
//
//			log.info("********** Created date is ********** " + ticketDetailsDto.getCreateddate());
//			log.info("********** Completion date is ********** " + ticketDetailsDto.getCompletiondatewithtime());
//			AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
//					.atmId(ticketDetailsDto.getEquipmentid()).ticketNumber(ticketDetailsDto.getSrno())
//					.bank(ticketDetailsDto.getCustomer()).siteName(ticketDetailsDto.getEquipmentid()).owner(
//							ticketDetailsDto.getSubcalltype() != null
//									? getSelectedBroadCategory(ticketDetailsDto.getSubcalltype(),
//											ticketDetailsDto.getSrno(), ticketDetailsDto.getEquipmentid())
//									: "")
//					.subcall(ticketDetailsDto.getSubcalltype()).vendor(ticketDetailsDto.getVendor())
//					.error(ticketDetailsDto.getEventcode())
//					.downTime(getDownTimeInHrs1(ticketDetailsDto.getCreateddate(),
//							ticketDetailsDto.getCompletiondatewithtime() == null ? ""
//									: ticketDetailsDto.getCompletiondatewithtime()))
//					.priorityScore(atmTicketEvent.getPriorityScore()).eventGroup(atmTicketEvent.getEventGroup())
//					.isBreakdown(atmTicketEvent.getIsBreakdown() == null ? 0 : atmTicketEvent.getIsBreakdown())
//					.isUpdated(atmTicketEvent.getIsUpdated() == null ? 0 : atmTicketEvent.getIsUpdated())
//					.isTimedOut(atmTicketEvent.getIsTimedOut() == null ? 0 : atmTicketEvent.getIsTimedOut())
//					.isTravelling(atmTicketEvent.getIsTravelling() == null ? 0 : atmTicketEvent.getIsTravelling())
//					.travelTime(atmTicketEvent.getTravelTime()).travelEta(atmTicketEvent.getTravelEta())
//					.downCall(atmTicketEvent.getDownCall() == null ? 0 : atmTicketEvent.getDownCall())
//					.etaDateTime((atmTicketEvent.getFormattedEtaDateTime() == null ? ""
//							: atmTicketEvent.getFormattedEtaDateTime()))
////					.etaDateTime((atmTicketEvent.getEtaDateTime()== null ? ""
////							: atmTicketEvent.getEtaDateTime()))
//
//					.etaTimeout(atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout())
//					.CreatedDate(DateUtil.formatDateStringIndexOfATM(
//							ticketDetailsDto.getCreateddate() == null ? "" : ticketDetailsDto.getCreateddate()))
//					.closeDate(DateUtil
//							.formatDateStringIndexOfATM(ticketDetailsDto.getCompletiondatewithtime() == null ? ""
//									: ticketDetailsDto.getCompletiondatewithtime()))
//					.ceName(atmTicketEvent.getCeName())
//
////					.createdTime(extractTimeInAmPm(ticketDetailsDto.getCreateddate()))
////					.closedTime(extractTimeInAmPm(ticketDetailsDto.getCompletiondatewithtime()))
//					 .createdTime(extractTimeInAmPmRobust(
//						        ticketDetailsDto.getCreateddate() == null ? "" : ticketDetailsDto.getCreateddate()))
//			         .closedTime(extractTimeInAmPmRobust(
//						        ticketDetailsDto.getCompletiondatewithtime() == null ? "" : ticketDetailsDto.getCompletiondatewithtime()))
//						    
//
//					.createdTime(extractTimeInAmPmRobust(ticketDetailsDto.getCreateddate()))
//					.closedTime(extractTimeInAmPmRobust(ticketDetailsDto.getCompletiondatewithtime()))
//
//					.SynergyStatus(ticketStatus).remark(atmTicketEvent.getInternalRemark())
//					.hr(calculateHoursBetween(
//							DateUtil.formatDateStringIndexOfATM(
//									ticketDetailsDto.getCreateddate() == null ? "" : ticketDetailsDto.getCreateddate()),
//							DateUtil.formatDateStringIndexOfATM(
//									ticketDetailsDto.getCompletiondatewithtime() == null ? ""
//											: ticketDetailsDto.getCompletiondatewithtime())))
//					.build();
//			downCallList.add(atmShortDetailsDto);
//		}
//		// }
//
//		return downCallList.stream().sorted((a, b) -> {
//
//			if (a.getCloseDate().isEmpty()) {
//				return 0;
//			}
			log.info("Atm list with source response: {}", atm);
			int source = atm.get(0).getSourceCode();
			if (source == 0) // synergy
			{

				AtmHistoryNTicketsResponse nTicketsResponse = synergyService.getntickets(atms);
				if (nTicketsResponse.getTicketDetails().isEmpty()) {
					return Collections.emptyList();
				}

				for (TicketDetailsDto ticketDetailsDTO : nTicketsResponse.getTicketDetails()) {
					String uniqueCode = String.format("%s|%s|%s|%s|%s,", ticketDetailsDTO.getEquipmentid(),
							ticketDetailsDTO.getSrno(), ticketDetailsDTO.getEventcode(),
							ticketDetailsDTO.getNextfollowup(), ticketDetailsDTO.getCalldate());
					atmTicketEventCode.append(uniqueCode);
					ticketDetailsMap.put(ticketDetailsDTO.getEquipmentid() + ticketDetailsDTO.getSrno(),
							ticketDetailsDTO);

					String atmTicketEventCodeList = atmTicketEventCode.toString();
					log.info("atmTicketEventCode:{}", atmTicketEventCodeList);
					atmTicketEventCodeList = atmTicketEventCodeList.substring(0, atmTicketEventCodeList.length() - 1);

					List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
							.getAtmTicketEvent(loginService.getLoggedInUser(), atmTicketEventCodeList);

					for (AtmTicketEvent atmTicketEvent : atmTicketEventList) {
						TicketDetailsDto ticketDetailsDto = ticketDetailsMap
								.get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());
						String ticketStatus = ticketDetailsDto.getStatus().toString(); // Assuming Status is an Enum in
																						// TicketDetailsDto

						// if(!ticketDetailsDto.getCompletiondatewithtime().isEmpty()) {

						log.info("********** Created date is ********** " + ticketDetailsDto.getCreateddate());
						log.info("********** Completion date is ********** "
								+ ticketDetailsDto.getCompletiondatewithtime());
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
								.priorityScore(atmTicketEvent.getPriorityScore())
								.eventGroup(atmTicketEvent.getEventGroup())
								.isBreakdown(
										atmTicketEvent.getIsBreakdown() == null ? 0 : atmTicketEvent.getIsBreakdown())
								.isUpdated(atmTicketEvent.getIsUpdated() == null ? 0 : atmTicketEvent.getIsUpdated())
								.isTimedOut(atmTicketEvent.getIsTimedOut() == null ? 0 : atmTicketEvent.getIsTimedOut())
								.isTravelling(
										atmTicketEvent.getIsTravelling() == null ? 0 : atmTicketEvent.getIsTravelling())
								.travelTime(atmTicketEvent.getTravelTime()).travelEta(atmTicketEvent.getTravelEta())
								.downCall(atmTicketEvent.getDownCall() == null ? 0 : atmTicketEvent.getDownCall())
								.etaDateTime((atmTicketEvent.getFormattedEtaDateTime() == null ? ""
										: atmTicketEvent.getFormattedEtaDateTime()))
//    						.etaDateTime((atmTicketEvent.getEtaDateTime()== null ? ""
//    								: atmTicketEvent.getEtaDateTime()))

								.etaTimeout(
										atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout())
								.CreatedDate(DateUtil
										.formatDateStringIndexOfATM(ticketDetailsDto.getCreateddate() == null ? ""
												: ticketDetailsDto.getCreateddate()))
								.closeDate(DateUtil.formatDateStringIndexOfATM(
										ticketDetailsDto.getCompletiondatewithtime() == null ? ""
												: ticketDetailsDto.getCompletiondatewithtime()))
								.ceName(atmTicketEvent.getCeName())
								.createdTime(extractTimeInAmPmRobust(ticketDetailsDto.getCreateddate()))
								.closedTime(extractTimeInAmPmRobust(ticketDetailsDto.getCompletiondatewithtime()))
								.SynergyStatus(ticketStatus).remark(atmTicketEvent.getInternalRemark())
								.hr(calculateHoursBetween(
										DateUtil.formatDateStringIndexOfATM(
												ticketDetailsDto.getCreateddate() == null ? ""
														: ticketDetailsDto.getCreateddate()),
										DateUtil.formatDateStringIndexOfATM(
												ticketDetailsDto.getCompletiondatewithtime() == null ? ""
														: ticketDetailsDto.getCompletiondatewithtime())))
								.build();
						downCallList.add(atmShortDetailsDto);
					}
					// }

					return downCallList.stream().sorted((a, b) -> {

						if (a.getCloseDate().isEmpty()) {
							return 0;
						}

						if (b.getCloseDate().isEmpty()) {
							return 0;
						}

						DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a",
								Locale.US);
						LocalDateTime dt1 = LocalDateTime.parse(a.getCloseDate(), displayFormatter);
						LocalDateTime dt2 = LocalDateTime.parse(b.getCloseDate(), displayFormatter);

						return dt1.compareTo(dt2);

					}).toList();

				}

			} else {

				String atmid = atms.getAtmid();
				List<TicketdetailsEntityHims> openTicketsByEquipmentId = nTickethistoryForCEHism
						.findOpenTicketsByEquipmentId(atmid);
				System.out.println("openTicketsByEquipmentId :" + openTicketsByEquipmentId);

				for (TicketdetailsEntityHims ticketdetailsEntityHims : openTicketsByEquipmentId) {
					String uniqueCode = String.format("%s|%s|%s|%s|%s,", ticketdetailsEntityHims.getEquipmentid(),
							ticketdetailsEntityHims.getSrno(), ticketdetailsEntityHims.getEventcode(),
							ticketdetailsEntityHims.getNextfollowup(),
							DateUtil.formatToDateTimeShort(ticketdetailsEntityHims.getCalldate()));
					atmTicketEventCode.append(uniqueCode);
					ticketDetailsHimsMap.put(
							ticketdetailsEntityHims.getEquipmentid() + ticketdetailsEntityHims.getSrno(),
							ticketdetailsEntityHims);

					String atmTicketEventCodeList = atmTicketEventCode.toString();
					log.info("atmTicketEventCode:{}", atmTicketEventCodeList);
					atmTicketEventCodeList = atmTicketEventCodeList.substring(0, atmTicketEventCodeList.length() - 1);

					List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
							.getAtmTicketEvent(loginService.getLoggedInUser(), atmTicketEventCodeList);

					for (AtmTicketEvent atmTicketEvent : atmTicketEventList) {
						TicketdetailsEntityHims ticketDetailsDto = ticketDetailsHimsMap
								.get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());
						String ticketStatus = ticketDetailsDto.getStatus().toString(); // Assuming Status is an Enum in
																						// TicketDetailsDto

						// if(!ticketDetailsDto.getCompletiondatewithtime().isEmpty()) {

						log.info("********** Created date is ********** " + ticketDetailsDto.getCreateddate());
						log.info("********** Completion date is ********** "
								+ ticketDetailsDto.getCompletiondatewithtime());
						AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
								.atmId(ticketDetailsDto.getEquipmentid())
								.ticketNumber(ticketDetailsDto.getSrno() != null ? ticketDetailsDto.getSrno() : "")
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
								.priorityScore(atmTicketEvent.getPriorityScore())
								.eventGroup(atmTicketEvent.getEventGroup())
								.isBreakdown(
										atmTicketEvent.getIsBreakdown() == null ? 0 : atmTicketEvent.getIsBreakdown())
								.isUpdated(atmTicketEvent.getIsUpdated() == null ? 0 : atmTicketEvent.getIsUpdated())
								.isTimedOut(atmTicketEvent.getIsTimedOut() == null ? 0 : atmTicketEvent.getIsTimedOut())
								.isTravelling(
										atmTicketEvent.getIsTravelling() == null ? 0 : atmTicketEvent.getIsTravelling())
								.travelTime(atmTicketEvent.getTravelTime()).travelEta(atmTicketEvent.getTravelEta())
								.downCall(atmTicketEvent.getDownCall() == null ? 0 : atmTicketEvent.getDownCall())
								.etaDateTime((atmTicketEvent.getFormattedEtaDateTime() == null ? ""
										: atmTicketEvent.getFormattedEtaDateTime()))
//    	    						.etaDateTime((atmTicketEvent.getEtaDateTime()== null ? ""
//    	    								: atmTicketEvent.getEtaDateTime()))

								.etaTimeout(
										atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout())
								.CreatedDate(CustomDateFormattor.convert(ticketDetailsDto.getCreateddate(),
										FormatStyle.HIMS_DATABASE))

//										DateUtil
//										.formatDateStringIndexOfATM(ticketDetailsDto.getCreateddate() == null ? ""
//												: ticketDetailsDto.getCreateddate()))
//    	    						.CreatedDate(DateUtil.formatDateStringCeCreatedDate(ticketDetailsDto.getCreateddate() == null ? "" : ticketDetailsDto.getCreateddate()))
								.closeDate(CustomDateFormattor.convert(ticketDetailsDto.getCompletiondatewithtime(),
										FormatStyle.HIMS_DATABASE))
//										DateUtil.formatDateStringIndexOfATM(
//										ticketDetailsDto.getCompletiondatewithtime() == null ? ""
//												: ticketDetailsDto.getCompletiondatewithtime()))
								.CreatedDate(DateUtil.formatCustomDate(ticketDetailsDto.getCreateddate() == null ? ""
										: ticketDetailsDto.getCreateddate()))
//    	    						.CreatedDate(DateUtil.formatDateStringCeCreatedDate(ticketDetailsDto.getCreateddate() == null ? "" : ticketDetailsDto.getCreateddate()))
								.closeDate(DateUtil
										.formatCustomDate(ticketDetailsDto.getCompletiondatewithtime() == null ? ""
												: ticketDetailsDto.getCompletiondatewithtime()))
								.ceName(atmTicketEvent.getCeName())
								// .createdTime(extractTimeInAmPm(ticketDetailsDto.getCreateddate()))
								// .closedTime(extractTimeInAmPm(ticketDetailsDto.getCompletiondatewithtime()))
								.createdTime(extractTimeInAmPmRobust(ticketDetailsDto.getCreateddate() == null ? ""
										: ticketDetailsDto.getCreateddate()))
								.closedTime(extractTimeInAmPmRobust(
										ticketDetailsDto.getCompletiondatewithtime() == null ? ""
												: ticketDetailsDto.getCompletiondatewithtime()))
								.SynergyStatus(ticketStatus).remark(atmTicketEvent.getInternalRemark())
								.hr(calculateHoursBetween(
										CustomDateFormattor.convert(ticketDetailsDto.getCreateddate(),
												FormatStyle.HIMS_DATABASE),
										CustomDateFormattor.convert(ticketDetailsDto.getCompletiondatewithtime(),
												FormatStyle.HIMS_DATABASE)))

								.build();
//						DateUtil.formatDateStringIndexOfATM(
//						ticketDetailsDto.getCompletiondatewithtime() == null ? ""
//								: ticketDetailsDto.getCompletiondatewithtime())))
						downCallList.add(atmShortDetailsDto);
					}
//    	    			// }

					return downCallList.stream().sorted((a, b) -> {

						if (a.getCloseDate().isEmpty()) {
							return 0;
						}

						if (b.getCloseDate().isEmpty()) {
							return 0;
						}

						DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a",
								Locale.US);
						LocalDateTime dt1 = LocalDateTime.parse(a.getCloseDate(), displayFormatter);
						LocalDateTime dt2 = LocalDateTime.parse(b.getCloseDate(), displayFormatter);

						return dt1.compareTo(dt2);

					}).toList();

				}

			}
		} catch (Exception e) {
			log.error("Unexpected error in ", e);
			throw new RuntimeException("Internal processing error", e);
		}
		return null;

	}

	// Alternative: More robust version with multiple date format support
//		public String extractTimeInAmPmRobust(String dateTimeString) {
//		    if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
//		        return "";
//		    }
//		    
//		    // List of possible date formats from your data
//		    DateTimeFormatter[] formatters = {
//		        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
//		        DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm"),
//		        DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm"),
//		        DateTimeFormatter.ofPattern("dd/M/yyyy HH:mm"),
//		        DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"),
//		        DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm a")
//		    };
//		    
//		    for (DateTimeFormatter formatter : formatters) {
//		        try {
//		            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString.trim(), formatter);
//		            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
//		            return dateTime.format(timeFormatter);
//		        } catch (DateTimeParseException e) {
//		            // Try next formatter
//		            continue;
//		        }
//		    }
//		    
//		    log.warn("Unable to parse date with any known format: '{}'", dateTimeString);
//		    return "";
//		}

	public static final DateTimeFormatter dateGroupFormatter = DateTimeFormatter.ofPattern("dd MMM ''yy");

	public static String formatGroupDate(LocalDate date) {
		return date.format(dateGroupFormatter);
	}

//	public static String extractTimeInAmPm(String dateTime) {
//		try {
//			log.info("Inside Extract Time Method");
//			log.info("Request Recieved:- " + dateTime);
//			// Define the date-time formatter for the input
//			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//
//			// Define the formatter for the output in 12-hour AM/PM format
//			DateTimeFormatter amPmFormatter = DateTimeFormatter.ofPattern("hh:mm a");
//
//			// Parse the input string into a LocalDateTime object
//			LocalDateTime localDateTime = LocalDateTime.parse(dateTime, inputFormatter);
//
//			// Format the time part in AM/PM format and return it
//			return localDateTime.format(amPmFormatter);
//		} catch (DateTimeParseException e) {
//			throw new IllegalArgumentException("Invalid date-time format. Expected format is dd/MM/yyyy HH:mm.", e);
//		}
//	}

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
			return calculateFallbackDowntimeHims(createdDate);
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

	private String calculateFallbackDowntimeHims(String createdDate) {
		try {
			// Use downtimeinmins as fallback if available
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
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

//------------------------------------------------------------------------------------------------------------

	public List<AtmShortDetailsDto> getTicketListBydate(String atmid, String fromdate) throws Exception {
		AtmHistoryNTicketsResponse ticketsBydateResponse = synergyService.getTicketByDateHistory(atmid, fromdate);

		if (ticketsBydateResponse.getTicketDetails().isEmpty()) {
			return Collections.emptyList();
		}

		List<AtmShortDetailsDto> downCallList = new ArrayList<>();
		Map<String, TicketDetailsDto> ticketDetailsMap = new HashMap<>();
		StringBuilder atmTicketEventCode = new StringBuilder();

		for (TicketDetailsDto ticketDetailsDto : ticketsBydateResponse.getTicketDetails()) {
			String uniqueCode = String.format("%s|%s|%s|%s|%s,", ticketDetailsDto.getEquipmentid(),
					ticketDetailsDto.getSrno(), ticketDetailsDto.getEventcode(), ticketDetailsDto.getNextfollowup(),
					ticketDetailsDto.getCalldate());
			atmTicketEventCode.append(uniqueCode);
			ticketDetailsMap.put(ticketDetailsDto.getEquipmentid() + ticketDetailsDto.getSrno(), ticketDetailsDto);

		}

		String atmTicketEventCodeList = atmTicketEventCode.toString();
		log.info("atmTicketEventCode:{}", atmTicketEventCodeList);
		atmTicketEventCodeList = atmTicketEventCodeList.substring(0, atmTicketEventCodeList.length() - 1);

		List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
				.getAtmTicketEvent(loginService.getLoggedInUser(), atmTicketEventCodeList);
		for (AtmTicketEvent atmTicketEvent : atmTicketEventList) {
			TicketDetailsDto ticketDetailsDto = ticketDetailsMap
					.get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());
			String ticketStatus = ticketDetailsDto.getStatus().toString(); // Assuming Status is an Enum in
																			// TicketDetailsDto

			AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
					.atmId(ticketDetailsDto.getEquipmentid()).ticketNumber(ticketDetailsDto.getSrno())
					.bank(ticketDetailsDto.getCustomer()).siteName(ticketDetailsDto.getEquipmentid()).owner(
							ticketDetailsDto.getSubcalltype() != null
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
					.isTravelling(atmTicketEvent.getIsTravelling() == null ? 0 : atmTicketEvent.getIsTravelling())
					.travelTime(atmTicketEvent.getTravelTime()).travelEta(atmTicketEvent.getTravelEta())
					.downCall(atmTicketEvent.getDownCall() == null ? 0 : atmTicketEvent.getDownCall())
					.etaDateTime((atmTicketEvent.getFormattedEtaDateTime() == null ? ""
							: atmTicketEvent.getFormattedEtaDateTime()))

					.etaTimeout(atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout())
					.CreatedDate(DateUtil.formatDateString(
							ticketDetailsDto.getCreateddate() == null ? "" : ticketDetailsDto.getCreateddate()))
					.closeDate(DateUtil.formatDateString(ticketDetailsDto.getCompletiondatewithtime() == null ? ""
							: ticketDetailsDto.getCompletiondatewithtime()))
					.SynergyStatus(ticketDetailsDto.getStatus()).build();
			downCallList.add(atmShortDetailsDto);
		}

		return downCallList.stream().sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed())
				.toList();
	}

	public OpenTicketsResponse getOpenTicketDetailsInBatches(List<AtmDetailsDto> atms, int batchSize) {
		List<OpenTicketsResponse> allResponses = new ArrayList<>();

		log.info("batchSize:{}", batchSize);
		// Calculate the number of batches needed
		int numBatches = (atms.size() + batchSize - 1) / batchSize;
		log.info("numBatches:{}", numBatches);

		// Create an ExecutorService with a fixed thread pool size
		ExecutorService executor = Executors.newFixedThreadPool(10);

		try {
			List<Future<OpenTicketsResponse>> futures = new ArrayList<>();

			for (int i = 0; i < numBatches; i++) {
				int startIdx = i * batchSize;
				int endIdx = Math.min((i + 1) * batchSize, atms.size());

				List<AtmDetailsDto> atmBatch = atms.subList(startIdx, endIdx);
				Callable<OpenTicketsResponse> task = () -> synergyService.getOpenTicketDetails(atmBatch);
				futures.add(executor.submit(task));
			}

			for (Future<OpenTicketsResponse> future : futures) {
				allResponses.add(future.get()); // Blocks until the result is available
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			executor.shutdown(); // Shutdown the executor
		}

		OpenTicketsResponse consolidatedResponse = new OpenTicketsResponse();
		consolidatedResponse.setRequestid("Consolidated");

		List<AtmDetailsWithTickets> consolidatedAtmDetails = allResponses.stream()
				.flatMap(response -> response.getAtmdetails().stream()).toList();

		consolidatedResponse.setAtmdetails(consolidatedAtmDetails);
		return consolidatedResponse;

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

	public NotificationCountDTO getNotificationCount(String type) {
		String managerUsername = loginService.getLoggedInUser();

		long ticketCount = 0;
		long leaveCount = 0;

		NotificationCountDTO notificationCountDTO = new NotificationCountDTO();
		ticketCount = ticketTaskRepository.countManagerTeamTasks(managerUsername);
		notificationCountDTO.setTicketCount(ticketCount);
		leaveCount = leaveRequestRepository.countManagerLeaveRequests(managerUsername);
		notificationCountDTO.setLeaveCount(leaveCount);

		notificationCountDTO.setTotalCount(ticketCount + leaveCount);

		return notificationCountDTO;
	}

	public NotificationPaginatedResponse getManagerCombinedTeamData(int page, int size, String dataType) {
		log.info("Entering getManagerCombinedTeamData with page: {}, size: {}, dataType: {}", page, size, dataType);

		String managerUsername = loginService.getLoggedInUser();
		log.debug("Retrieved logged-in user: {}", managerUsername);

		List<Object> combinedData = new ArrayList<>();
		long totalElements = 0;

		String filter = (dataType != null) ? dataType.toUpperCase() : null;
		log.debug("Filter type: {}", filter);
		int pageNumber = page + 1;

		// Fetch and filter ticket tasks
		if ("TICKET".equals(filter) || filter == null) {
			log.debug("Fetching ticket tasks for manager: {}, page: {}, size: {}", managerUsername, pageNumber, size);
			List<TicketTask> tasks = ticketTaskRepository.getManagerTeamTasksWithPagination(managerUsername, size,
					pageNumber);
			if (!tasks.isEmpty()) {
				totalElements = tasks.get(0).getTotalTask();
				log.info("Retrieved {} ticket tasks, total elements: {}", tasks.size(), totalElements);
			} else {
				log.warn("No ticket tasks found for manager: {}", managerUsername);
			}

			List<TicketTaskDTO> taskDTOs = tasks.stream().map(this::toTicketTaskDTO).collect(Collectors.toList());
			log.debug("Converted {} ticket tasks to DTOs", taskDTOs.size());

			combinedData.addAll(taskDTOs);
		}

		if ("LEAVE".equals(filter) || filter == null) {
			log.debug("Fetching leave requests for manager: {}, page: {}, size: {}", managerUsername, pageNumber, size);
			List<LeaveRequest> leaveRequests = leaveRequestRepository.sp_GetManagerLeaveRequests(managerUsername, size,
					pageNumber);
			if (filter == null && !leaveRequests.isEmpty()) {
				totalElements += leaveRequests.get(0).getTotalLeaveRequests();
				log.info("Retrieved {} leave requests, updated total elements: {}", leaveRequests.size(),
						totalElements);
			} else if ("LEAVE".equals(filter) && !leaveRequests.isEmpty()) {
				totalElements = leaveRequests.get(0).getTotalLeaveRequests();
				log.info("Retrieved {} leave requests, total elements: {}", leaveRequests.size(), totalElements);
			} else {
				log.warn("No leave requests found for manager: {}", managerUsername);
			}

			List<LeaveRequestDTO> leaveRequestDTOs = leaveRequests.stream().map(this::toLeaveRequestDTO)
					.filter(dto -> dto != null).collect(Collectors.toList());
			log.debug("Converted {} leave requests to DTOs", leaveRequestDTOs.size());

			combinedData.addAll(leaveRequestDTOs);
		}

		int totalFilteredElements = combinedData.size();
		log.info("Total filtered elements: {}", totalFilteredElements);

		int start = (int) PageRequest.of(page, size).getOffset();
		int end = Math.min((start + size), totalFilteredElements);
		List<Object> pageContent;
		try {
			pageContent = combinedData.subList(start, end);
			log.debug("Page content extracted with size: {}", pageContent.size());
		} catch (IndexOutOfBoundsException e) {
			log.error("Error extracting page content: start={}, end={}, totalFilteredElements={}", start, end,
					totalFilteredElements, e);
			pageContent = new ArrayList<>();
		}

		// Create PaginationMetadataDTO
		NotificationPaginatedResponse.PaginationMetadataDTO paginationMetadata = new NotificationPaginatedResponse.PaginationMetadataDTO();
		paginationMetadata.setCurrentPage(page);
		paginationMetadata.setPageSize(size);
		paginationMetadata.setTotalRecords(totalFilteredElements);
		paginationMetadata.setTotalPages((int) Math.ceil((double) totalFilteredElements / size));

		// Create NotificationPaginatedResponse
		NotificationPaginatedResponse response = new NotificationPaginatedResponse();
		response.setData(pageContent);
		response.setPagination(paginationMetadata);

		log.info("Exiting getManagerCombinedTeamData with page content size: {}, total elements: {}",
				pageContent.size(), totalFilteredElements);

		return response;
	}

//
	public NotificationSearchResponse getManagerCombinedTeamDataWithSearch(NotificationSearch notificationSearch) {

		String searchParam = notificationSearch.getSearchParam();
		String dataType = (notificationSearch.getNotificationType() != null)
				? notificationSearch.getNotificationType().toString()
				: null;
		String filter = (dataType != null) ? dataType.toUpperCase() : null;

		String managerUsername = loginService.getLoggedInUser();
		log.debug("Retrieved logged-in user: {}", managerUsername);

		NotificationSearchResponse combinedData = new NotificationSearchResponse();
		log.debug("Filter type: {}", filter);

		String searchTerm = (searchParam != null) ? searchParam.toLowerCase().trim() : null;

		if ("TICKET".equals(filter) || filter == null) {
			log.debug("Fetching ticket tasks for manager: {}", managerUsername);
			List<TicketTask> tasks = ticketTaskRepository.getManagerTeamTasks(managerUsername);
			List<TicketTask> filterAndSortTicketTasks = filterAndSortTicketTasks(tasks, notificationSearch);

			List<String> filteredAtmCodes = filterAndSortTicketTasks.stream().map(TicketTask::getAtmCode).filter(
					atmCode -> atmCode != null && (searchTerm == null || atmCode.toLowerCase().contains(searchTerm)))
					.distinct().collect(Collectors.toList());

			log.info("Retrieved and filtered {} unique atm codes for tickets", filteredAtmCodes.size());
			combinedData.getResponse().addAll(filteredAtmCodes);
		}

		if ("LEAVE".equals(filter) || filter == null) {
			log.debug("Fetching leave requests for manager: {}", managerUsername);
			List<LeaveRequest> leaveRequests = leaveRequestRepository
					.sp_GetManagerLeaveRequestsWithoutPagination(managerUsername);
			List<LeaveRequest> filterAndSortLeaveRequests = filterAndSortLeaveRequests(leaveRequests,
					notificationSearch);
			List<String> filteredRemarks = filterAndSortLeaveRequests.stream().map(LeaveRequest::getRemarks).filter(
					remarks -> remarks != null && (searchTerm == null || remarks.toLowerCase().contains(searchTerm)))
					.distinct().collect(Collectors.toList());

			log.info("Retrieved and filtered {} unique remarks for leaves", filteredRemarks.size());
			combinedData.getResponse().addAll(filteredRemarks);
		}

		log.info("Exiting getManagerCombinedTeamDataWithSearch with combined data size: {}",
				combinedData.getResponse().size());
		return combinedData;
	}

	TicketTaskDTO toTicketTaskDTO(TicketTask ticketTask) {
		TicketTaskDTO ticketTaskDTO = new TicketTaskDTO();
		ticketTaskDTO.setComment(ticketTask.getComment());
		ticketTaskDTO.setCreatedBy(ticketTask.getCreatedBy());
		ticketTaskDTO.setCreatedDate(ticketTask.getCreatedDate());
		ticketTaskDTO.setReason(ticketTask.getReason());
		ticketTaskDTO.setStatus(ticketTask.getStatus());
		ticketTaskDTO.setCheckerName(ticketTask.getCheckerName());
		ticketTaskDTO.setCheckerComment(ticketTask.getCheckerComment());
		ticketTaskDTO.setAtmCode(ticketTask.getAtmCode());
		ticketTaskDTO.setAddress(ticketTask.getAddress());
		ticketTaskDTO.setBankName(ticketTask.getBankName());

		return ticketTaskDTO;
	}

	public LeaveRequestDTO toLeaveRequestDTO(LeaveRequest leaveRequest) {
		if (leaveRequest == null) {
			return null;
		}

		LeaveRequestDTO leaveRequestDTO = new LeaveRequestDTO();
		leaveRequestDTO.setUserId(leaveRequest.getUserId());
		leaveRequestDTO.setRequestDate(leaveRequest.getRequestDate());
		leaveRequestDTO.setLeaveTypeId(leaveRequest.getLeaveTypeId());
		leaveRequestDTO.setReasonId(leaveRequest.getReasonId());
		leaveRequestDTO.setAbsenceSlotId(leaveRequest.getAbsenceSlotId());
		leaveRequestDTO.setCustomStartTime(leaveRequest.getCustomStartTime());
		leaveRequestDTO.setCustomEndTime(leaveRequest.getCustomEndTime());
		leaveRequestDTO.setRemarks(leaveRequest.getRemarks());
		leaveRequestDTO.setStatus(leaveRequest.getStatus());
		leaveRequestDTO.setCreatedAt(leaveRequest.getCreatedAt());

		return leaveRequestDTO;
	}

	public List<TicketTask> filterAndSortTicketTasks(List<TicketTask> tasks, NotificationSearch notificationSearch) {

		String searchTerm = (notificationSearch.getSearchParam() != null)
				? notificationSearch.getSearchParam().toLowerCase().trim()
				: null;

		List<TicketTask> filteredTasks = tasks.stream()
				.filter(task -> searchTerm == null
						|| (task.getAtmCode() != null && task.getAtmCode().toLowerCase().contains(searchTerm)))
				.collect(Collectors.toList());

		Integer creationDateId = notificationSearch.getCreationDateId();
		if (creationDateId != null) {
			LocalDate filterDate;
			if (creationDateId == 6) {
				filterDate = getFilterDate(creationDateId, notificationSearch.getFromDate(),
						notificationSearch.getToDate());
			} else {
				filterDate = getFilterDate(creationDateId);
			}

			if (filterDate != null) {
				filteredTasks = filteredTasks.stream().filter(task -> filterByCreationDateDirect(task, filterDate))
						.collect(Collectors.toList());
			}
		}

		Long sortOption = notificationSearch.getSortOption();
		if (sortOption != null) {
			Comparator<TicketTask> comparator = (t1, t2) -> compareByCreationDateDirect(t1, t2);
			if (sortOption == 1) { // Newest to oldest
				filteredTasks.sort(comparator.reversed());
			} else if (sortOption == 2) { // Oldest to newest
				filteredTasks.sort(comparator);
			}
		}

		return filteredTasks;
	}

	public List<LeaveRequest> filterAndSortLeaveRequests(List<LeaveRequest> leaveRequests,
			NotificationSearch notificationSearch) {

		String searchTerm = (notificationSearch.getSearchParam() != null)
				? notificationSearch.getSearchParam().toLowerCase().trim()
				: null;

		List<LeaveRequest> filteredLeaves = leaveRequests.stream()
				.filter(leave -> searchTerm == null
						|| (leave.getRemarks() != null && leave.getRemarks().toLowerCase().contains(searchTerm)))
				.collect(Collectors.toList());

		Integer creationDateId = notificationSearch.getCreationDateId();
		if (creationDateId != null) {
			LocalDate filterDate;
			if (creationDateId == 6) {
				filterDate = getFilterDate(creationDateId, notificationSearch.getFromDate(),
						notificationSearch.getToDate());
			} else {
				filterDate = getFilterDate(creationDateId);
			}

			if (filterDate != null) {
				filteredLeaves = filteredLeaves.stream().filter(leave -> filterByCreationDateDirect(leave, filterDate))
						.collect(Collectors.toList());
			}
		}

		Long sortOption = notificationSearch.getSortOption();
		if (sortOption != null) {
			Comparator<LeaveRequest> comparator = (l1, l2) -> compareByCreationDateDirect(l1, l2);
			if (sortOption == 1) { // Newest to oldest
				filteredLeaves.sort(comparator.reversed());
			} else if (sortOption == 2) { // Oldest to newest
				filteredLeaves.sort(comparator);
			}
		}

		return filteredLeaves;
	}

	public List<Object> applyFiltersAndSort(List<Object> combinedData, Long sortOption, Integer creationDateId,
			LocalDate fromDate, LocalDate toDate) {
		log.info("Entering applyFiltersAndSort with combinedData size: {}, sortOption: {}, creationDateId: {}",
				combinedData != null ? combinedData.size() : 0, sortOption, creationDateId);

		if (combinedData == null || combinedData.isEmpty()) {
			log.warn("Combined data is null or empty, returning empty list");
			return new ArrayList<>();
		}

		List<Object> filteredData = new ArrayList<>(combinedData);
		LocalDate filterDate;
		if (creationDateId != null) {
			log.debug("Applying creation date filter with creationDateId: {}", creationDateId);
			if (creationDateId == 6) {
				filterDate = getFilterDate(creationDateId, fromDate, toDate);

			} else {
				filterDate = getFilterDate(creationDateId);
			}
			if (filterDate != null) {
				filteredData = filteredData.stream().filter(item -> filterByCreationDate(item, filterDate))
						.collect(Collectors.toList());
				log.info("After applying creation date filter, filteredData size: {}", filteredData.size());
			} else {
				log.warn("No filter date returned for creationDateId: {}", creationDateId);
			}
		} else {
			log.debug("No creation date filter provided, skipping date filtering");
		}

		if (sortOption != null) {
			log.debug("Applying sorting with sortOption: {}", sortOption);
			Comparator<Object> comparator = (o1, o2) -> compareByCreationDate(o1, o2);

			if (sortOption == 1) {
				log.debug("Sorting newest to oldest");
				filteredData.sort(comparator.reversed());
			} else if (sortOption == 2) {
				log.debug("Sorting oldest to newest");
				filteredData.sort(comparator);
			} else {
				log.warn("Unknown sortOption: {}, skipping sorting", sortOption);
			}
		} else {
			log.debug("No sort option provided, skipping sorting");
		}

		log.info("Exiting applyFiltersAndSort with filteredData size: {}", filteredData.size());
		return filteredData;
	}

	private boolean filterByCreationDate(Object item, LocalDate filterDate) {
		LocalDate createdDate = null;
		if (item instanceof TicketTaskDTO ticketTaskDTO) {
			createdDate = ticketTaskDTO.getCreatedDate() != null ? ticketTaskDTO.getCreatedDate().toLocalDate() : null;
		} else if (item instanceof LeaveRequestDTO leaveRequestDTO) {
			createdDate = leaveRequestDTO.getCreatedAt() != null ? leaveRequestDTO.getCreatedAt().toLocalDate() : null;
		} else {
			log.warn("Unknown item type in combinedData: {}", item != null ? item.getClass().getName() : "null");
			return false;
		}

		boolean matches = createdDate != null && (createdDate.isAfter(filterDate) || createdDate.isEqual(filterDate));
		log.trace("Filtering item: createdDate={}, matches={}", createdDate, matches);
		return matches;
	}

	private boolean filterByCreationDateDirect(Object item, LocalDate filterDate) {
		LocalDate createdDate = null;
		if (item instanceof TicketTask ticketTaskDTO) {
			createdDate = ticketTaskDTO.getCreatedDate() != null ? ticketTaskDTO.getCreatedDate().toLocalDate() : null;
		} else if (item instanceof LeaveRequest leaveRequestDTO) {
			createdDate = leaveRequestDTO.getCreatedAt() != null ? leaveRequestDTO.getCreatedAt().toLocalDate() : null;
		} else {
			log.warn("Unknown item type in combinedData: {}", item != null ? item.getClass().getName() : "null");
			return false;
		}

		boolean matches = createdDate != null && (createdDate.isAfter(filterDate) || createdDate.isEqual(filterDate));
		log.trace("Filtering item: createdDate={}, matches={}", createdDate, matches);
		return matches;
	}

	private int compareByCreationDate(Object o1, Object o2) {
		LocalDate date1 = null;
		LocalDate date2 = null;

		if (o1 instanceof TicketTaskDTO ticketTaskDTO) {
			date1 = ticketTaskDTO.getCreatedDate() != null ? ticketTaskDTO.getCreatedDate().toLocalDate() : null;
		} else if (o1 instanceof LeaveRequestDTO leaveRequestDTO) {
			date1 = leaveRequestDTO.getCreatedAt() != null ? leaveRequestDTO.getCreatedAt().toLocalDate() : null;
		} else {
			log.warn("Unknown type for o1 in sorting: {}", o1 != null ? o1.getClass().getName() : "null");
		}

		if (o2 instanceof TicketTaskDTO ticketTaskDTO) {
			date2 = ticketTaskDTO.getCreatedDate() != null ? ticketTaskDTO.getCreatedDate().toLocalDate() : null;
		} else if (o2 instanceof LeaveRequestDTO leaveRequestDTO) {
			date2 = leaveRequestDTO.getCreatedAt() != null ? leaveRequestDTO.getCreatedAt().toLocalDate() : null;
		} else {
			log.warn("Unknown type for o2 in sorting: {}", o2 != null ? o2.getClass().getName() : "null");
		}

		if (date1 != null && date2 != null) {
			log.trace("Comparing dates: date1={}, date2={}", date1, date2);
			return date1.compareTo(date2);
		}
		log.debug("One or both dates are null during sorting: date1={}, date2={}", date1, date2);
		return 0;
	}

	private int compareByCreationDateDirect(Object o1, Object o2) {
		LocalDate date1 = null;
		LocalDate date2 = null;

		if (o1 instanceof TicketTask ticketTaskDTO) {
			date1 = ticketTaskDTO.getCreatedDate() != null ? ticketTaskDTO.getCreatedDate().toLocalDate() : null;
		} else if (o1 instanceof LeaveRequest leaveRequestDTO) {
			date1 = leaveRequestDTO.getCreatedAt() != null ? leaveRequestDTO.getCreatedAt().toLocalDate() : null;
		} else {
			log.warn("Unknown type for o1 in sorting: {}", o1 != null ? o1.getClass().getName() : "null");
		}

		if (o2 instanceof TicketTask ticketTaskDTO) {
			date2 = ticketTaskDTO.getCreatedDate() != null ? ticketTaskDTO.getCreatedDate().toLocalDate() : null;
		} else if (o2 instanceof LeaveRequest leaveRequestDTO) {
			date2 = leaveRequestDTO.getCreatedAt() != null ? leaveRequestDTO.getCreatedAt().toLocalDate() : null;
		} else {
			log.warn("Unknown type for o2 in sorting: {}", o2 != null ? o2.getClass().getName() : "null");
		}

		if (date1 != null && date2 != null) {
			log.trace("Comparing dates: date1={}, date2={}", date1, date2);
			return date1.compareTo(date2);
		}
		log.debug("One or both dates are null during sorting: date1={}, date2={}", date1, date2);
		return 0;
	}

	public NotificationPaginatedResponse getManagerCombinedTeamData(
			NotificationFilterRequest notificationFilterRequest) {
		int page = notificationFilterRequest != null ? notificationFilterRequest.getPageNumber() : 1;
		int size = notificationFilterRequest != null ? notificationFilterRequest.getPageSize() : 10;
		String searchBy = notificationFilterRequest != null ? notificationFilterRequest.getSearchBy() : null;
		log.info(
				"Entering getManagerCombinedTeamData with page: {}, size: {}, searchBy: {}, notificationFilterRequest: {}",
				page, size, searchBy, notificationFilterRequest);

		String managerUsername = loginService.getLoggedInUser();
		if (managerUsername == null || managerUsername.isEmpty()) {
			log.error("No logged-in manager username found");
			throw new IllegalStateException("Manager username not found");
		}

		List<Object> combinedData = new ArrayList<>();
		long totalElements = 0;

		String filter = (notificationFilterRequest != null && notificationFilterRequest.getType() != null)
				? notificationFilterRequest.getType().toString().toUpperCase()
				: null;
		log.debug("Filter type: {}", filter);

		if (filter != null && !filter.equals("TICKET") && !filter.equals("LEAVE")) {
			log.error("Invalid filter type: {}", filter);
			throw new IllegalArgumentException("Invalid filter type: " + filter);
		}

		long ticketTotal = 0;
		if ("TICKET".equals(filter) || filter == null) {
			log.debug("Fetching ticket tasks for manager: {}, searchBy: {}", managerUsername, searchBy);
			try {
				List<TicketTask> tasks = ticketTaskRepository.getManagerTeamTasksWithPagination(managerUsername,
						Integer.MAX_VALUE, 1);
				if (searchBy != null && !searchBy.trim().isEmpty()) {
					tasks = tasks.stream()
							.filter(task -> task.getAtmCode() != null
									&& task.getAtmCode().toLowerCase().contains(searchBy.toLowerCase()))
							.collect(Collectors.toList());
				}
				ticketTotal = tasks.size();
				combinedData.addAll(tasks.stream().map(this::toTicketTaskDTO).collect(Collectors.toList()));
				totalElements += ticketTotal;
				log.info("Retrieved {} ticket tasks after search, total ticket elements: {}", tasks.size(),
						ticketTotal);
			} catch (Exception e) {
				log.error("Error fetching ticket tasks for manager: {}", managerUsername, e);
				throw new RuntimeException("Failed to fetch ticket tasks", e);
			}
		}

		long leaveTotal = 0;
		if ("LEAVE".equals(filter) || filter == null) {
			log.debug("Fetching leave requests for manager: {}, searchBy: {}", managerUsername, searchBy);
			try {
				List<LeaveRequest> leaveRequests = leaveRequestRepository.sp_GetManagerLeaveRequests(managerUsername,
						Integer.MAX_VALUE, 1);
				if (searchBy != null && !searchBy.trim().isEmpty()) {
					leaveRequests = leaveRequests.stream()
							.filter(leave -> leave.getRemarks() != null
									&& leave.getRemarks().toLowerCase().contains(searchBy.toLowerCase()))
							.collect(Collectors.toList());
				}
				leaveTotal = leaveRequests.size();
				combinedData.addAll(leaveRequests.stream().map(this::toLeaveRequestDTO).collect(Collectors.toList()));
				totalElements += leaveTotal;
				log.info("Retrieved {} leave requests after search, total leave elements: {}", leaveRequests.size(),
						leaveTotal);
			} catch (Exception e) {
				log.error("Error fetching leave requests for manager: {}", managerUsername, e);
				throw new RuntimeException("Failed to fetch leave requests", e);
			}
		}

		log.debug("Applying filters and sorting to combined data of size: {}", combinedData.size());
		combinedData = applyFiltersAndSort(combinedData,
				notificationFilterRequest != null ? notificationFilterRequest.getSortOption() : null,
				notificationFilterRequest != null ? notificationFilterRequest.getCreationDateId() : null,
				notificationFilterRequest != null ? notificationFilterRequest.getFromDate() : null,
				notificationFilterRequest != null ? notificationFilterRequest.getToDate() : null);

		int filteredSize = combinedData.size();
		int offset = (page - 1) * size;
		int start = Math.min(offset, filteredSize);
		int end = Math.min(start + size, filteredSize);
		List<Object> pageContent = (start < filteredSize) ? combinedData.subList(start, end) : new ArrayList<>();
		log.debug("Page content extracted with size: {}, offset: {}", pageContent.size(), offset);

		NotificationPaginatedResponse.PaginationMetadataDTO paginationMetadata = new NotificationPaginatedResponse.PaginationMetadataDTO();
		paginationMetadata.setCurrentPage(page);
		paginationMetadata.setPageSize(size);
		paginationMetadata.setTotalRecords((int) totalElements);
		paginationMetadata.setTotalFilteredSize(filteredSize);
		paginationMetadata.setTotalPages((int) Math.ceil((double) filteredSize / size));

		NotificationPaginatedResponse response = new NotificationPaginatedResponse();
		response.setData(pageContent);
		response.setPagination(paginationMetadata);

		log.info("Exiting getManagerCombinedTeamData with page content size: {}, total elements: {}, filtered size: {}",
				pageContent.size(), totalElements, filteredSize);

		return response;
	}

	private LocalDate getFilterDate(int creationDateId, LocalDate fromDate, LocalDate toDate) {
		log.debug("Entering getFilterDate with creationDateId: {}", creationDateId);
		LocalDate today = LocalDate.now();
		LocalDate filterDate;

		long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate);
		log.info("daysBetween  : " + daysBetween);

		filterDate = today.minusDays(daysBetween);
		log.debug("Filter date set to range days ago: {}", filterDate);

		return filterDate;
	}

	private LocalDate getFilterDate(int creationDateId) {
		log.debug("Entering getFilterDate with creationDateId: {}", creationDateId);
		LocalDate today = LocalDate.now();
		LocalDate filterDate;

		switch (creationDateId) {
		case 1 -> {
			filterDate = today;
			log.debug("Filter date set to today: {}", filterDate);
		}
		case 2 -> {
			filterDate = today.minusWeeks(1);
			log.debug("Filter date set to one week ago: {}", filterDate);
		}
		case 3 -> {
			filterDate = today.minusMonths(1);
			log.debug("Filter date set to one month ago: {}", filterDate);
		}
		case 4 -> {
			filterDate = today.minusDays(30);
			log.debug("Filter date set to 30 days ago: {}", filterDate);
		}
		case 5 -> {
			filterDate = today.minusDays(90);
			log.debug("Filter date set to 90 days ago: {}", filterDate);
		}
		default -> {
			filterDate = null;
			log.warn("No filter date set for creationDateId: {}", creationDateId);
		}
		}

		log.debug("Exiting getFilterDate with filterDate: {}", filterDate);
		return filterDate;
	}

	public FilterOptionsResponse getFilterOptionsResponse() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime todayStart = now.truncatedTo(ChronoUnit.DAYS);
		LocalDateTime thisWeekStart = todayStart.minusDays(now.getDayOfWeek().getValue() - 1);
		LocalDateTime thisMonthStart = now.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
		LocalDateTime last30DaysStart = now.minusDays(30);
		LocalDateTime last90DaysStart = now.minusDays(90);

		long todayCount = taskRepository.countCreatedToday(todayStart);
		long thisWeekCount = taskRepository.countByCreatedDateBetween(thisWeekStart, now);
		long thisMonthCount = taskRepository.countByCreatedDateBetween(thisMonthStart, now);
		long last30DaysCount = taskRepository.countByCreatedDateBetween(last30DaysStart, now);
		long last90DaysCount = taskRepository.countByCreatedDateBetween(last90DaysStart, now);

		FilterOptionsResponse response = new FilterOptionsResponse();

		List<SortOption> sortOptions = new ArrayList<>();
		sortOptions.add(new SortOption(1, "Date (Newest to Oldest)"));
		sortOptions.add(new SortOption(2, "Date (Oldest to Newest)"));
		response.setSortOptions(sortOptions);

		List<CreationDateOption> creationDateOptions = new ArrayList<>();
		creationDateOptions.add(new CreationDateOption(1, "Today", todayCount));
		creationDateOptions.add(new CreationDateOption(2, "This Week", thisWeekCount));
		creationDateOptions.add(new CreationDateOption(3, "This Month", thisMonthCount));
		creationDateOptions.add(new CreationDateOption(4, "Last 30 days", last30DaysCount));
		creationDateOptions.add(new CreationDateOption(5, "Last 90 days", last90DaysCount));
		creationDateOptions.add(new CreationDateOption(6, "Date Range", 0L));

		response.setCreationDateOptions(creationDateOptions);

		return response;
	}

}