package com.hpy.ops360.atmservice.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpy.ops360.atmservice.dto.AssignedAtmDetailsDto;
import com.hpy.ops360.atmservice.dto.AssignedAtmDto;
import com.hpy.ops360.atmservice.dto.AssignedAtmMtdDto;
import com.hpy.ops360.atmservice.dto.AtmHistoryNTicketsResponse;
import com.hpy.ops360.atmservice.dto.AtmIdDto;
import com.hpy.ops360.atmservice.dto.AtmVendorDetailsWithDto;
import com.hpy.ops360.atmservice.dto.LoginService;
import com.hpy.ops360.atmservice.dto.NTicketHistory;
import com.hpy.ops360.atmservice.dto.SearchBankLocationDto;
import com.hpy.ops360.atmservice.dto.TicketHistoryDto;
import com.hpy.ops360.atmservice.entity.AtmDetails;
import com.hpy.ops360.atmservice.entity.AtmIndent;
import com.hpy.ops360.atmservice.entity.AtmIndentDetails;
import com.hpy.ops360.atmservice.entity.AtmTicketEvent;
import com.hpy.ops360.atmservice.entity.AtmUptimeDetailsHims;
import com.hpy.ops360.atmservice.entity.BroadCategory;
import com.hpy.ops360.atmservice.entity.CheckAtmSource;
import com.hpy.ops360.atmservice.entity.TicketDetailsHimsEntity;
import com.hpy.ops360.atmservice.entity.UserAssignedAtmDetails;
import com.hpy.ops360.atmservice.entity.UserAtmDetails;
import com.hpy.ops360.atmservice.repository.AtmDetailsRepoHims;
import com.hpy.ops360.atmservice.repository.AtmDetailsRepository;
import com.hpy.ops360.atmservice.repository.AtmDetailsRepositoryHims;
import com.hpy.ops360.atmservice.repository.AtmIndentDetailsRepository;
import com.hpy.ops360.atmservice.repository.AtmIndentRepository;
import com.hpy.ops360.atmservice.repository.AtmTicketEventRepository;
import com.hpy.ops360.atmservice.repository.AtmUptimeDetailsRepositoryHims;
import com.hpy.ops360.atmservice.repository.BroadCategoryRepository;
import com.hpy.ops360.atmservice.repository.CheckAtmSourceRepository;
import com.hpy.ops360.atmservice.response.AtmDetailsWithTickets;
import com.hpy.ops360.atmservice.response.OpenTicketsResponse;
import com.hpy.ops360.atmservice.response.TicketDetailsDto;
import com.hpy.ops360.atmservice.response.TicketDetailsDtoHims;
import com.hpy.ops360.atmservice.response.TicketDetailsDtoHims;
import com.hpy.ops360.atmservice.ticket.dto.TicketCategoryCountDto;
import com.hpy.ops360.atmservice.utils.DateUtil;
import com.hpy.ops360.atmservice.utils.DisableSslClass;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AtmDetailsService {

	private final SynergyService synergyService;

	private final UserAtmDetailsService userAtmDetailsService;

	private final LoginService loginService;

	private final int maxAtmCount;

	private final AtmTicketEventRepository atmTicketEventRepository;

	private final AtmIndentRepository atmIndentRepository;

	private final AtmIndentDetailsRepository atmIndentDetailsRepository;

	private final AtmDetailsRepository atmDetailsRepository;

	private final BroadCategoryRepository broadCategoryRepository;
	
	@Autowired
	private CheckAtmSourceRepository checkAtmSourceRepository;
	
	@Autowired
	private AtmDetailsRepositoryHims atmDetailsRepositoryHims;
	
	@Autowired
	private AtmDetailsRepoHims atmDetailsRepoHims;
	
	@Autowired
	private AtmUptimeDetailsRepositoryHims atmUptimeDetailsRepositoryHims;
	

	public AtmDetailsService(SynergyService synergyService, UserAtmDetailsService userAtmDetailsService,
			LoginService loginService, @Value("${ops360.dashboard.max-atm-list-count}") int maxAtmCount,
			AtmTicketEventRepository atmTicketEventRepository, AtmIndentRepository atmIndentRepository,
			AtmIndentDetailsRepository atmIndentDetailsRepository, AtmDetailsRepository atmDetailsRepository,
			BroadCategoryRepository broadCategoryRepository) {
		this.synergyService = synergyService;
		this.userAtmDetailsService = userAtmDetailsService;
		this.loginService = loginService;
		this.maxAtmCount = maxAtmCount;
		this.atmTicketEventRepository = atmTicketEventRepository;
		this.atmIndentRepository = atmIndentRepository;
		this.atmIndentDetailsRepository = atmIndentDetailsRepository;
		this.atmDetailsRepository = atmDetailsRepository;
		this.broadCategoryRepository = broadCategoryRepository;
	}

	public AtmVendorDetailsWithDto getAtmDetails() throws IOException {

		try (InputStreamReader reader = new InputStreamReader(
				new ClassPathResource("mock/atm_details.json").getInputStream())) {
			ObjectMapper mapper = new ObjectMapper();
			AtmVendorDetailsWithDto atmDetailsDtos = mapper.readValue(reader, AtmVendorDetailsWithDto.class);
			log.debug("readValue = " + atmDetailsDtos);
			return atmDetailsDtos;
		} catch (IOException e) {
			log.error("Failed to read ATM details from JSON file", e);
			throw e;
		}
	}

	public List<AssignedAtmDto> getAssignedAtmDetailList() {

		List<UserAssignedAtmDetails> assignedAtmDetails = userAtmDetailsService
				.getUserAssignedAtmDetails(loginService.getLoggedInUser());

		return assignedAtmDetails.stream().map(atm -> new AssignedAtmDto(atm.getAtmiId() == null ? "" : atm.getAtmiId(),
				atm.getBankname() == null ? "" : atm.getBankname(), atm.getLocation() == null ? "" : atm.getLocation(),
				atm.getGrade() == null ? "" : atm.getGrade())).toList();
	}

	public List<AtmIdDto> getAtmIdList() {
		List<UserAtmDetails> userAtmDetails = userAtmDetailsService.getUserAtmIds(loginService.getLoggedInUser());
		return userAtmDetails.stream().map(userAtmDetail -> new AtmIdDto(userAtmDetail.getAtmCode())).toList();
	}

	public List<AssignedAtmMtdDto> getAssignedAtmMtdDetailList() {

		List<UserAssignedAtmDetails> assignedAtmDetails = userAtmDetailsService
				.getUserAssignedAtmDetails(loginService.getLoggedInUser());

		return assignedAtmDetails.stream().map(atm -> new AssignedAtmMtdDto(atm.getAtmiId(), atm.getBankname(),
				atm.getLocation(), atm.getGrade(), atm.getMtdPerformance(), atm.getUptimeTrend(), atm.getTrendCash()))
				.toList();

	}

	public List<NTicketHistory> getNTicketHistoryBasedOnAtmId(TicketHistoryDto ticketHistoryReqDto) {

		AtmHistoryNTicketsResponse atmHistory = synergyService.getntickets(ticketHistoryReqDto);
		List<NTicketHistory> ticketHistory = new ArrayList<>();
		for (int i = 0; atmHistory.getTicketDetails() != null && i < atmHistory.getTicketDetails().size(); i++) {

			NTicketHistory nTicketHistory = new NTicketHistory(atmHistory.getTicketDetails().get(i).getEquipmentid(),
					atmHistory.getTicketDetails().get(i).getSrno(),
					getSelectedBroadCategory(atmHistory.getTicketDetails().get(i).getSubcalltype()),
					atmHistory.getTicketDetails().get(i).getEventcode(),
					atmHistory.getTicketDetails().get(i).getCalldate(),
					getDownTimeInHrs(atmHistory.getTicketDetails().get(i).getDowntimeinmins()),
					atmHistory.getTicketDetails().get(i).getSubcalltype());
			ticketHistory.add(nTicketHistory);
		}
		return ticketHistory;
	}


//	public List<NTicketHistory> getNTicketHistoryBasedOnAtmIdHims(TicketHistoryDto ticketHistoryReqDto, List<TicketDetailsHims> ticketDetailsData) {
//	    List<NTicketHistory> ticketHistory = new ArrayList<>();
//	    NTicketHistory nTicketHistory = new NTicketHistory();
//	    if (ticketDetailsData != null && !ticketDetailsData.isEmpty()) {
//	        for (TicketDetailsHims ticketDetail : ticketDetailsData) {
//	             nTicketHistory = new NTicketHistory(
//	                ticketDetail.getEquipmentId(),
//	                ticketDetail.getSrNo(), 
//	                getSelectedBroadCategory(ticketDetail.getSubCallType()), 
//	                ticketDetail.getEventCode(), 
//	                ticketDetail.getCallDate(),
//	                getDownTimeInHrs(ticketDetail.getDowntimeInMins()),
//	                ticketDetail.getSubCallType()
//	            );
//	            ticketHistory.add(nTicketHistory);
//	        }
//	    }
//	    return ticketHistory;
//	}
	
	public List<NTicketHistory> getNTicketHistoryBasedOnAtmIdHims(TicketHistoryDto ticketHistoryReqDto, List<TicketDetailsHimsEntity> ticketDetailsData) {
	    List<NTicketHistory> ticketHistory = new ArrayList<>();
	    
	    if (ticketDetailsData != null && !ticketDetailsData.isEmpty()) {
	        for (TicketDetailsHimsEntity ticketDetail : ticketDetailsData) {
	            NTicketHistory nTicketHistory = new NTicketHistory();
	            nTicketHistory.setId(null); 
	            nTicketHistory.setAtmId(ticketDetail.getEquipmentId()); 
	            nTicketHistory.setTicketNumber(ticketDetail.getSrNo()); 
	            nTicketHistory.setOwner(ticketDetail.getOwner()); 
	            nTicketHistory.setIssue(getSelectedBroadCategory(ticketDetail.getSubCallType())); 
	            nTicketHistory.setCalldate(DateUtil.formatFullTimestamp(ticketDetail.getCallDate()));
	            nTicketHistory.setDownTime(getDownTimeInHrs(ticketDetail.getDowntimeInMins()));
	            nTicketHistory.setSubcall(ticketDetail.getSubCallType());
	            nTicketHistory.setIssue(ticketDetail.getEventCode());
	            ticketHistory.add(nTicketHistory);
	        }
	    }
	    
	    return ticketHistory;
	}

	private String getDownTimeInHrs(int downtimeInMins) {
		return String.format("%d Hrs", downtimeInMins / 60);
	}

	private String getMaxDownTimeInHrs(String createdDate) {
		log.info("getDownTimeInHrs()|createdDate:{}", createdDate);
		DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a", Locale.US);
		LocalDateTime parsedCreatedDate = LocalDateTime.parse(createdDate, formatterInput);
		Duration duration = Duration.between(parsedCreatedDate, LocalDateTime.now());

		return String.format("%d Hrs", duration.toHours());
	}
	
	//not using N ticket history verify and remove logic
	public AssignedAtmDetailsDto getAtmDetails(String atmId) {
		DisableSslClass.disableSSLVerification();
		List<AtmIdDto> atmIds = new ArrayList<>();
		atmIds.add(new AtmIdDto(atmId));
		
		try {
			List<CheckAtmSource> atm = checkAtmSourceRepository.checkAtmSource(atmId);
			
			if (atm.isEmpty()) {
				log.error("Atm list with source is empty insert valid atm id");
				throw new IllegalArgumentException("Atm list with source is empty");
			}
			log.info("Atm list with source response: {}", atm);
			int source = atm.get(0).getSourceCode();
			if (source == 0) // synergy
			{
				
				OpenTicketsResponse openTicketsResponse = synergyService.getOpenTicketDetails(atmIds);

				if (openTicketsResponse.getAtmdetails().isEmpty()) {
					AtmDetails atmDetails = atmDetailsRepository.getAtmDetails(atmId);
					log.info("getAtmDetails| atmDetailsFromSp:{}", atmDetails);
					return new AssignedAtmDetailsDto(atmId, atmDetails.getAddress(),
							synergyService.getAtmUptime(atmId).getMonthtotilldateuptime(), "", "", 0, "",
							new TicketCategoryCountDto(0, 0, 0, 0, 0, 0), Collections.emptyList());
				}

				TicketHistoryDto ticketHistoryDto = new TicketHistoryDto(atmId, maxAtmCount);
				StringBuilder atmTicketEventCode = new StringBuilder();

				Map<String, TicketDetailsDto> ticketDetailsMap = new HashMap<>();
				List<TicketDetailsDto> ticketDetails = new ArrayList<>();
				for (AtmDetailsWithTickets atmdetails : openTicketsResponse.getAtmdetails()) {
					ticketDetails = atmdetails.getOpenticketdetails();

					log.info("AtmDetailsService|getAtmDetails|Desc order of open tickets", ticketDetails);

					for (TicketDetailsDto ticketDetailsDto : ticketDetails) {
						String uniqueCode = String.format("%s|%s|%s,", ticketDetailsDto.getEquipmentid(),
								ticketDetailsDto.getSrno(), ticketDetailsDto.getEventcode());
						atmTicketEventCode.append(uniqueCode);
						ticketDetailsMap.put(ticketDetailsDto.getEquipmentid() + ticketDetailsDto.getSrno(), ticketDetailsDto);
					}
				}

				log.info("ticketDetails:{}", ticketDetails);

				String atmTicketEventCodeList = atmTicketEventCode.toString();
				atmTicketEventCodeList = atmTicketEventCodeList.substring(0, atmTicketEventCodeList.length() - 1);
				log.info("atmTicketEventCode after trim:{}", atmTicketEventCodeList);
				List<AtmTicketEvent> atmTicketEventList = userAtmDetailsService
						.getAtmTicketEvent(loginService.getLoggedInUser(), atmTicketEventCodeList);
				log.info("atmTicketEventList:{}", atmTicketEventList);

				List<String> breakdownTicketIds = atmTicketEventList.stream().filter(event -> event.getIsBreakdown() == 1)
						.map(AtmTicketEvent::getTicketId).toList();
				log.info("breakdownTicketIds:{}", breakdownTicketIds);
				Integer isBreakDownCount = breakdownTicketIds.size();
				log.info("isBreakDownCount:{}", isBreakDownCount);

				if (breakdownTicketIds.isEmpty()) {
					return new AssignedAtmDetailsDto(openTicketsResponse.getAtmdetails().get(0).getAtmId(),
							openTicketsResponse.getAtmdetails().get(0).getOpenticketdetails().get(0).getAddress(),
							synergyService.getAtmUptime(atmId).getMonthtotilldateuptime(), "", "",
							isBreakDownCount != null ? isBreakDownCount : 0, "", new TicketCategoryCountDto(0, 0, 0, 0, 0, 0),
							getNTicketHistoryBasedOnAtmId(ticketHistoryDto));
				}

				TicketCategoryCountDto ticketCategoryCountDto = getEventCategoryCounts(
						atmTicketEventList.stream().filter(event -> event.getIsBreakdown() == 1).toList());
				log.info("ticketCategoryCountDto:{}", ticketCategoryCountDto);

				List<TicketDetailsDto> openBreakDownTickets = ticketDetails.stream()
						.filter(ticketDetail -> breakdownTicketIds.contains(ticketDetail.getSrno())).toList();

				log.info("openBreakDownTickets:{}", openBreakDownTickets);

				List<TicketDetailsDto> mutableList = new ArrayList<>(openBreakDownTickets);
				Collections.sort(mutableList, (t1, t2) -> {
					String date1 = DateUtil.formatDateString(t1.getCreateddate());
					String date2 = DateUtil.formatDateString(t2.getCreateddate());
					return date2.compareTo(date1);
				});
				TicketDetailsDto lastVisitedTicket = mutableList.get(0);
				TicketDetailsDto earlyTicket = mutableList.get(mutableList.size() - 1);

				return new AssignedAtmDetailsDto(openTicketsResponse.getAtmdetails().get(0).getAtmId(),
						openTicketsResponse.getAtmdetails().get(0).getOpenticketdetails().get(0).getAddress(),
						synergyService.getAtmUptime(atmId).getMonthtotilldateuptime(), lastVisitedTicket.getSrno(),
						DateUtil.formatDateString(lastVisitedTicket.getNextfollowup()),
						isBreakDownCount != null ? isBreakDownCount : 0, getMaxDownTimeInHrs(earlyTicket.getCreateddate()),
						ticketCategoryCountDto, getNTicketHistoryBasedOnAtmId(ticketHistoryDto));
				
			}else {
				List<AtmUptimeDetailsHims> atmUptimeDetails = atmUptimeDetailsRepositoryHims.findByAtmIdNative(atmId);
				List<TicketDetailsHimsEntity> ticketDetailsData = atmDetailsRepoHims.findByEquipmentIdNative(atmId);


				log.info("ATM Uptime Details: {}", atmUptimeDetails);
				log.info("Ticket Details: {}", ticketDetailsData);

				if (ticketDetailsData.isEmpty() || ticketDetailsData.get(0).getEquipmentId().isEmpty()) {
				    AtmDetails atmDetails = atmDetailsRepository.getAtmDetails(atmId);
				    log.info("ATM Details from SP: {}", atmDetails);
				    return new AssignedAtmDetailsDto(atmId, atmDetails.getAddress(),
				            atmUptimeDetails.isEmpty() ? 0 : atmUptimeDetails.get(0).getMonthToTillDateUptime(), "", "", 0, "",
				            new TicketCategoryCountDto(0, 0, 0, 0, 0, 0), Collections.emptyList());
				}

				TicketHistoryDto ticketHistoryDto = new TicketHistoryDto(atmId, maxAtmCount);
				StringBuilder atmTicketEventCode = new StringBuilder();
				Map<String, TicketDetailsDtoHims> ticketDetailsMap = new HashMap<>();
				List<TicketDetailsDtoHims> ticketDetailsList = new ArrayList<>();
				for (TicketDetailsHimsEntity ticketDetail : ticketDetailsData) {
				    log.info("Processing Ticket Detail: {}", ticketDetail);

				    TicketDetailsDtoHims ticketDetailsDto = new TicketDetailsDtoHims(

				    	    ticketDetail.getSrNo(), 
				    	    ticketDetail.getCustomer(), 
				    	    ticketDetail.getEquipmentId(),
				    	    ticketDetail.getModel(), 
				    	    ticketDetail.getAtmCategory(), 
				    	    ticketDetail.getAtmClassification(), 
				    	    DateUtil.formatFullTimestamp(ticketDetail.getCallDate()), 
				    	    formatDateField(ticketDetail.getCreatedDate()),
				    	    ticketDetail.getCallType(), 
				    	    ticketDetail.getSubCallType(), 
				    	    formatDateField(ticketDetail.getCompletionDateWithTime()), 
				    	    ticketDetail.getDowntimeInMins(), 
				    	    ticketDetail.getVendor(), 
				    	    ticketDetail.getServiceCode(), 
				    	    ticketDetail.getDiagnosis(), 
				    	    ticketDetail.getEventCode(), 
				    	    ticketDetail.getHelpdeskName(), 
				    	    formatDateField(ticketDetail.getLastAllocatedTime()),
				    	    ticketDetail.getLastComment(), 
				    	    formatDateField(ticketDetail.getLastActivity()), 
				    	    ticketDetail.getStatus(), 
				    	    ticketDetail.getSubStatus(), 
				    	    ticketDetail.getRo(), 
				    	    ticketDetail.getSite(), 
				    	    ticketDetail.getAddress(), 
				    	    ticketDetail.getCity(), 
				    	    ticketDetail.getLocationName(), 
				    	    ticketDetail.getState(), 
				    	    formatDateField(ticketDetail.getNextFollowUp()), 
				    	    formatDateField(ticketDetail.getEtaDateTime()), 
				    	    ticketDetail.getOwner(), 
				    	    ticketDetail.getCustomerRemark() 
				    	    
				    	);
				    	ticketDetailsList.add(ticketDetailsDto);	
				    
				    String uniqueCode = String.format("%s|%s|%s,", ticketDetailsDto.getEquipmentid(),
				            ticketDetailsDto.getSrno(), ticketDetailsDto.getEventcode());
				    atmTicketEventCode.append(uniqueCode);
				    ticketDetailsMap.put(ticketDetailsDto.getEquipmentid() + ticketDetailsDto.getSrno(), ticketDetailsDto);
				}

				if (atmTicketEventCode.length() > 0) {
				    atmTicketEventCode.setLength(atmTicketEventCode.length() - 1);
				}
				log.info("ATM Ticket Event Code after trim: {}", atmTicketEventCode);

				// Retrieve ATM ticket events
				List<AtmTicketEvent> atmTicketEventList = userAtmDetailsService
				        .getAtmTicketEvent(loginService.getLoggedInUser (), atmTicketEventCode.toString());
				log.info("ATM Ticket Event List: {}", atmTicketEventList);

				List<String> breakdownTicketIds = atmTicketEventList.stream()
				        .filter(event -> event.getIsBreakdown() == 1)
				        .map(AtmTicketEvent::getTicketId)
				        .toList();
				log.info("Breakdown Ticket IDs: {}", breakdownTicketIds);

				Integer isBreakDownCount = breakdownTicketIds.size();
				log.info("Is Breakdown Count: {}", isBreakDownCount);

				// Return AssignedAtmDetailsDto based on breakdown ticket IDs
				if (breakdownTicketIds.isEmpty()) {
				    return new AssignedAtmDetailsDto(ticketDetailsData.get(0).getEquipmentId(),
				            ticketDetailsData.get(0).getAddress(),
				            atmUptimeDetails.isEmpty() ? 0 : atmUptimeDetails.get(0).getMonthToTillDateUptime(), "", "",
				            isBreakDownCount != null ? isBreakDownCount : 0, "", new TicketCategoryCountDto(0, 0, 0, 0, 0, 0),
				            		getNTicketHistoryBasedOnAtmIdHims(ticketHistoryDto,ticketDetailsData));
				}
				
				TicketCategoryCountDto ticketCategoryCountDto = getEventCategoryCounts(
						atmTicketEventList.stream().filter(event -> event.getIsBreakdown() == 1).toList());
				log.info("ticketCategoryCountDto:{}", ticketCategoryCountDto);
				
				List<TicketDetailsDtoHims> openBreakDownTickets = ticketDetailsList.stream()
						.filter(ticketDetail -> breakdownTicketIds.contains(ticketDetail.getSrno())).toList();

				log.info("openBreakDownTickets:{}", openBreakDownTickets);

				List<TicketDetailsDtoHims> mutableList = new ArrayList<>(openBreakDownTickets);
				Collections.sort(mutableList, (t1, t2) -> {
					String date1 = DateUtil.formatDateString(t1.getCreateddate());
					String date2 = DateUtil.formatDateString(t2.getCreateddate());
					return date2.compareTo(date1);
				});
				TicketDetailsDtoHims lastVisitedTicket = mutableList.get(0);
				TicketDetailsDtoHims earlyTicket = mutableList.get(mutableList.size() - 1);
				
				log.info("Last Visited Ticket: {}", lastVisitedTicket);
				log.info("Early Ticket: {}", earlyTicket);

				return new AssignedAtmDetailsDto(ticketDetailsData.isEmpty() ? atmId : atmId,
						ticketDetailsData.isEmpty() ? "" : ticketDetailsData.get(0).getAddress(),
						atmUptimeDetails.isEmpty() ? 0 : atmUptimeDetails.get(0).getMonthToTillDateUptime(), lastVisitedTicket.getSrno(),
						DateUtil.formatDateString(lastVisitedTicket.getNextfollowup()),
						isBreakDownCount != null ? isBreakDownCount : 0, getMaxDownTimeInHrs(earlyTicket.getCreateddate()),
						ticketCategoryCountDto, getNTicketHistoryBasedOnAtmIdHims(ticketHistoryDto,ticketDetailsData));
				
			}
			
		}catch (Exception e) {
			log.error("Unexpected error in getTicketDetailsByNumber", e);
			throw new RuntimeException("Internal processing error", e);
		}

	}
	
	private String formatDateField(Date date) {
	    return date != null ? DateUtil.formatDateStringIndexOfATM(date.toString()) : "";
	}

//	// Alternative helper method if dates need special handling
//	private String robustDateFormat(Date date) {
//	    if (date == null) return "";
//	    try {
//	        return DateUtil.formatDateStringIndexOfATM(date.toString());
//	    } catch (Exception e) {
//	        log.error("Error formatting date", e);
//	        return "";
//	    }
//	}

	private String formatDateField(Object date) {
	    if (date instanceof Date) {
	        return DateUtil.formatDateStringIndexOfATM(date.toString());
	    } else if (date instanceof String) {
	        return DateUtil.formatDateStringIndexOfATM((String) date);
	    }
	    return ""; // Return empty string for null or unsupported types
	}
	
	private TicketCategoryCountDto getEventCategoryCounts(List<AtmTicketEvent> atmTicketEventList) {
		log.info("getEventCategoryCounts()|atmTicketEventList:{}", atmTicketEventList);
		int cash = 0;
		int communication = 0;
		int hardwareFault = 0;
		int supervisory = 0;
		int others = 0;
		int total = 0;

		for (AtmTicketEvent atmTicketEvent : atmTicketEventList) {
			String eventGroup = atmTicketEvent.getEventGroup();
			if ("Cash".equalsIgnoreCase(eventGroup)) {
				cash++;
			} else if ("Communication".equalsIgnoreCase(eventGroup)) {
				communication++;
			} else if ("Hardware Fault".equalsIgnoreCase(eventGroup)) {
				hardwareFault++;
			} else if ("Supervisory".equalsIgnoreCase(eventGroup)) {
				supervisory++;
			} else if ("Others".equalsIgnoreCase(eventGroup)) {
				others++;
			}
		}

		total = atmTicketEventList.size();

		return TicketCategoryCountDto.builder().total(total).cash(cash).communication(communication)
				.hardwareFault(hardwareFault).supervisory(supervisory).others(others).build();
	}

	public List<SearchBankLocationDto> getSearchBankLocationList() {
		return userAtmDetailsService.getUserAssignedAtmDetails(loginService.getLoggedInUser()).stream()
				.map(e -> new SearchBankLocationDto(e.getBankname() == null ? "" : e.getBankname(),
						e.getLocation() == null ? "" : e.getLocation()))
				.toList();

	}

	public List<SearchBankLocationDto> getSearchLocationBankList() {
		return userAtmDetailsService.getUserAssignedAtmDetails(loginService.getLoggedInUser()).stream()
				.map(e -> new SearchBankLocationDto(e.getBankname() == null ? "" : e.getBankname(),
						e.getLocation() == null ? "" : e.getLocation()))
				.toList();

	}

	public List<AtmIndent> getAtmIndentList() {

		return atmIndentRepository.getAtmIndentList(loginService.getLoggedInUser());

	}

	public List<AtmIndentDetails> getAtmIndentDetails(String atmCode) {
		return atmIndentDetailsRepository.getAtmIndentDetails(loginService.getLoggedInUser(), atmCode);
	}

	public String getSelectedBroadCategory(String subcallType) {
		if (subcallType.isEmpty() || subcallType == null) {
			return "";
		}
		List<BroadCategory> broadCategory = broadCategoryRepository.getBroadCategory(loginService.getLoggedInUser(),
				subcallType);
		String selectedCategory = broadCategory.get(0).getCurrentValue();
		return selectedCategory;
	}

}
