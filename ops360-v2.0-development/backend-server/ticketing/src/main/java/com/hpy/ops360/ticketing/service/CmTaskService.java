package com.hpy.ops360.ticketing.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import com.hpy.ops360.ticketing.cm.dto.ApproveRejectStatusDto;
import com.hpy.ops360.ticketing.cm.dto.AtmMtdUptimeDto;
import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;
import com.hpy.ops360.ticketing.cm.dto.CeMachineUpDownCountDto;
import com.hpy.ops360.ticketing.cm.dto.CmTasksWithDocumentsDto;
import com.hpy.ops360.ticketing.cm.dto.FileDownloadDto;
import com.hpy.ops360.ticketing.cm.dto.OpenTicketsWithCategoryDto;
import com.hpy.ops360.ticketing.cm.dto.OpenTicketsWithCategoryDto_forAllTickets;
import com.hpy.ops360.ticketing.cm.dto.RejectionReasonDto;
import com.hpy.ops360.ticketing.cm.dto.TicketListCountDTO;
import com.hpy.ops360.ticketing.cm.entity.AtmDetails;
import com.hpy.ops360.ticketing.cm.entity.CeMachineUpDownCount;
import com.hpy.ops360.ticketing.cm.entity.TicketCount;
import com.hpy.ops360.ticketing.config.DisableSslClass;
import com.hpy.ops360.ticketing.dto.AtmDetailsDto;
import com.hpy.ops360.ticketing.dto.CategoryData;
import com.hpy.ops360.ticketing.dto.DateGroupedTickets;
import com.hpy.ops360.ticketing.dto.DocumentDto;
import com.hpy.ops360.ticketing.dto.RejectionReasonListDto;
import com.hpy.ops360.ticketing.dto.TabDataForNTickets;
import com.hpy.ops360.ticketing.dto.TaskApprovedTicketsDto;
import com.hpy.ops360.ticketing.dto.TicketCategoryCountDto;
import com.hpy.ops360.ticketing.dto.TicketCategoryForAllTickets;
import com.hpy.ops360.ticketing.dto.TicketData;
import com.hpy.ops360.ticketing.dto.TicketDataResponse;
import com.hpy.ops360.ticketing.dto.TicketDateGroup;
import com.hpy.ops360.ticketing.dto.TicketGroup;
import com.hpy.ops360.ticketing.dto.TicketHistoryByDateRequest;
import com.hpy.ops360.ticketing.dto.TicketHistoryData;
import com.hpy.ops360.ticketing.dto.TicketHistoryDto;
import com.hpy.ops360.ticketing.dto.TicketHistoryResponse;
import com.hpy.ops360.ticketing.dto.TicketManagementResponse;
import com.hpy.ops360.ticketing.dto.TicketTypeDataForAllTickets;
import com.hpy.ops360.ticketing.dto.TicketUpdateEvent;
import com.hpy.ops360.ticketing.dto.TicketsWithCategoryCountDto;
import com.hpy.ops360.ticketing.entity.CMTask;
import com.hpy.ops360.ticketing.entity.CMTaskDetails;
import com.hpy.ops360.ticketing.entity.CeTaskDetails;
import com.hpy.ops360.ticketing.entity.CheckAtmSource;
import com.hpy.ops360.ticketing.entity.GetManualTicketEventcode;
import com.hpy.ops360.ticketing.entity.RejectionReason;
import com.hpy.ops360.ticketing.entity.TicketUpdateDocument;
import com.hpy.ops360.ticketing.entity.UserAtmDetails;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.CeMachineUpDownCountRepository;
import com.hpy.ops360.ticketing.repository.CeTaskDetailsRepository;
import com.hpy.ops360.ticketing.repository.CheckAtmSourceRepository;
import com.hpy.ops360.ticketing.repository.CmAtmDetailsRepository;
import com.hpy.ops360.ticketing.repository.CmTaskDetailsRepository;
import com.hpy.ops360.ticketing.repository.CmTaskRepository;
import com.hpy.ops360.ticketing.repository.GetManualTicketEventcodeRepository;
import com.hpy.ops360.ticketing.repository.RejectionReasonRepository;
import com.hpy.ops360.ticketing.repository.TicketCountRepository;
import com.hpy.ops360.ticketing.repository.TicketUpdateDocumentRepository;
import com.hpy.ops360.ticketing.response.CreateTicketResponse;
import com.hpy.ops360.ticketing.response.HimsCreateTicketResponse;
import com.hpy.ops360.ticketing.ticket.dto.CMTaskDto;
import com.hpy.ops360.ticketing.ticket.dto.CreateTicketDto;
import com.hpy.ops360.ticketing.ticket.dto.CreateTicketHimsDto;
import com.hpy.ops360.ticketing.util.TicketEventProducer;
import com.hpy.ops360.ticketing.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CmTaskService {

	@Autowired
	private LoginService loginService;

	@Autowired
	private CmTaskRepository cmTaskRepository;

	@Autowired
	private UserAtmDetailsService userAtmDetailsService;

	@Autowired
	private RejectionReasonRepository reasonRepository;

	@Autowired
	private CmAtmDetailsRepository cmAtmDetailsRepository;

	@Autowired
	private SynergyService synergyService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private NTicketHistoryService taskService2;

	@Autowired
	private TicketCountRepository ticketCountRepository;

	@Autowired
	private CmTaskDetailsRepository cmTaskDetailsRepository;

	@Autowired
	private TicketUpdateDocumentRepository ticketUpdateDocumentRepository;

	@Autowired
	private CeMachineUpDownCountRepository ceUpDownCountRepo;

	@Value("${ops360.ticketing.max-atm-list-count}")
	private int maxAtmListCount;

	@Value("${ops360.ticketing.atm-batch-size}")
	private int atmBatchSize;

	@Autowired
	private TicketEventProducer ticketEventProducer;
	
	@Autowired
	private CheckAtmSourceRepository checkAtmSourceRepository;
	
	@Autowired
	private GetManualTicketEventcodeRepository getManualTicketEventcodeRepository;
	
	@Autowired
	private HimsService himsService;
	
	@Autowired
	private CeTaskDetailsRepository ceTaskDetailsRepository;
	// -----------getTicketsNumberDetails--------------------------------------------------------------
	
	public CMTaskDto getTaskByTicketNumber(String createdBy, String ticketNumber, String atmId) {
		String loggedInUser = loginService.getLoggedInUser();
		log.info("Logged in user: {}", loggedInUser);

		if (createdBy == null || ticketNumber == null || atmId == null) {
			throw new IllegalArgumentException("createdBy, ticketNumber, and atmId must not be null");
		}

		CMTaskDetails task = cmTaskDetailsRepository.getTicketsNumberDetails(createdBy, ticketNumber, atmId);

		if (task == null) {
			throw new EmptyResultDataAccessException("No task found for the given parameters", 1);
		}

		log.info("task: {}", task);
		return new CMTaskDto(task.getSrNo(), task.getAtmId(), task.getComment(), task.getCreatedBy(),
				task.getDiagnosis(), task.getDocumentName(), task.getReason(), task.getTicketNumber(), task.getRefNo());
	}

	

	// -------------------------------------------getTicketsNumberOfCEList---------------------------

	public List<CmTasksWithDocumentsDto> getTicketsNumberOfCEList() {

		List<CMTask> cmTask = cmTaskRepository.getTicketsNumberOfCEList(loginService.getLoggedInUser());
		List<CmTasksWithDocumentsDto> cmTasksWithDocumentsDtos = new ArrayList<>();

		for (CMTask manualTasks : cmTask) {
			List<String> documents = new ArrayList<>();
			if (manualTasks.getDocumentName() != null && !manualTasks.getDocumentName().isEmpty()) {
				documents.add(manualTasks.getDocumentName());
			}
			if (manualTasks.getDocument1Name() != null && !manualTasks.getDocument1Name().isEmpty()) {
				documents.add(manualTasks.getDocument1Name());
			}
			if (manualTasks.getDocument2Name() != null && !manualTasks.getDocument2Name().isEmpty()) {
				documents.add(manualTasks.getDocument2Name());
			}
			if (manualTasks.getDocument3Name() != null && !manualTasks.getDocument3Name().isEmpty()) {
				documents.add(manualTasks.getDocument3Name());
			}
			if (manualTasks.getDocument4Name() != null && !manualTasks.getDocument4Name().isEmpty()) {
				documents.add(manualTasks.getDocument4Name());
			}
			cmTasksWithDocumentsDtos.add(CmTasksWithDocumentsDto.builder().srNo(manualTasks.getSrNo())
					.atmId(manualTasks.getAtmId()).comment(manualTasks.getComment())
					.createdBy(manualTasks.getCreatedBy()).diagnosis(manualTasks.getDiagnosis())
					.reason(manualTasks.getReason()).ticketNumber(manualTasks.getTicketNumber())
					.status(manualTasks.getStatus()).refNo(manualTasks.getRefNo())
					.createdDate(manualTasks.getCreatedDate() == null ? "" : manualTasks.getCreatedDate())
					.username(manualTasks.getUsername()).documents(documents).build());
		}
		return cmTasksWithDocumentsDtos;
	}

	public TicketListCountDTO getTicketsNumberOfCEListCount() {
		TicketCount ticketCount = ticketCountRepository.getTicketsNumberOfCEListCount(loginService.getLoggedInUser());
		return new TicketListCountDTO(ticketCount.getSrNo(), ticketCount.getOpenCount());
	}

	public List<RejectionReasonListDto> getRejectionReasons() {
		List<RejectionReason> obj = reasonRepository.getRejectionReasons();

		return obj.stream().map(result -> new RejectionReasonListDto(result.getSr_no(), result.getRejectionReason()))
				.toList();

	}


	private RejectionReasonDto convertToDto(RejectionReason reason) {
		RejectionReasonDto dto = new RejectionReasonDto();
		dto.setRejectionReason(reason.getRejectionReason());
		return dto;
	}

	/*
	 * original code of approve tickets -------------------
	 */
	public ApproveRejectStatusDto updateTicketsNumberData(String status, TaskApprovedTicketsDto taskDTO) {
		Integer ticketStatus;
		if (status.equalsIgnoreCase("approved")) {
			CMTaskDetails ceTaskDetails = cmTaskDetailsRepository.getTicketsNumberDetails(taskDTO.getUserName(),
					taskDTO.getTicketNumber(), taskDTO.getAtmId());
			CreateTicketDto createTicketDto = new CreateTicketDto(ceTaskDetails.getAtmId(),
					ceTaskDetails.getTicketNumber(), "Start diagnosis", ceTaskDetails.getCreatedBy(), "", "",
					ceTaskDetails.getComment());
			log.info("CmTaskService:createSynergyTicketReq:{}", createTicketDto);
			CreateTicketResponse createTicketResponse = synergyService.createTicket(createTicketDto);
			log.info("CmTaskService:createSynergyTicketResp:{}", createTicketResponse);
			ticketStatus = cmTaskRepository.updateTicketsNumberData(taskDTO.getUserName(), taskDTO.getAtmId(),
					createTicketResponse.getTicketno(), taskDTO.getStatus(), loginService.getLoggedInUser(),
					taskDTO.getCheckerRejectReason(), taskDTO.getCheckerComment(), status, taskDTO.getTicketNumber());
			return new ApproveRejectStatusDto(ticketStatus, createTicketResponse.getTicketno());
		}

		ticketStatus = cmTaskRepository.updateTicketsNumberData(taskDTO.getUserName(), taskDTO.getAtmId(), "",
				taskDTO.getStatus(), loginService.getLoggedInUser(), taskDTO.getCheckerRejectReason(),
				taskDTO.getCheckerComment(), status, taskDTO.getTicketNumber());
		return new ApproveRejectStatusDto(ticketStatus, "");
	}

	public ApproveRejectStatusDto updateTicketsNumberData_ViaKafka(String status, TaskApprovedTicketsDto taskDTO) {
		ApproveRejectStatusDto result;
		try {
			if (status.equalsIgnoreCase("approved")) {
				result = handleApprovedTicket(taskDTO);
			} else {
				result = handleRejectedTicket(taskDTO);
			}

			// Send event to Kafka
			TicketUpdateEvent event = TicketUpdateEvent.builder().ticketNumber(taskDTO.getTicketNumber())
					.atmId(taskDTO.getAtmId()).status(status).userName(taskDTO.getUserName())
					.checkerName(loginService.getLoggedInUser()).checkerComment(taskDTO.getCheckerComment())
					.synergyTicketNo(result.getSynergyTicketNo()).timestamp(LocalDateTime.now()).build();

			ticketEventProducer.sendTicketUpdate(event);

			return result;
		} catch (Exception e) {
			log.error("Error updating ticket: {}", e.getMessage(), e);
			throw new RuntimeException("Error processing ticket update", e);
		}
	}
	
	public ApproveRejectStatusDto updateTicketsNumberData_ViaKafkaHims(String status, TaskApprovedTicketsDto taskDTO) {
		ApproveRejectStatusDto result;
		try {
			if (status.equalsIgnoreCase("approved")) {
				result = handleApprovedTicketHims(taskDTO);
			} else {
				result = handleRejectedTicket(taskDTO);
			}

			// Send event to Kafka
			TicketUpdateEvent event = TicketUpdateEvent.builder().ticketNumber(taskDTO.getTicketNumber())
					.atmId(taskDTO.getAtmId()).status(status).userName(taskDTO.getUserName())
					.checkerName(loginService.getLoggedInUser()).checkerComment(taskDTO.getCheckerComment())
					.synergyTicketNo(result.getSynergyTicketNo()).timestamp(LocalDateTime.now()).build();

			ticketEventProducer.sendTicketUpdate(event);

			return result;
		} catch (Exception e) {
			log.error("Error updating ticket: {}", e.getMessage(), e);
			throw new RuntimeException("Error processing ticket update", e);
		}
	}


	private ApproveRejectStatusDto handleApprovedTicket(TaskApprovedTicketsDto taskDTO) {
		CMTaskDetails ceTaskDetails = cmTaskDetailsRepository.getTicketsNumberDetails(taskDTO.getUserName(),
				taskDTO.getTicketNumber(), taskDTO.getAtmId());

		CreateTicketDto createTicketDto = new CreateTicketDto(ceTaskDetails.getAtmId(), ceTaskDetails.getTicketNumber(),
				"Start diagnosis", ceTaskDetails.getCreatedBy(), "", "", ceTaskDetails.getComment());

		log.info("Creating Synergy ticket: {}", createTicketDto);
		CreateTicketResponse createTicketResponse = synergyService.createTicket(createTicketDto);
		log.info("Synergy ticket created: {}", createTicketResponse);

		Integer ticketStatus = cmTaskRepository.updateTicketsNumberData(taskDTO.getUserName(), taskDTO.getAtmId(),
				createTicketResponse.getTicketno(), taskDTO.getStatus(), loginService.getLoggedInUser(),
				taskDTO.getCheckerRejectReason(), taskDTO.getCheckerComment(), "approved", taskDTO.getTicketNumber());

		return new ApproveRejectStatusDto(ticketStatus, createTicketResponse.getTicketno());
	}
	
	private ApproveRejectStatusDto handleApprovedTicketHims(TaskApprovedTicketsDto taskDTO) {
//		CMTaskDetails ceTaskDetails = cmTaskDetailsRepository.getTicketsNumberDetails(taskDTO.getUserName(),
//				taskDTO.getTicketNumber(), taskDTO.getAtmId());
		CeTaskDetails ceTaskDetails = ceTaskDetailsRepository.getCeTaskDetails(taskDTO.getUserName(),
				taskDTO.getTicketNumber(), taskDTO.getAtmId());
		//check for atm source hims/synergy
		
		List<CheckAtmSource> atmWithSource = checkAtmSourceRepository.checkAtmSource(taskDTO.getAtmId());
		
		if (atmWithSource.get(0).getSourceCode() == 0) {
			CreateTicketDto createTicketDto = new CreateTicketDto(ceTaskDetails.getAtmId(), ceTaskDetails.getTicketNumber(),
					"Start diagnosis", ceTaskDetails.getCreatedBy(), "", "", ceTaskDetails.getComment());

			log.info("Creating Synergy ticket: {}", createTicketDto);
			CreateTicketResponse createTicketResponse = synergyService.createTicket(createTicketDto);
			log.info("Synergy ticket created: {}", createTicketResponse);
			if (!createTicketResponse.getStatus().trim().equalsIgnoreCase("success")) {
				log.error("Failed to create ticket in synergy: {}",createTicketResponse);
				return new ApproveRejectStatusDto(-1, taskDTO.getTicketNumber());
			}
			Integer ticketStatus = cmTaskRepository.updateTicketsNumberData(taskDTO.getUserName(), taskDTO.getAtmId(),
					createTicketResponse.getTicketno(), taskDTO.getStatus(), loginService.getLoggedInUser(),
					taskDTO.getCheckerRejectReason(), taskDTO.getCheckerComment(), "approved", taskDTO.getTicketNumber());

			return new ApproveRejectStatusDto(ticketStatus, createTicketResponse.getTicketno());
		}
		else if(atmWithSource.get(0).getSourceCode() == 1)
		{
			Optional<GetManualTicketEventcode> ticketEventcode=getManualTicketEventcodeRepository.getManualTicketEventcode(taskDTO.getTicketNumber(), taskDTO.getAtmId());
			if (ticketEventcode.isPresent()) {
				CreateTicketHimsDto createTicketHimsDto = new CreateTicketHimsDto(ceTaskDetails.getAtmId(), ceTaskDetails.getTicketNumber(),
						"Start diagnosis", ceTaskDetails.getUsername(),ceTaskDetails.getComment(),ticketEventcode.get().getEventcode());
				
				log.info("Creating HIMS ticket: {}", createTicketHimsDto);
				
				HimsCreateTicketResponse  himsCreateTicketResp=himsService.createTicket(createTicketHimsDto);
				
				if (!himsCreateTicketResp.getStatus().trim().equalsIgnoreCase("success")) {
					log.error("Failed to create ticket in hims: {}",himsCreateTicketResp);
					return new ApproveRejectStatusDto(-1, taskDTO.getTicketNumber());
				}
				
				log.info("HIMS ticket created: {}", himsCreateTicketResp);

				Integer ticketStatus = cmTaskRepository.updateTicketsNumberData(taskDTO.getUserName(), taskDTO.getAtmId(),
						himsCreateTicketResp.getTicketno(), taskDTO.getStatus(), loginService.getLoggedInUser(),
						taskDTO.getCheckerRejectReason(), taskDTO.getCheckerComment(), "approved", taskDTO.getTicketNumber());
				
				return new ApproveRejectStatusDto(ticketStatus, himsCreateTicketResp.getTicketno());
				
			}
			log.error("Eventcode cannot be blank: {}",ticketEventcode);
			return new ApproveRejectStatusDto(-1, taskDTO.getTicketNumber());
			
		}
		else {
			log.error("Unknown source or Empty source response: {}",atmWithSource);
			return new ApproveRejectStatusDto(-1, taskDTO.getTicketNumber());
		}
	}

	private ApproveRejectStatusDto handleRejectedTicket(TaskApprovedTicketsDto taskDTO) {
		Integer ticketStatus = cmTaskRepository.updateTicketsNumberData(taskDTO.getUserName(), taskDTO.getAtmId(), "",
				taskDTO.getStatus(), loginService.getLoggedInUser(), taskDTO.getCheckerRejectReason(),
				taskDTO.getCheckerComment(), "rejected", taskDTO.getTicketNumber());

		return new ApproveRejectStatusDto(ticketStatus, "");
	}


	// --------original ---------------------

	public OpenTicketsWithCategoryDto getTicketDetailsByCEAndStatus(String ceId, String status) {
		List<UserAtmDetails> userAtmDetails = userAtmDetailsService.getUserAtmDetails(ceId); // atmids
		List<AtmDetailsDto> atmIds = userAtmDetails.stream()
				.map(atmDetails -> new AtmDetailsDto(atmDetails.getAtm_code())).toList();
		return getTicketsByAtms(status, atmIds);
	}


	private long getDownAtmCnt(List<AtmShortDetailsDto> openTicketsResponse) {
		return openTicketsResponse.stream().filter(ticket -> ticket.getIsBreakdown() == 1).map(a -> a.getAtmId())
				.distinct().count();
	}

	private OpenTicketsWithCategoryDto getTicketsByAtms(String status, List<AtmDetailsDto> atmIds) {
		if (atmIds.isEmpty()) {
			return new OpenTicketsWithCategoryDto(Collections.emptyList(),
					new TicketCategoryCountDto(0, 0, 0, 0, 0, 0,0));
		}

		List<AtmShortDetailsDto> sortedTicketList = taskService.getTicketList(atmIds, maxAtmListCount);

		if (sortedTicketList.isEmpty()) {
			return new OpenTicketsWithCategoryDto(Collections.emptyList(),
					new TicketCategoryCountDto(0, 0, 0, 0, 0, 0,0));
		}

		List<AtmShortDetailsDto> filteredList;

		switch (status.toLowerCase()) {
		case "all":
			filteredList = sortedTicketList.stream().filter(t -> t.getDownCall() == 1)
					.sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()).toList();
			break;
		case "open":
			filteredList = sortedTicketList.stream()
					.filter(e -> e.getDownCall() == 1 && e.getIsUpdated() == 0 && e.getIsTimedOut() == 0)
					.collect(Collectors.toList());
			break;
		case "updated":
			filteredList = sortedTicketList.stream()
					.filter(e -> e.getIsUpdated() == 1 && e.getIsTimedOut() == 0 && e.getDownCall() == 1)
					.collect(Collectors.toList());
			break;
		case "istimedout":
			filteredList = sortedTicketList.stream()
					.filter(e -> e.getIsUpdated() == 0 && e.getIsTimedOut() == 1 && e.getDownCall() == 1)
					.collect(Collectors.toList());
			break;
		default:
			filteredList = sortedTicketList;
		}

		return new OpenTicketsWithCategoryDto(filteredList, getEventCategoryCounts(filteredList));
	}

//	-----------------------------------for flag-------------------------------------------------

	public OpenTicketsWithCategoryDto_forAllTickets getTicketDetailsByCEAndStatus_forAllTicketOriginal(String ceId,
			String status) {

		DisableSslClass.disableSSLVerification();
		List<UserAtmDetails> userAtmDetails = userAtmDetailsService.getUserAtmDetails(ceId); // ATM IDs
		List<AtmDetailsDto> atmIds = userAtmDetails.stream()
				.map(atmDetails -> new AtmDetailsDto(atmDetails.getAtm_code())).toList();

		return getTicketsByAtms_forAllTickets(status, atmIds);
	}

	private OpenTicketsWithCategoryDto_forAllTickets getTicketsByAtms_forAllTickets(String status,
			List<AtmDetailsDto> atmIds) {
		if (atmIds.isEmpty()) {
			return new OpenTicketsWithCategoryDto_forAllTickets();
		}

		List<AtmShortDetailsDto> sortedTicketList = taskService.getTicketList(atmIds, maxAtmListCount);

		if (sortedTicketList.isEmpty()) {
			return new OpenTicketsWithCategoryDto_forAllTickets();
		}

		// Apply status filters
		List<AtmShortDetailsDto> filteredList;
		switch (status.toLowerCase()) {
		case "all":
			filteredList = sortedTicketList.stream().filter(t -> t.getDownCall() == 1).collect(Collectors.toList());
			break;
		case "open":
			filteredList = sortedTicketList.stream()
					.filter(e -> e.getDownCall() == 1 && e.getIsUpdated() == 0 && e.getIsTimedOut() == 0)
					.collect(Collectors.toList());
			break;
		case "updated":
			filteredList = sortedTicketList.stream()
					.filter(e -> e.getIsUpdated() == 1 && e.getIsTimedOut() == 0 && e.getDownCall() == 1)
					.collect(Collectors.toList());
			break;
		case "istimedout":
			filteredList = sortedTicketList.stream()
					.filter(e -> e.getIsUpdated() == 0 && e.getIsTimedOut() == 1 && e.getDownCall() == 1)
					.collect(Collectors.toList());
			break;
		default:
			filteredList = sortedTicketList;
		}

		// Create response DTO
		OpenTicketsWithCategoryDto_forAllTickets response = new OpenTicketsWithCategoryDto_forAllTickets();

		List<AtmShortDetailsDto> flaggedTickets = filteredList.stream().filter(t -> {
			Integer flagStatus = t.getFlagStatus();
			return flagStatus == null || Objects.equals(flagStatus, 1);
		}).collect(Collectors.toList());

		if (flaggedTickets.isEmpty()) {
			// Create a single entry with the message
			Map<String, Object> emptyGroup = new HashMap<>();
			// emptyGroup.put("date", "No Data");
			emptyGroup.put("data", Collections.emptyList());
			// emptyGroup.put("message", "No flag tickets are found");

			response.setFlagticketData(Collections.singletonList(emptyGroup));
		} else {
			Map<String, List<AtmShortDetailsDto>> groupedFlaggedTickets = flaggedTickets.stream()
					.collect(Collectors.groupingBy(ticket -> {
						LocalDateTime flagTime = ticket.getFlagStatusInsertTime();
						if (flagTime == null) {
							return "Unknown Date";
						}

						LocalDate flagDate = flagTime.toLocalDate();
						LocalDate today = LocalDate.now();
						LocalDate yesterday = today.minusDays(1);

						if (flagDate.equals(today)) {
							return "Today | " + formatDate(today);
						} else if (flagDate.equals(yesterday)) {
							return "Yesterday | " + formatDate(yesterday);
						} else {
							return formatDate(flagDate);
						}
					}));

			List<Map<String, Object>> dateGroups = createDateGroups(groupedFlaggedTickets);
			if (dateGroups.isEmpty()) {
				// Create a single entry with the message
				Map<String, Object> emptyGroup = new HashMap<>();
				emptyGroup.put("date", "No Data");
				emptyGroup.put("data", Collections.emptyList());
				emptyGroup.put("message", "No flag tickets are found");

				response.setFlagticketData(Collections.singletonList(emptyGroup));
			} else {
				response.setFlagticketData(dateGroups);
			}
		}

		// Process unflagged tickets
		List<AtmShortDetailsDto> unflaggedTickets = filteredList.stream().filter(t -> t.getFlagStatus() == 0)
				.collect(Collectors.toList());

		if (!unflaggedTickets.isEmpty()) {
			Map<String, List<AtmShortDetailsDto>> groupedUnflaggedTickets = unflaggedTickets.stream()
					.collect(Collectors.groupingBy(ticket -> {
						String createdDate = ticket.getCreatedDate();
						if (createdDate == null || createdDate.isEmpty()) {
							return "Unknown Date";
						}
						try {
							// Parse date from "dd MMM 'yy, hh:mm a" format
							String datePart = createdDate.split(",")[0]; // Get "dd MMM 'yy"
							DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd MMM ''yy", Locale.US);
							LocalDate date = LocalDate.parse(datePart.trim(), inputFormatter);

							LocalDate today = LocalDate.now();
							LocalDate yesterday = today.minusDays(1);

							if (date.equals(today)) {
								return "Today | " + formatDate(today);
							} else if (date.equals(yesterday)) {
								return "Yesterday | " + formatDate(yesterday);
							} else {
								return formatDate(date);
							}
						} catch (Exception e) {
							log.error("Error parsing created date: {} for ticket: {}", createdDate,
									ticket.getTicketNumber());
							return "Unknown Date";
						}
					}));

			response.setOtherticketData(createDateGroups(groupedUnflaggedTickets));
		}

		// Set category counts
		response.setTicketCategoryCountDto(getEventCategoryCounts(filteredList));
		return response;
	}

	private List<Map<String, Object>> createDateGroups(Map<String, List<AtmShortDetailsDto>> groupedTickets) {
		return groupedTickets.entrySet().stream().sorted((a, b) -> {
			String dateA = a.getKey();
			String dateB = b.getKey();

			// Special cases first
			if (dateA.equals("Unknown Date"))
				return 1;
			if (dateB.equals("Unknown Date"))
				return -1;
			if (dateA.startsWith("Today"))
				return -1;
			if (dateB.startsWith("Today"))
				return 1;
			if (dateA.startsWith("Yesterday"))
				return -1;
			if (dateB.startsWith("Yesterday"))
				return 1;

			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy");
				// Extract date part if it contains separator
				String dateStrA = dateA.contains("|") ? dateA.split("\\|")[1].trim() : dateA;
				String dateStrB = dateB.contains("|") ? dateB.split("\\|")[1].trim() : dateB;

				LocalDate dateObjA = LocalDate.parse(dateStrA, formatter);
				LocalDate dateObjB = LocalDate.parse(dateStrB, formatter);
				return dateObjB.compareTo(dateObjA); // Descending order
			} catch (Exception e) {
				log.error("Error comparing dates: {} and {}", dateA, dateB);
				return 0;
			}
		}).map(entry -> {
			Map<String, Object> group = new HashMap<>();
			group.put("date", entry.getKey());
			group.put("data", entry.getValue());
			return group;
		}).collect(Collectors.toList());
	}

	/*
	 * -----------------------------------for flag new
	 * Response---------------------------------------------
	 * 
	 */

	public TicketManagementResponse getTicketDetailsByCEAndStatus_newResponse(String ceId) {
		List<UserAtmDetails> userAtmDetails = userAtmDetailsService.getUserAtmDetails(ceId);
		List<AtmDetailsDto> atmIds = userAtmDetails.stream()
				.map(atmDetails -> new AtmDetailsDto(atmDetails.getAtm_code())).toList();

		return processTickets_newResponse(atmIds);
	}

	/*
	 * this getAllTicketDetailsBasedOnIndex [,] processInBatches two method
	 * developed by @SachinSonawane
	 */

	public static Map<Integer, List<String>> processInBatches(List<String> atmId, int batchSize) {
		Map<Integer, List<String>> resultMap = new HashMap<>();
		int key = 0; // Start key from 0

		for (int i = 0; i < atmId.size(); i += batchSize) {
			List<String> batch = atmId.subList(i, Math.min(i + batchSize, atmId.size()));
			resultMap.put(key++, batch);
		}

		return resultMap;
	}

	public TicketManagementResponse getAllTicketDetailsBasedOnIndex(String cmId, int index) {
		List<String> userAtmList = userAtmDetailsService.getCmAtmList(cmId);

		if (userAtmList.isEmpty()) {
			return new TicketManagementResponse(Collections.emptyList());
		}

		// Process in batches
		Map<Integer, List<String>> resultMap = processInBatches(userAtmList, atmBatchSize);

		// Retrieve the list for the given key
		List<String> batchAtms = resultMap.get(index);
		if (batchAtms == null) {
			return new TicketManagementResponse(Collections.emptyList());
		}
		return processTickets_newResponse(batchAtms.stream().map(atmDetails -> new AtmDetailsDto(atmDetails)).toList());
	}

	private TicketManagementResponse processTickets_newResponse(List<AtmDetailsDto> atmIds) {
		if (atmIds.isEmpty()) {
			return new TicketManagementResponse();
		}

		List<AtmShortDetailsDto> allTickets = taskService.getTicketList(atmIds, maxAtmListCount);
		return buildResponse(allTickets);
	}

	private TicketManagementResponse buildResponse(List<AtmShortDetailsDto> allTickets) {
		TicketManagementResponse response = new TicketManagementResponse();
		List<TicketCategoryForAllTickets> categories = new ArrayList<>();

		// All Tickets Category
		categories.add(createTicketCategory("All Tickets", allTickets));

		// Open Tickets Category
		List<AtmShortDetailsDto> openTickets = allTickets.stream()
				.filter(t -> t.getIsUpdated() == 0 && t.getIsTimedOut() == 0).collect(Collectors.toList());
		categories.add(createTicketCategory("Open Tickets", openTickets));

		// Timeout Tickets Category
		List<AtmShortDetailsDto> timeoutTickets = allTickets.stream().filter(t -> t.getIsTimedOut() == 1)
				.collect(Collectors.toList());
		categories.add(createTicketCategory("Timeout Tickets", timeoutTickets));

		// Updated Tickets Category
		List<AtmShortDetailsDto> updatedTickets = allTickets.stream().filter(t -> t.getIsUpdated() == 1)
				.collect(Collectors.toList());
		categories.add(createTicketCategory("Updated Tickets", updatedTickets));

		response.setData(categories);
		return response;
	}

	private TicketCategoryForAllTickets createTicketCategory(String name, List<AtmShortDetailsDto> tickets) {
		TicketCategoryForAllTickets category = new TicketCategoryForAllTickets();
		category.setName(name);
		category.setCount(tickets.size());

		List<TicketTypeDataForAllTickets> typeDataList = Arrays.asList(createTicketType("Total Tickets", tickets),
				createTicketType("Cash", filterByEventGroup(tickets, "Cash")),
				createTicketType("Communication", filterByEventGroup(tickets, "Communication")),
				createTicketType("Hardware Fault", filterByEventGroup(tickets, "Hardware Fault")),
				createTicketType("No Transactions", filterByEventGroup(tickets, "No Transactions")),
				createTicketType("Supervisory", filterByEventGroup(tickets, "Supervisory")),
				createTicketType("Others", filterByEventGroup(tickets, "Others")));

		category.setData(typeDataList);
		return category;
	}

	private TicketTypeDataForAllTickets createTicketType(String name, List<AtmShortDetailsDto> tickets) {
		TicketTypeDataForAllTickets typeData = new TicketTypeDataForAllTickets();
		typeData.setName(name);
		typeData.setCount(tickets.size());

		List<AtmShortDetailsDto> flaggedTickets = tickets.stream().filter(t -> t.getFlagStatus() == 1)
				.filter(t -> !t.getSubcall().contains("Data;ATM Transacting")).collect(Collectors.toList());
		typeData.setFlagedTickets(groupTicketsByDate(flaggedTickets));

		List<AtmShortDetailsDto> otherTickets = tickets.stream()
				.filter(t -> t.getFlagStatus() == 0 || t.getSubcall().contains("Data;ATM Transacting"))
				.collect(Collectors.toList());
		typeData.setOtherTickets(groupTicketsByDate(otherTickets));

		return typeData;
	}

	private List<AtmShortDetailsDto> filterByEventGroup(List<AtmShortDetailsDto> tickets, String eventGroup) {
		return tickets.stream().filter(t -> eventGroup.equals(t.getEventGroup())).collect(Collectors.toList());
	}

	private List<TicketDateGroup> groupTicketsByDate(List<AtmShortDetailsDto> tickets) {
		if (tickets.isEmpty()) {
			return Collections.emptyList();
		}

		Map<String, List<AtmShortDetailsDto>> ticketsByDate = tickets.stream().collect(Collectors.groupingBy(ticket -> {
			LocalDateTime dateTime = ticket.getFlagStatus() == 1 ? ticket.getFlagStatusInsertTime()
					: parseCreatedDate(ticket.getCreatedDate());

			return formatDateGroup(dateTime);
		}));

		return ticketsByDate.entrySet().stream()
				.map(entry -> new TicketDateGroup(entry.getKey(), mapToTicketData(entry.getValue())))
				.sorted(compareDateGroups()).collect(Collectors.toList());
	}

	private LocalDateTime parseCreatedDate(String createdDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM ''yy, hh:mm a", Locale.US);
			return LocalDateTime.parse(createdDate, formatter);
		} catch (Exception e) {
			// log.error("Error parsing date: {}", createdDate);
			return LocalDateTime.now();
		}
	}

	private String formatDateGroup(LocalDateTime dateTime) {
		LocalDate date = dateTime.toLocalDate();
		LocalDate today = LocalDate.now();

		if (date.equals(today)) {
			return "Today | " + formatDate(date);
		} else if (date.equals(today.minusDays(1))) {
			return "Yesterday | " + formatDate(date);
		}
		return formatDate(date);
	}

	private String formatDate(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern("dd MMM ''yy"));
	}

	private List<TicketData> mapToTicketData(List<AtmShortDetailsDto> tickets) {
		return tickets.stream().map(this::convertToTicketData).collect(Collectors.toList());
	}

	private TicketData convertToTicketData(AtmShortDetailsDto dto) {
		return new TicketData(dto.getRequestid(), dto.getAtmId(), dto.getTicketNumber(), dto.getBank(),
				dto.getSiteName(), dto.getOwner(), dto.getSubcall(), dto.getVendor(), dto.getError(), dto.getDownTime(),
				dto.getPriorityScore(), dto.getEventGroup(), dto.getIsBreakdown(), dto.getIsUpdated(),
				dto.getIsTimedOut(), dto.getIsTravelling(), dto.getTravelTime(), dto.getTravelEta(), dto.getDownCall(),
				dto.getEtaDateTime(), dto.getEtaTimeout(), dto.getCreatedDate(), dto.getCloseDate(),
				dto.getFlagStatus(), dto.getFlagStatusInsertTime(), dto.getColor(), dto.getCeName());
	}

	private Comparator<TicketDateGroup> compareDateGroups() {
		return (g1, g2) -> {
			if (g1.getDate().startsWith("Today"))
				return -1;
			if (g2.getDate().startsWith("Today"))
				return 1;
			if (g1.getDate().startsWith("Yesterday"))
				return -1;
			if (g2.getDate().startsWith("Yesterday"))
				return 1;

			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy");
				LocalDate d1 = LocalDate.parse(g1.getDate().split("\\|")[1].trim(), formatter);
				LocalDate d2 = LocalDate.parse(g2.getDate().split("\\|")[1].trim(), formatter);
				return d2.compareTo(d1);
			} catch (Exception e) {
				return 0;
			}
		};
	}


	// -----------------------for flag  newResponse----------------------------------------------------

	private TicketCategoryCountDto getEventCategoryCounts(List<AtmShortDetailsDto> sortedTicketList) {
		log.info("getEventCategoryCounts()|atmTicketEventList:{}", sortedTicketList);

		// Use parallel stream for better performance
		Map<String, Long> eventGroupCounts = sortedTicketList.parallelStream()
				.collect(Collectors.groupingBy(AtmShortDetailsDto::getEventGroup, Collectors.counting()));

		return TicketCategoryCountDto.builder().total(sortedTicketList.size())
				.cash(eventGroupCounts.getOrDefault("Cash", 0L).intValue())
				.communication(eventGroupCounts.getOrDefault("Communication", 0L).intValue())
				.hardwareFault(eventGroupCounts.getOrDefault("Hardware Fault", 0L).intValue())
				.noTransactions(eventGroupCounts.getOrDefault("No Transactions", 0L).intValue())
				.supervisory(eventGroupCounts.getOrDefault("Supervisory", 0L).intValue())
				.others(eventGroupCounts.getOrDefault("Others", 0L).intValue()).build();
	}


	public FileDownloadDto download(String createdBy, String ticketNumber, String atmId, int index) {
		CMTaskDetails task = cmTaskDetailsRepository.getTicketsNumberDetails(createdBy, ticketNumber, atmId);
		String documentName = task.getDocumentName();
		String document = task.getDocument();
		if (index == 0) {
			documentName = task.getDocumentName() == null ? "" : task.getDocumentName();
			document = task.getDocument() == null ? "" : task.getDocument();
		} else if (index == 1) {
			documentName = task.getDocument1Name() == null ? "" : task.getDocument1Name();
			document = task.getDocument1() == null ? "" : task.getDocument1();
		} else if (index == 2) {
			documentName = task.getDocument2Name() == null ? "" : task.getDocument2Name();
			document = task.getDocument2() == null ? "" : task.getDocument2();
		} else if (index == 3) {
			documentName = task.getDocument3Name() == null ? "" : task.getDocument3Name();
			document = task.getDocument3() == null ? "" : task.getDocument3();
		} else if (index == 4) {
			documentName = task.getDocument4Name() == null ? "" : task.getDocument4Name();
			document = task.getDocument4() == null ? "" : task.getDocument4();
		}

		return new FileDownloadDto(documentName, document);
	}

	//---------original code ------------------
	
	public OpenTicketsWithCategoryDto getTicketDetailsByCEAgainstAtm(String atmId, String ceId) {
		// Check if the given atmId belongs to the CE
		DisableSslClass.disableSSLVerification();
		List<UserAtmDetails> userAtmDetails = userAtmDetailsService.getUserAtmDetails(ceId);
		boolean atmBelongsToCe = userAtmDetails.stream().anyMatch(atmDetails -> atmDetails.getAtm_code().equals(atmId));

		if (!atmBelongsToCe) {
			// If the ATM doesn't belong to the CE, return an empty result
			return new OpenTicketsWithCategoryDto(Collections.emptyList(),
					new TicketCategoryCountDto(0, 0, 0, 0, 0, 0,0));
		}

		// Create a list with only the specified ATM ID
		List<AtmDetailsDto> atmIds = Collections.singletonList(new AtmDetailsDto(atmId));

		return getCeOpenTicketDetailsAgainstAtm(ceId, atmIds);
	}

	public OpenTicketsWithCategoryDto getCeOpenTicketDetailsAgainstAtm(String ceId, List<AtmDetailsDto> atmIds) {
		if (atmIds.isEmpty()) {
			return new OpenTicketsWithCategoryDto(Collections.emptyList(),
					new TicketCategoryCountDto(0, 0, 0, 0, 0, 0,0));
		}

		// Fetch all tickets for the specified ATM ID
		List<AtmShortDetailsDto> allTickets = taskService.getTicketList(atmIds, Integer.MAX_VALUE);

		if (allTickets.isEmpty()) {
			return new OpenTicketsWithCategoryDto(Collections.emptyList(),
					new TicketCategoryCountDto(0, 0, 0, 0, 0, 0,0));
		}

		// Filter down calls and sort by priority and downtime
		List<AtmShortDetailsDto> sortedDownCalls = allTickets.stream().filter(t -> t.getDownCall() == 1)
				.sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()
						.thenComparing(AtmShortDetailsDto::getDownTime).reversed())
				.toList();

		return new OpenTicketsWithCategoryDto(sortedDownCalls, getEventCategoryCounts(sortedDownCalls));
	}


	public TicketDataResponse transformToTicketDataResponse(OpenTicketsWithCategoryDto ticketDetails) {
	    List<CategoryData> categoryDataList = new ArrayList<>();
	    List<AtmShortDetailsDto> allTickets = ticketDetails.getTicketShortDetails();
	    
	    // Deduplicate all tickets first
	    Map<String, AtmShortDetailsDto> uniqueTickets = allTickets.stream()
	        .collect(Collectors.toMap(
	            AtmShortDetailsDto::getTicketNumber,
	            ticket -> ticket,
	            (existing, replacement) -> existing
	        ));
	    
	    List<AtmShortDetailsDto> deduplicatedTickets = new ArrayList<>(uniqueTickets.values());
	    TicketCategoryCountDto counts = ticketDetails.getTicketCategoryCount();

	    // Add Total Ticket category
	    categoryDataList.add(createCategoryData("Total Ticket", 
	        uniqueTickets.size(), 
	        deduplicatedTickets));

	    // Add Cash category
	    categoryDataList.add(createCategoryData("Cash", 
	        (int) uniqueTickets.values().stream().filter(t -> "Cash".equals(t.getEventGroup())).count(),
	        filterTicketsByCategory(deduplicatedTickets, "Cash")));

	    // Add Communication category
	    categoryDataList.add(createCategoryData("Communication", 
	        (int) uniqueTickets.values().stream().filter(t -> "Communication".equals(t.getEventGroup())).count(),
	        filterTicketsByCategory(deduplicatedTickets, "Communication")));

	    // Add Hardware Fault category
	    categoryDataList.add(createCategoryData("Hardware Fault", 
	        (int) uniqueTickets.values().stream().filter(t -> "Hardware Fault".equals(t.getEventGroup())).count(),
	        filterTicketsByCategory(deduplicatedTickets, "Hardware Fault")));
	        
	     // Add Hardware Fault category
		    categoryDataList.add(createCategoryData("No Transactions", 
		        (int) uniqueTickets.values().stream().filter(t -> "No Transactions".equals(t.getEventGroup())).count(),
		        filterTicketsByCategory(deduplicatedTickets, "No Transactions")));
	    // Add Supervisory category
	    categoryDataList.add(createCategoryData("Supervisory", 
	        (int) uniqueTickets.values().stream().filter(t -> "Supervisory".equals(t.getEventGroup())).count(),
	        filterTicketsByCategory(deduplicatedTickets, "Supervisory")));

	    // Add Others category
	    categoryDataList.add(createCategoryData("Others", 
	        (int) uniqueTickets.values().stream().filter(t -> "Others".equals(t.getEventGroup())).count(),
	        filterTicketsByCategory(deduplicatedTickets, "Others")));

	    return TicketDataResponse.builder()
	        .count(uniqueTickets.size())
	        .data(categoryDataList)
	        .build();
	}
	
	
	
	private List<AtmShortDetailsDto> filterTicketsByCategory(List<AtmShortDetailsDto> tickets, String category) {
	    if (tickets == null) return new ArrayList<>();
	    return tickets.stream()
	        .filter(ticket -> category.equals(ticket.getEventGroup())) // Exact match instead of ignoreCase
	        .collect(Collectors.toList());
	}


	private CategoryData createCategoryData(String name, Integer count, List<AtmShortDetailsDto> tickets) {
	    if (tickets == null) {
	        tickets = new ArrayList<>();
	    }

	    // First deduplicate tickets based on ticketNumber to prevent duplicates
	    Map<String, AtmShortDetailsDto> uniqueTickets = tickets.stream()
	        .collect(Collectors.toMap(
	            AtmShortDetailsDto::getTicketNumber,
	            ticket -> ticket,
	            (existing, replacement) -> existing  // Keep the first occurrence if duplicate found
	        ));
	    
	    // Use the deduplicated tickets list
	    List<AtmShortDetailsDto> deduplicatedTickets = new ArrayList<>(uniqueTickets.values());

	    // Now process flagged and open tickets from deduplicated list
	    Map<String, List<AtmShortDetailsDto>> flaggedTickets = deduplicatedTickets.stream()
	        .filter(ticket -> ticket.getFlagStatus() == 1)
	        .collect(Collectors.groupingBy(this::getFormattedDate));

	    Map<String, List<AtmShortDetailsDto>> openTickets = deduplicatedTickets.stream()
	        .filter(ticket -> ticket.getFlagStatus() == 0)
	        .collect(Collectors.groupingBy(this::getFormattedDate));

	    return CategoryData.builder()
	        .name(name)
	        .count(count)
	        .flagedTickets(convertToTicketGroups(flaggedTickets))
	        .openTickets(convertToTicketGroups(openTickets))
	        .build();
	}


	private String getFormattedDate(AtmShortDetailsDto ticket) {
	    try {
	        // Parse the input date format "dd MMM 'yyyy, hh:mm a"
//	        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd MMM ''yyyy, hh:mm a", Locale.ENGLISH);
//	        LocalDateTime dateTime = LocalDateTime.parse(ticket.getCreatedDate(), inputFormatter);
	        
	       // LocalDate date = dateTime.toLocalDate();
	        LocalDate ticketDate = parseCreatedDateForRecentTickets(ticket.getCreatedDate());
	        LocalDate today = LocalDate.now();
	        
//	        String dateStr = ticket.getCreatedDate();
//	        
//	        int commaIndex = dateStr.indexOf(',');
//	        String dateWithoutTime = commaIndex > 0 ? dateStr.substring(0, commaIndex) : dateStr;

	        if (ticketDate.equals(today)) {
	            return "Today | " + formatDate(ticketDate);
	        } else if (ticketDate.equals(today.minusDays(1))) {
	            return "Yesterday | " + formatDate(ticketDate);
	        }
	        
	        return formatDate(ticketDate); // Return date without time for other dates
	        
	    } catch (DateTimeParseException e) {
	        // If parsing fails, return original date string without time
	        String dateStr = ticket.getCreatedDate();
	        int commaIndex = dateStr.indexOf(',');
	        return commaIndex > 0 ? dateStr.substring(0, commaIndex) : dateStr;
	    }
	}
	
	  public static LocalDate parseCreatedDateForRecentTickets(String dateTimeString) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM ''yy");
	        try {
	            return LocalDate.parse(dateTimeString, formatter);
	        } catch (DateTimeParseException e) {
	            System.err.println("Error parsing date-time string: " + e.getMessage());
	            return LocalDate.now(); // or handle the error as needed
	        }
	    }

	
	private List<TicketGroup> convertToTicketGroups(Map<String, List<AtmShortDetailsDto>> ticketMap) {
	    if (ticketMap.isEmpty()) {
	        return new ArrayList<>();
	    }

	    return ticketMap.entrySet().stream()
	        .map(entry -> TicketGroup.builder()
	            .date(entry.getKey())
	            .data(entry.getValue())
	            .build())
	        .sorted((date1, date2) -> {
	        	String d1 = date1.getDate(), d2 = date2.getDate();

	        	if(date1.getDate().startsWith("Today | "))
			 	{
			 		d1 = date1.getDate().replace("Today | ","");
			 		
			 	}
			 	
			 	if(date1.getDate().startsWith("Yesterday | "))
			 	{
			 		d1 = date1.getDate().replace("Yesterday | ","");
			 		
			 	}
			 	
			 	if(date2.getDate().startsWith("Today | "))
			 	{
			 		d2 = date2.getDate().replace("Today | ","");
			 		
			 	}
			 	if(date2.getDate().startsWith("Yesterday | "))
			 	{
			 		d2 = date2.getDate().replace("Yesterday | ","");
			 		
			 	}
			 	
			 	System.out.println("---format---"+d1+"----format"+d2);
			 	DateTimeFormatter dateGroupFormatter = DateTimeFormatter.ofPattern("dd MMM ''yy");
			 	
	            LocalDate dt1 = LocalDate.parse(d1, dateGroupFormatter);
	            LocalDate dt2 = LocalDate.parse(d2, dateGroupFormatter);
	            return dt2.compareTo(dt1); 
	        }) // Sort by date descending
	        .collect(Collectors.toList());
	}
	
	// ----------------------count open, update ,IsTimedOut closed--------------------------

	public TicketsWithCategoryCountDto getCeAllTicketDetailsCount() {

//		Map<String, Integer> ticketCategoryCount = new HashMap<>();

		List<AtmDetails> cmAtmDetails = cmAtmDetailsRepository.getCmAtmDetails(loginService.getLoggedInUser());

		if (cmAtmDetails.isEmpty()) {
			return new TicketsWithCategoryCountDto(0, 0, 0, 0);
		}
		List<AtmDetailsDto> atmIds = cmAtmDetails.stream().map(atmDetails -> new AtmDetailsDto(atmDetails.getAtmCode()))
				.toList();

		List<AtmShortDetailsDto> sortedTicketList = taskService.getTicketList(atmIds, maxAtmListCount);
		
		if (sortedTicketList.isEmpty()) {
			return new TicketsWithCategoryCountDto(0, 0, 0, 0);
		}

		Integer openCount = 0;
		Integer updatedCount = 0;
		Integer timedOutCount = 0;
		Integer closedCount = 0;

		for (AtmShortDetailsDto ticket : sortedTicketList) {

			if (ticket.getIsUpdated() == 1 && ticket.getIsTimedOut() == 0 && ticket.getDownCall() == 1) {
				updatedCount++;
			} else if (ticket.getIsTimedOut() == 1 && ticket.getIsUpdated() == 0 && ticket.getDownCall() == 1) {
				timedOutCount++;
			} else if (ticket.getIsTimedOut() == 0 && ticket.getIsUpdated() == 0 && ticket.getDownCall() == 1) {
				openCount++;
			}
		}

		return new TicketsWithCategoryCountDto(openCount, updatedCount, timedOutCount, closedCount);

	}

	// ------------------------------------------ntickets of original response --------------------------

	public OpenTicketsWithCategoryDto getCe_nTicketDetailsAgainstAtm(TicketHistoryDto ticketHistoryDto) {
//		List<AtmDetailsDto> atmIds = Collections.singletonList(new AtmDetailsDto(ticketHistoryDto.getAtmid()));
		DisableSslClass.disableSSLVerification();
		List<AtmShortDetailsDto> sortedTicketList = taskService.getnTicketList(ticketHistoryDto, maxAtmListCount);
		if (sortedTicketList.isEmpty()) {
			return new OpenTicketsWithCategoryDto(Collections.emptyList(),
					new TicketCategoryCountDto(0, 0, 0, 0, 0, 0,0));
		}
		return new OpenTicketsWithCategoryDto(sortedTicketList, getEventCategoryCounts(sortedTicketList));
	}
//------------------------------------------added for tickets History by date--------------------------------

	public OpenTicketsWithCategoryDto getTicketHistoryBydateForCe(
			TicketHistoryByDateRequest ticketHistoryByDateRequest) {
		try {
			DisableSslClass.disableSSLVerification();
			List<AtmShortDetailsDto> sortedTicketList = taskService.getTicketListBydate(
					ticketHistoryByDateRequest.getAtmid(), ticketHistoryByDateRequest.getFromdate());

			if (sortedTicketList.isEmpty()) {
				return new OpenTicketsWithCategoryDto(Collections.emptyList(),
						new TicketCategoryCountDto(0, 0, 0, 0, 0, 0,0));
			}

			return new OpenTicketsWithCategoryDto(sortedTicketList, getEventCategoryCounts(sortedTicketList));
		} catch (Exception e) {
			log.error("Error in getTicketHistoryBydate: ", e);
			throw new RuntimeException("Failed to get ticket history", e);
		}
	}

	public OpenTicketsWithCategoryDto getTicketHistoryBydateForCm(String atmId) {

		try {
			DisableSslClass.disableSSLVerification();
			// Calculate last 3 months date
			LocalDate currentDate = LocalDate.now();
			LocalDate threeMonthsAgo = currentDate.minusMonths(3);
			String fromDate = threeMonthsAgo.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			List<AtmShortDetailsDto> sortedTicketList = taskService.getTicketListBydate(atmId, fromDate);

			if (sortedTicketList.isEmpty()) {
				return new OpenTicketsWithCategoryDto(Collections.emptyList(),
						new TicketCategoryCountDto(0, 0, 0, 0, 0, 0,0));
			}

			return new OpenTicketsWithCategoryDto(sortedTicketList, getEventCategoryCounts(sortedTicketList));
		} catch (Exception e) {
			log.error("Error in getTicketHistoryBydate: ", e);
			throw new RuntimeException("Failed to get ticket history", e);
		}
	}


	/**
	 * @owner :- Shubham Shinde
	 * @get_n_tickets_history:- get_n_tickets_history (Only 10 tickets will show
	 *                          )given the response according this new figma 1.5
	 * @return:-Success
	 */

	public TicketHistoryResponse processTickets_forNticketHistory(TicketHistoryDto ticketHistoryDto) {
		DisableSslClass.disableSSLVerification();
		List<AtmShortDetailsDto> ticketList = taskService2.getnTicketList(ticketHistoryDto, maxAtmListCount);
//		if (ticketList.isEmpty()) {
//			return new TicketHistoryResponse();
//		}
		if (ticketList == null || ticketList.isEmpty()) {
	        TicketHistoryData defaultData = new TicketHistoryData(0, new ArrayList<>());
	        return new TicketHistoryResponse(null, defaultData);
	    }
		
		return buildResponseforNTickets(ticketList);
	}
	
	public TicketHistoryResponse processTickets_forNticketHistory_Ce(TicketHistoryDto ticketHistoryDto) {
		DisableSslClass.disableSSLVerification();
		List<AtmShortDetailsDto> ticketList = taskService.getnTicketList(ticketHistoryDto, maxAtmListCount);
		if (ticketList.isEmpty()) {
			return new TicketHistoryResponse();
		}
		return buildResponseforNTicketsCe(ticketList);
	}
	
	private TicketHistoryResponse buildResponseforNTicketsCe(List<AtmShortDetailsDto> tickets) {
		TicketHistoryResponse response = new TicketHistoryResponse();
		TicketHistoryData historyData = new TicketHistoryData();
		historyData.setTotal_count(tickets.size());

		List<TabDataForNTickets> tabDataList = new ArrayList<>();

		// Add tabs in specified sequence
		List<String> eventGroups = Arrays.asList("All", "Cash", "Communication", "Hardware Faults","No Transactions", "Supervisory",
				"Others");

		eventGroups.forEach(groupName -> {
		    List<AtmShortDetailsDto> filteredTickets;
		    if ("All".equals(groupName)) {
		        filteredTickets = tickets;
		    } else {
		        filteredTickets = tickets.stream()
		            .filter(t -> {
		                if (t.getEventGroup() == null) {
		                    return "Others".equals(groupName); // Null event groups go to Others
		                }
		                switch(groupName) {
		                    case "Cash":
		                        return t.getEventGroup().contains("Cash") || 
		                               t.getEventGroup().contains("cash");
		                    case "Communication":
		                        return t.getEventGroup().contains("Communication") || 
		                               t.getEventGroup().contains("Network");
		                    case "Hardware Faults":
		                        return t.getEventGroup().contains("Hardware") || 
		                               t.getEventGroup().contains("Device");
		                    case "No Transactions":
		                        return t.getEventGroup().contains("No Transactions") || 
		                               t.getEventGroup().contains("No Transaction");
		                    case "Supervisory":
		                        return t.getEventGroup().contains("Supervisory") || 
		                               t.getEventGroup().contains("Admin");
		                    case "Others":
		                        // If doesn't match any other category, it goes to Others
		                        return !t.getEventGroup().contains("Cash") &&
		                               !t.getEventGroup().contains("Communication") &&
		                               !t.getEventGroup().contains("Hardware") &&
		                               !t.getEventGroup().contains("No Transactions")&&
		                               !t.getEventGroup().contains("Supervisory");
		                    default:
		                        return false;
		                }
		            })
		            .collect(Collectors.toList());
		    }
		    tabDataList.add(createTabData(groupName, filteredTickets));
		});

		historyData.setTab_data(tabDataList);
		response.setData(historyData);
		return response;
	}


	private TicketHistoryResponse buildResponseforNTickets(List<AtmShortDetailsDto> tickets) {
		TicketHistoryResponse response = new TicketHistoryResponse();
		TicketHistoryData historyData = new TicketHistoryData();
		historyData.setTotal_count(tickets.size());

		List<TabDataForNTickets> tabDataList = new ArrayList<>();

		// Add tabs in specified sequence
		List<String> eventGroups = Arrays.asList("All", "Cash", "Communication", "Hardware Faults", "No Transactions",
				"Supervisory", "Others");

		eventGroups.forEach(groupName -> {
			List<AtmShortDetailsDto> filteredTickets;
			if ("All".equals(groupName)) {
				filteredTickets = tickets;
			} else {
				filteredTickets = tickets.stream().filter(t -> {
					if (t.getEventGroup() == null) {
						return "Others".equals(groupName); // Null event groups go to Others
					}
					switch (groupName) {
					case "Cash":
						return t.getEventGroup().contains("Cash") || t.getEventGroup().contains("cash");
					case "Communication":
						return t.getEventGroup().contains("Communication") || t.getEventGroup().contains("Network");
					case "Hardware Faults":
						return t.getEventGroup().contains("Hardware") || t.getEventGroup().contains("Device");
					case "No Transactions":
						return t.getEventGroup().contains("No Transactions")
								|| t.getEventGroup().contains("No Transaction");
					case "Supervisory":
						return t.getEventGroup().contains("Supervisory") || t.getEventGroup().contains("Admin");
					case "Others":
						// If doesn't match any other category, it goes to Others
						return !t.getEventGroup().contains("Cash") && !t.getEventGroup().contains("Communication")
								&& !t.getEventGroup().contains("Hardware")
								&& !t.getEventGroup().contains("No Transactions")
								&& !t.getEventGroup().contains("Supervisory");
					default:
						return false;
					}
				}).collect(Collectors.toList());
			}
			tabDataList.add(createTabData(groupName, filteredTickets));
		});

		historyData.setTab_data(tabDataList);
		response.setData(historyData);
		return response;
	}

	private TabDataForNTickets createTabData(String tabName, List<AtmShortDetailsDto> tickets) {
		TabDataForNTickets tabData = new TabDataForNTickets();
		tabData.setTab_name(tabName);
		tabData.setTab_count(tickets.size());
		tabData.setTicket_data(groupTicketsByDate_forNtickets(tickets));
		return tabData;
	}

	private List<DateGroupedTickets> groupTicketsByDate_forNtickets(List<AtmShortDetailsDto> tickets) {
		Map<String, List<AtmShortDetailsDto>> groupedTickets = tickets.stream()
				.collect(Collectors.groupingBy(this::getDateGroup));
		return groupedTickets.entrySet().stream().map(entry -> new DateGroupedTickets(entry.getKey(), entry.getValue()))
				//.sorted(this::compareDateGroups).collect(Collectors.toList());
				 .sorted((date1, date2) -> {
					 String d1 = date1.getDate(), d2 = date2.getDate();
					 	if(date1.getDate().startsWith("Today | "))
					 	{
					 		d1 = date1.getDate().replace("Today | ","");
					 		
					 	}
					 	
					 	if(date1.getDate().startsWith("Yesterday | "))
					 	{
					 		d1 = date1.getDate().replace("Yesterday | ","");
					 		
					 	}
					 	
					 	if(date2.getDate().startsWith("Today | "))
					 	{
					 		d2 = date2.getDate().replace("Today | ","");
					 		
					 	}
					 	if(date2.getDate().startsWith("Yesterday | "))
					 	{
					 		d2 = date2.getDate().replace("Yesterday | ","");
					 		
					 	}
					 	
					 	System.out.println("---format---"+d1+"----format"+d2);
					 	DateTimeFormatter dateGroupFormatter = DateTimeFormatter.ofPattern("dd MMM ''yy");
					 	
		                LocalDate dt1 = LocalDate.parse(d1, dateGroupFormatter);
		                LocalDate dt2 = LocalDate.parse(d2, dateGroupFormatter);
		                return dt2.compareTo(dt1); // Ascending order
		            }).collect(Collectors.toList());
	}

	private String getDateGroup(AtmShortDetailsDto ticket) {
		LocalDate ticketDate = parseTicketDateNew(ticket.getCloseDate());
		LocalDate today = LocalDate.now();

		if (ticketDate.equals(today)) {
			return "Today | " + formatDate(today);
		}
		LocalDate yesterday = LocalDate.now().minusDays(1);

		if (ticketDate.equals(yesterday)) {
			return "Yesterday | " + formatDate(yesterday);
		}

		return formatDate(ticketDate);
	}

	private LocalDate parseTicketDate(String dateStr) {
		try {
			System.out.println("----------date ---------"+dateStr);
			// 15 January 2024, 10:30 AM
			//String standardDate = format(dateTime, Patterns.STANDARD_12H);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM ''yy");
			return LocalDate.parse(dateStr, formatter);
		} catch (Exception e) {
			System.out.println("---------------------------------error-"+e);

			return LocalDate.now().minusDays(1);
		}
	}
	
	private LocalDate parseTicketDateNew(String dateStr) {
	    try {
	        // Strip off the time part before parsing
	        String dateOnly = dateStr.split(",")[0].trim();  // "21 July 2025"
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
	        return LocalDate.parse(dateOnly, formatter);
	    } catch (Exception e) {
	        System.out.println("Parsing error: " + e);
	        return LocalDate.now().minusDays(1);
	    }
	}


	private String formatDate1(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
	}

	private int compareDateGroups(DateGroupedTickets g1, DateGroupedTickets g2) {
		if (g1.getDate().startsWith("Today"))
			return -1;
		if (g2.getDate().startsWith("Today"))
			return 1;
		if (g1.getDate().startsWith("Last week"))
			return -1;
		if (g2.getDate().startsWith("Last week"))
			return 1;
		return g2.getDate().compareTo(g1.getDate());
	}

	// -----------------------------------------------------------------------------

	public AtmMtdUptimeDto getAtmUptime(String atmId) {
		return AtmMtdUptimeDto.builder().atmMtdUptime(synergyService.getAtmUptime(atmId).getMonthtotilldateuptime())
				.lastUpdatedMtdDateTime(DateUtil.formatPreviousDayTimestamp()).build();
	}

	private void addDocument(List<DocumentDto> documentDetails, String document, String documentName) {
		if (document != null && !document.isBlank())
			documentDetails.add(new DocumentDto(document, documentName));
	}

	public List<DocumentDto> getTicketUpdateDocuments(String userId, String atmId, String ticketNumber) {
		TicketUpdateDocument ticketUpdateDocument = ticketUpdateDocumentRepository.getTicketUpdateDocuments(userId,
				atmId, ticketNumber);
		log.info("ticketUpdateDocument:{}", ticketUpdateDocument);
		if (ticketUpdateDocument == null) {
			return Collections.emptyList();
		}
		List<DocumentDto> documentDetails = new ArrayList<>();

		addDocument(documentDetails, ticketUpdateDocument.getDocument(), ticketUpdateDocument.getDocumentName());
		addDocument(documentDetails, ticketUpdateDocument.getDocument1(), ticketUpdateDocument.getDocument1Name());
		addDocument(documentDetails, ticketUpdateDocument.getDocument2(), ticketUpdateDocument.getDocument2Name());
		addDocument(documentDetails, ticketUpdateDocument.getDocument3(), ticketUpdateDocument.getDocument3Name());
		addDocument(documentDetails, ticketUpdateDocument.getDocument4(), ticketUpdateDocument.getDocument4Name());

		return documentDetails;
	}

	public CeMachineUpDownCountDto getCeMachineUpDownCount(String username) {
		CeMachineUpDownCount ceMachineUpDownCount = ceUpDownCountRepo.getCeMachineUpDownCount(username);
		if (ceMachineUpDownCount == null) {
			return CeMachineUpDownCountDto.builder().downAtm(0).upAtm(0).totalAtm(0).build();

		}
		return CeMachineUpDownCountDto.builder().downAtm(ceMachineUpDownCount.getDownAtm())
				.upAtm(ceMachineUpDownCount.getUpAtm()).totalAtm(ceMachineUpDownCount.getTotalAtm()).build();
	}

}
