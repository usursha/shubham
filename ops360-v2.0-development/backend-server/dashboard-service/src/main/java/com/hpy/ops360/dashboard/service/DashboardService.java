package com.hpy.ops360.dashboard.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpy.ops360.dashboard.config.DisableSslClass;
import com.hpy.ops360.dashboard.dto.AtmDetailsDto;
import com.hpy.ops360.dashboard.dto.AtmDetailsRequest;
import com.hpy.ops360.dashboard.dto.AtmDetailsWithSourceDto;
import com.hpy.ops360.dashboard.dto.AtmDetailsWithTickets;
import com.hpy.ops360.dashboard.dto.AtmMtdUptimeDto;
import com.hpy.ops360.dashboard.dto.AtmShortDetailsDto;
import com.hpy.ops360.dashboard.dto.AtmStatusDto;
import com.hpy.ops360.dashboard.dto.AtmStatusDtoForCm;
import com.hpy.ops360.dashboard.dto.AtmUpDownCountDto;
import com.hpy.ops360.dashboard.dto.AtmUptimeDetailsResp;
import com.hpy.ops360.dashboard.dto.AtmUptimeDto;
import com.hpy.ops360.dashboard.dto.CEAtmDetailsDtoFor_Cm;
import com.hpy.ops360.dashboard.dto.CMPortalDashboardDto;
import com.hpy.ops360.dashboard.dto.CMPortalDashboardDto_AtmStatusDto;
import com.hpy.ops360.dashboard.dto.CeAtmUptimeDto;
import com.hpy.ops360.dashboard.dto.DashboardDataDto;
import com.hpy.ops360.dashboard.dto.DashboardFlagDto;
import com.hpy.ops360.dashboard.dto.GetCmPortalATMScreenDataDto;
import com.hpy.ops360.dashboard.dto.NotificationDto;
import com.hpy.ops360.dashboard.dto.NumberOfCeDto;
import com.hpy.ops360.dashboard.dto.OpenTicketsRequest;
import com.hpy.ops360.dashboard.dto.OpenTicketsRequest_cm;
import com.hpy.ops360.dashboard.dto.OpenTicketsResponse;
import com.hpy.ops360.dashboard.dto.SynergyLoginRequest;
import com.hpy.ops360.dashboard.dto.SynergyLoginResponse;
import com.hpy.ops360.dashboard.dto.TargetMtdSummaryDto;
import com.hpy.ops360.dashboard.dto.TicketDetailsDto;
import com.hpy.ops360.dashboard.dto.TicketsRaisedCountDTO;
import com.hpy.ops360.dashboard.dto.TicketsRaisedCountResponseDto;
import com.hpy.ops360.dashboard.dto.UsersAtmDetailsDto;
import com.hpy.ops360.dashboard.entity.AtmTicketEvent;
import com.hpy.ops360.dashboard.entity.AtmUpDownCount;
import com.hpy.ops360.dashboard.entity.AtmWithSource;
import com.hpy.ops360.dashboard.entity.CmMtdUptime;
import com.hpy.ops360.dashboard.entity.OpenTicketHimsView;
import com.hpy.ops360.dashboard.entity.TicketsRaisedCount;
import com.hpy.ops360.dashboard.entity.UserAtmDetails;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.repository.AtmTicketEventRepository;
import com.hpy.ops360.dashboard.repository.AtmUpDownCountRepository;
import com.hpy.ops360.dashboard.repository.AtmWithSourceRepository;
import com.hpy.ops360.dashboard.repository.CmMtdUptimeRepository;
import com.hpy.ops360.dashboard.repository.OpenTicketHimsViewRepository;
import com.hpy.ops360.dashboard.repository.TicketRaisedCountRepository;
import com.hpy.ops360.dashboard.util.CustomDateFormattor;
import com.hpy.ops360.dashboard.util.CustomDateFormattor.FormatStyle;
import com.hpy.ops360.dashboard.util.DateUtil;
import com.hpy.ops360.dashboard.util.Helper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DashboardService {

	private int maxAtmListCount;

	@Value("${synergy.base-url}")
	private String synergyBaseUrl;

	private String username;

	private String password;

	private RestTemplate restTemplate;

	@Autowired
	private CEAtmDetailsServiceFor_Cm atmDetailsServiceFor_Cm;

	@Autowired
	private ProducerTemplate producerTemplate;

	private Helper helper;

	private UserAtmDetailsService userAtmDetailsService;

	private CmSynopsisService cmSynopsisService;

	private NumberOfCeService numberOfCeService;

	private AtmTicketEventRepository atmTicketEventRepository;

	private TicketRaisedCountRepository ticketRaisedCountRepository;

	private AtmDetailsService atmDetailsService;

	private SiteVisitService siteVisitService;

	private MtdUptimeService mtdUptimeService;

	@Autowired
	private CmMtdUptimeRepository cmMtdUptimeRepository;

	@Autowired
	private AtmUpDownCountRepository atmUpDownCountRepository;

	int batchSize;
	
	private AtmWithSourceRepository atmWithSourceRepository;
	
	private OpenTicketHimsViewRepository openTicketHimsViewRepository;

	public DashboardService(RestTemplate restTemplate, @Value("${synergy.base-url}") String synergyBaseUrl,
			@Value("${synergy.username}") String username, @Value("${synergy.password}") String password,
			@Value("${ops360.dashboard.max-atm-list-count}") Integer maxAtmListCount, Helper helper,
			UserAtmDetailsService userAtmDetailsService, CmSynopsisService cmSynopsisService,
			NumberOfCeService numberOfCeService, AtmTicketEventRepository atmTicketEventRepository,
			TicketRaisedCountRepository ticketRaisedCountRepository, SiteVisitService siteVisitService,
			AtmDetailsService atmDetailsService, CEAtmDetailsServiceFor_Cm atmDetailsServiceFor_Cm,
			CEAtmDetailsServiceFor_Cm atmDetailsServiceFor_Cm2, MtdUptimeService mtdUptimeService,
			@Value("${ops360.dashboard.batch-size}") int batchSize,AtmWithSourceRepository atmWithSourceRepository,OpenTicketHimsViewRepository openTicketHimsViewRepository) {
//			@Value("${ops360.executor.poolSize}") int poolSize) {

		this.restTemplate = restTemplate;
		this.synergyBaseUrl = synergyBaseUrl;
		this.username = username;
		this.password = password;
		this.atmDetailsServiceFor_Cm = atmDetailsServiceFor_Cm2;
		this.helper = helper;
		this.maxAtmListCount = maxAtmListCount;
		this.userAtmDetailsService = userAtmDetailsService;
		this.cmSynopsisService = cmSynopsisService;
		this.numberOfCeService = numberOfCeService;
		this.atmTicketEventRepository = atmTicketEventRepository;
		this.ticketRaisedCountRepository = ticketRaisedCountRepository;
		this.siteVisitService = siteVisitService;
		this.atmDetailsService = atmDetailsService;
		this.mtdUptimeService = mtdUptimeService;
		this.batchSize = batchSize;
		this.atmWithSourceRepository= atmWithSourceRepository;
		this.openTicketHimsViewRepository = openTicketHimsViewRepository;
//		this.poolSize = poolSize;
	}


	@Loggable
	public SynergyLoginResponse getSynergyRequestId() {
		String url = synergyBaseUrl + "/trequest";
		return restTemplate.postForObject(url, new SynergyLoginRequest(username, password), SynergyLoginResponse.class);
	}

	@Loggable
	public OpenTicketsResponse getOpenTicketDetails(List<AtmDetailsDto> atms) {
		long startLoginTime = System.currentTimeMillis();
		SynergyLoginResponse synergyLoginResponse = new SynergyLoginResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());
		log.debug("Time for execution synergy login: {} seconds", (System.currentTimeMillis() - startLoginTime) / 1000);
		String url = synergyBaseUrl + "/getopentickets";
		log.debug("getopenTicketsUrl: {}", url);
		log.debug("atms: {}", atms.toString());
		log.debug("atm size:{}", atms.size());
		long startSynOpenTicketsTime = System.currentTimeMillis();
		OpenTicketsRequest synergyRequest = new OpenTicketsRequest(synergyLoginResponse.getRequestid(), atms);
		log.info("synergyRequest for opentickets:{}", synergyRequest);
		ResponseEntity<OpenTicketsResponse> responseEntity = restTemplate.postForEntity(url, synergyRequest,
				OpenTicketsResponse.class);
		log.info("OpenTicketsResponse:{}", responseEntity.getBody());
		log.debug("Time for execution synergy open tickets: {} seconds",
				(System.currentTimeMillis() - startSynOpenTicketsTime) / 1000);

		// return responseEntity.getBody();
		OpenTicketsResponse response = responseEntity.getBody();
		producerTemplate.sendBody("direct:getCeOpenTicketDetails", response);

//		log.info("OpenTicketsResponse: {}", response);
		return response;
	}

	
	public OpenTicketsResponse getOpenTicketDetailsForUpDwonAtm(List<AtmDetailsDto> atms) {
		long startLoginTime = System.currentTimeMillis();
		SynergyLoginResponse synergyLoginResponse = new SynergyLoginResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());
		log.debug("Time for execution synergy login: {} seconds", (System.currentTimeMillis() - startLoginTime) / 1000);
		String url = synergyBaseUrl + "/getopentickets";
		log.debug("getopenTicketsUrl: {}", url);
		log.debug("atms: {}", atms.toString());
		log.debug("atm size:{}", atms.size());
		long startSynOpenTicketsTime = System.currentTimeMillis();
		OpenTicketsRequest synergyRequest = new OpenTicketsRequest(synergyLoginResponse.getRequestid(), atms);
		log.info("synergyRequest for opentickets:{}", synergyRequest);
		ResponseEntity<OpenTicketsResponse> responseEntity = restTemplate.postForEntity(url, synergyRequest,
				OpenTicketsResponse.class);
		log.info("OpenTicketsResponse:{}", responseEntity.getBody());
		log.debug("Time for execution synergy open tickets: {} seconds",
				(System.currentTimeMillis() - startSynOpenTicketsTime) / 1000);

		// return responseEntity.getBody();
		OpenTicketsResponse response = responseEntity.getBody();
		//producerTemplate.sendBody("direct:getCeOpenTicketDetails", response);

		return response;
	}
	
	@Loggable
	public DashboardFlagDto getDashboardRefreshFlag(int minutesToSubtract) {
		log.info("getDashboardRefreshFlag()|minutesToSubtract:", minutesToSubtract);

		List<UsersAtmDetailsDto> userAtmDetails = userAtmDetailsService.getUserAtmDetails(helper.getLoggedInUser());

		List<AtmDetailsDto> atmIdDtoList = new ArrayList<>();

		for (int i = 0; i < userAtmDetails.size(); i++) {
			atmIdDtoList.add(new AtmDetailsDto(userAtmDetails.get(i).getAtm_code()));

		}
		log.info("----------------------atmIdDtoList:{}", atmIdDtoList.get(0));
		log.info("Atm List Size:{}", atmIdDtoList.size());

		OpenTicketsResponse openTicketsResponse = getOpenTicketDetails(atmIdDtoList); // get open tickets from synergy

		for (int i = 0; openTicketsResponse.getAtmdetails() != null
				&& i < openTicketsResponse.getAtmdetails().size(); i++) {

			for (int j = 0; j < openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails().size(); j++) {
				TicketDetailsDto ticketDetailsDto = openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails()
						.get(j);

				if (ticketDetailsDto.getLastactivity().isEmpty()) {
					continue;
				}

				LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(minutesToSubtract);
				DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("M/dd/yyyy h:mm:ss a", Locale.US);
				LocalDateTime parsedLastActivity = LocalDateTime.parse(ticketDetailsDto.getLastactivity(),
						formatterInput);
				log.info("getDashboardRefreshFlag()|parsedLastActivity:{} ", parsedLastActivity);
				int comparisonResult = parsedLastActivity.compareTo(fiveMinutesAgo);
				if (comparisonResult >= 0) {
					return new DashboardFlagDto(1);
				}

				if (openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails().size() > maxAtmListCount) {
					break;
				}
			}
			if (openTicketsResponse.getAtmdetails().size() > maxAtmListCount) {
				break;
			}
		}

		return new DashboardFlagDto(0);

	}
//--------------------original-----------------------------------------

	@Loggable
	public String insertOpenTicketList(List<AtmDetailsDto> atms) {
		OpenTicketsResponse openTicketsResponse = getOpenTicketDetailsInBatches(atms, batchSize);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String jsonData = objectMapper.writeValueAsString(openTicketsResponse);
			log.info("jsonData:{}", jsonData);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "Bulk insert complete";
	}

	@Loggable
	public List<AtmShortDetailsDto> getOpenTicketList(List<AtmDetailsDto> atms, int maxLimit) {
		OpenTicketsResponse openTicketsResponse = getOpenTicketDetailsInBatches(atms, batchSize);
		List<AtmShortDetailsDto> timedOutList = new ArrayList<>();

		if (openTicketsResponse.getAtmdetails() == null) {
			return Collections.emptyList();
		}

		StringBuilder atmTicketEventCode = new StringBuilder();
		Map<String, TicketDetailsDto> ticketDetailsMap = new HashMap<>();

		for (int i = 0; openTicketsResponse.getAtmdetails() != null && i < openTicketsResponse.getAtmdetails().size()
				&& !openTicketsResponse.getAtmdetails().isEmpty(); i++) {
			for (int j = 0; j < openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails().size(); j++) {
				TicketDetailsDto ticketDetailsDto = openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails()
						.get(j);

				String uniqueCode = String.format("%s|%s|%s|%s|%s,", ticketDetailsDto.getEquipmentid(),
						ticketDetailsDto.getSrno(), ticketDetailsDto.getEventcode(), ticketDetailsDto.getNextfollowup(),
						ticketDetailsDto.getCalldate());
				// Log the calldate
//                log.info("Call Date::++++++++++++++++++++++++++++  {}", ticketDetailsDto.getCalldate());
				atmTicketEventCode.append(uniqueCode);
				ticketDetailsMap.put(ticketDetailsDto.getEquipmentid() + ticketDetailsDto.getSrno(), ticketDetailsDto);

				if (openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails().size() > maxLimit) {
					break;
				}
			}
			if (openTicketsResponse.getAtmdetails().size() > maxLimit) {
				break;
			}
		}

		String atmTicketEventCodeList = atmTicketEventCode.toString();

		atmTicketEventCodeList = atmTicketEventCodeList.substring(0, atmTicketEventCodeList.length() - 1);

		log.info("atmTicketEventCode:{}", atmTicketEventCodeList);

		log.info("atmTicketEventCode:{}", atmTicketEventCodeList);
		List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository.getAtmTicketEvent(helper.getLoggedInUser(),
				atmTicketEventCodeList);
		log.info("getOpenTicketList()|From SP|atmTicketEventList:{}", atmTicketEventList);
		for (AtmTicketEvent atmTicketEvent : atmTicketEventList) {
			TicketDetailsDto ticketDetailsDto = ticketDetailsMap
					.get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());

			AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
					.requestid(openTicketsResponse.getRequestid().toString()).atmId(ticketDetailsDto.getEquipmentid())
					.ticketNumber(ticketDetailsDto.getSrno()).bank(ticketDetailsDto.getCustomer())
					.siteName(ticketDetailsDto.getEquipmentid())
					.owner(atmTicketEvent.getOwner().isEmpty() ? "" : atmTicketEvent.getOwner())
					.subcall(ticketDetailsDto.getSubcalltype()).vendor(ticketDetailsDto.getVendor())
					.error(atmTicketEvent.getEventCode() == null ? "default" : atmTicketEvent.getEventCode())
					.downTime(getDownTimeInHrs(ticketDetailsDto.getCreateddate()))
					.priorityScore(atmTicketEvent.getPriorityScore()).eventGroup(atmTicketEvent.getEventGroup())
					.isBreakdown(atmTicketEvent.getIsBreakdown() == null ? 0 : atmTicketEvent.getIsBreakdown())
					.isUpdated(atmTicketEvent.getIsUpdated() == null ? 0 : atmTicketEvent.getIsUpdated())
					.isTimedOut(atmTicketEvent.getIsTimedOut() == null ? 0 : atmTicketEvent.getIsTimedOut())
					.isTravelling(atmTicketEvent.getIsTravelling() == null ? 0 : atmTicketEvent.getIsTravelling())
					.travelTime(atmTicketEvent.getTravelTime()).travelEta(atmTicketEvent.getTravelEta())
					.downCall(atmTicketEvent.getDownCall() == null ? 0 : atmTicketEvent.getDownCall())
					.etaDateTime((atmTicketEvent.getFormattedEtaDateTime() == null ? ""
							: atmTicketEvent.getFormattedEtaDateTime()))
					// .etaDateTime(DateUtil.formatDateString(atmTicketEvent.getEtaDateTime() ==
					// null ? "" : atmTicketEvent.getEtaDateTime()))
					// .etaDateTime(atmTicketEvent.getEtaDateTime() == null ? "" :
					// atmTicketEvent.getEtaDateTime())
					.etaTimeout(atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout())
					// .CreatedDate(ticketDetailsDto.getCalldate()==null?"":ticketDetailsDto.getCalldate())
					.CreatedDate(DateUtil.formatDateString(
							ticketDetailsDto.getCreateddate() == null ? "" : ticketDetailsDto.getCreateddate()))
					.flagStatus(atmTicketEvent.getFlagStatus())
					.flagStatusInsertTime(atmTicketEvent.getFlagStatusInsertTime())
					.updateEnable(
							updateEnableStatus(atmTicketEvent.getSubcall().isEmpty() ? ticketDetailsDto.getSubcalltype()
									: atmTicketEvent.getSubcall()))
					.build();

			log.info("getOpenTicketList()|atmShortDetailsDto:{}", atmShortDetailsDto);

			timedOutList.add(atmShortDetailsDto);
		}

		return timedOutList.stream().filter(e -> e.getDownCall() == 1)
				.sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()).toList();

	}
	
	@Loggable
	public List<AtmShortDetailsDto> getOpenTicketListWithSource(List<AtmDetailsWithSourceDto> atmIdDtoList, int maxLimit) {
		OpenTicketsResponse openTicketsResponse = getOpenTicketDetailsInBatchesWithSource(atmIdDtoList, batchSize);
		if (openTicketsResponse.getAtmdetails().isEmpty()) {
			return Collections.emptyList();
		}
		List<AtmShortDetailsDto> timedOutList = new ArrayList<>();

		if (openTicketsResponse.getAtmdetails() == null) {
			return Collections.emptyList();
		}

		StringBuilder atmTicketEventCode = new StringBuilder();
		Map<String, TicketDetailsDto> ticketDetailsMap = new HashMap<>();

		for (int i = 0; openTicketsResponse.getAtmdetails() != null && i < openTicketsResponse.getAtmdetails().size()
				&& !openTicketsResponse.getAtmdetails().isEmpty(); i++) {
			for (int j = 0; j < openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails().size(); j++) {
				TicketDetailsDto ticketDetailsDto = openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails()
						.get(j);

				String uniqueCode = String.format("%s|%s|%s|%s|%s,", ticketDetailsDto.getEquipmentid(),
						ticketDetailsDto.getSrno(), ticketDetailsDto.getEventcode(), ticketDetailsDto.getNextfollowup(),
						ticketDetailsDto.getCalldate());
				// Log the calldate
//                log.info("Call Date::++++++++++++++++++++++++++++  {}", ticketDetailsDto.getCalldate());
				atmTicketEventCode.append(uniqueCode);
				ticketDetailsMap.put(ticketDetailsDto.getEquipmentid() + ticketDetailsDto.getSrno(), ticketDetailsDto);

				if (openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails().size() > maxLimit) {
					break;
				}
			}
			if (openTicketsResponse.getAtmdetails().size() > maxLimit) {
				break;
			}
		}

		String atmTicketEventCodeList = atmTicketEventCode.toString();

		atmTicketEventCodeList = atmTicketEventCodeList.substring(0, atmTicketEventCodeList.length() - 1);

		log.info("atmTicketEventCode:{}", atmTicketEventCodeList);
		List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository.getAtmTicketEvent(helper.getLoggedInUser(),
				atmTicketEventCodeList);
		log.info("getOpenTicketList()|From SP|atmTicketEventList:{}", atmTicketEventList);
		for (AtmTicketEvent atmTicketEvent : atmTicketEventList) {
			TicketDetailsDto ticketDetailsDto = ticketDetailsMap
					.get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());

			AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
					.requestid(openTicketsResponse.getRequestid().toString()).atmId(ticketDetailsDto.getEquipmentid())
					.ticketNumber(ticketDetailsDto.getSrno()).bank(ticketDetailsDto.getCustomer())
					.siteName(ticketDetailsDto.getEquipmentid())
					.owner(atmTicketEvent.getOwner().isEmpty() ? "" : atmTicketEvent.getOwner())
					.subcall(ticketDetailsDto.getSubcalltype() == null ? "":ticketDetailsDto.getSubcalltype()).vendor(ticketDetailsDto.getVendor())
					.error(atmTicketEvent.getEventCode() == null ? "default" : atmTicketEvent.getEventCode())
					.downTime(getDownTimeInHrs(ticketDetailsDto.getCreateddate()))
					.priorityScore(atmTicketEvent.getPriorityScore()).eventGroup(atmTicketEvent.getEventGroup())
					.isBreakdown(atmTicketEvent.getIsBreakdown() == null ? 0 : atmTicketEvent.getIsBreakdown())
					.isUpdated(atmTicketEvent.getIsUpdated() == null ? 0 : atmTicketEvent.getIsUpdated())
					.isTimedOut(atmTicketEvent.getIsTimedOut() == null ? 0 : atmTicketEvent.getIsTimedOut())
					.isTravelling(atmTicketEvent.getIsTravelling() == null ? 0 : atmTicketEvent.getIsTravelling())
					.travelTime(atmTicketEvent.getTravelTime()).travelEta(atmTicketEvent.getTravelEta())
					.downCall(atmTicketEvent.getDownCall() == null ? 0 : atmTicketEvent.getDownCall())
					.etaDateTime((atmTicketEvent.getFormattedEtaDateTime() == null ? ""
							: atmTicketEvent.getFormattedEtaDateTime()))
					// .etaDateTime(DateUtil.formatDateString(atmTicketEvent.getEtaDateTime() ==
					// null ? "" : atmTicketEvent.getEtaDateTime()))
					// .etaDateTime(atmTicketEvent.getEtaDateTime() == null ? "" :
					// atmTicketEvent.getEtaDateTime())
					.etaTimeout(atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout())
					// .CreatedDate(ticketDetailsDto.getCalldate()==null?"":ticketDetailsDto.getCalldate())
					.CreatedDate(CustomDateFormattor.convert(ticketDetailsDto.getCreateddate(), FormatStyle.HIMS_DATABASE))
					.flagStatus(atmTicketEvent.getFlagStatus())
					.flagStatusInsertTime(atmTicketEvent.getFlagStatusInsertTime())
					.updateEnable(
							updateEnableStatus(isNullOrEmpty(atmTicketEvent.getSubcall()) 
								    ? ticketDetailsDto.getSubcalltype() 
								    	    : atmTicketEvent.getSubcall()))
					.build();

			log.info("getOpenTicketList()|atmShortDetailsDto:{}", atmShortDetailsDto);

			timedOutList.add(atmShortDetailsDto);
		}

		return timedOutList.stream().filter(e -> e.getDownCall() == 1)
				.sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()).toList();

	}
	
	private boolean isNullOrEmpty(String str) {
	    return str == null || str.trim().isEmpty();
	}

	@Loggable
	private boolean updateEnableStatus(String subcallType) {
	    // Handle null/empty case first
	    if (subcallType == null || subcallType.isEmpty()) {
	        return true;
	    }
	    
	    // Return false only for specific disabled statuses
	    return !(subcallType.equals("BLM") || 
	             subcallType.equals("HD;Pending") || 
	             subcallType.equals("HD;Pending;"));
	}

	
	
	public List<AtmShortDetailsDto> getOpenTicketListForUpDownAtm(List<AtmDetailsDto> atms, int maxLimit) {
		OpenTicketsResponse openTicketsResponse = getOpenTicketDetailsInBatchesForUpDownAtm(atms, batchSize);
		List<AtmShortDetailsDto> timedOutList = new ArrayList<>();

		if (openTicketsResponse.getAtmdetails() == null) {
			return Collections.emptyList();
		}

		StringBuilder atmTicketEventCode = new StringBuilder();
		Map<String, TicketDetailsDto> ticketDetailsMap = new HashMap<>();

		for (int i = 0; openTicketsResponse.getAtmdetails() != null && i < openTicketsResponse.getAtmdetails().size()
				&& !openTicketsResponse.getAtmdetails().isEmpty(); i++) {
			for (int j = 0; j < openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails().size(); j++) {
				TicketDetailsDto ticketDetailsDto = openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails()
						.get(j);

				String uniqueCode = String.format("%s|%s|%s|%s|%s,", ticketDetailsDto.getEquipmentid(),
						ticketDetailsDto.getSrno(), ticketDetailsDto.getEventcode(), ticketDetailsDto.getNextfollowup(),
						ticketDetailsDto.getCalldate());
				atmTicketEventCode.append(uniqueCode);
				ticketDetailsMap.put(ticketDetailsDto.getEquipmentid() + ticketDetailsDto.getSrno(), ticketDetailsDto);

				if (openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails().size() > maxLimit) {
					break;
				}
			}
			if (openTicketsResponse.getAtmdetails().size() > maxLimit) {
				break;
			}
		}

		String atmTicketEventCodeList = atmTicketEventCode.toString();

		atmTicketEventCodeList = atmTicketEventCodeList.substring(0, atmTicketEventCodeList.length() - 1);

		log.info("atmTicketEventCode:{}", atmTicketEventCodeList);

		log.info("atmTicketEventCode:{}", atmTicketEventCodeList);
		List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository.getAtmTicketEvent(helper.getLoggedInUser(),
				atmTicketEventCodeList);
		log.info("getOpenTicketList()|From SP|atmTicketEventList:{}", atmTicketEventList);
		for (AtmTicketEvent atmTicketEvent : atmTicketEventList) {
			TicketDetailsDto ticketDetailsDto = ticketDetailsMap
					.get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());

			AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
					.requestid(openTicketsResponse.getRequestid().toString()).atmId(ticketDetailsDto.getEquipmentid())
					.ticketNumber(ticketDetailsDto.getSrno()).bank(ticketDetailsDto.getCustomer())
					.siteName(ticketDetailsDto.getEquipmentid())
					.owner(atmTicketEvent.getOwner().isEmpty() ? "" : atmTicketEvent.getOwner())
					.subcall(ticketDetailsDto.getSubcalltype()).vendor(ticketDetailsDto.getVendor())
					.error(atmTicketEvent.getEventCode() == null ? "default" : atmTicketEvent.getEventCode())
					.downTime(getDownTimeInHrs(ticketDetailsDto.getCreateddate()))
					.priorityScore(atmTicketEvent.getPriorityScore()).eventGroup(atmTicketEvent.getEventGroup())
					.isBreakdown(atmTicketEvent.getIsBreakdown() == null ? 0 : atmTicketEvent.getIsBreakdown())
					.isUpdated(atmTicketEvent.getIsUpdated() == null ? 0 : atmTicketEvent.getIsUpdated())
					.isTimedOut(atmTicketEvent.getIsTimedOut() == null ? 0 : atmTicketEvent.getIsTimedOut())
					.isTravelling(atmTicketEvent.getIsTravelling() == null ? 0 : atmTicketEvent.getIsTravelling())
					.travelTime(atmTicketEvent.getTravelTime()).travelEta(atmTicketEvent.getTravelEta())
					.downCall(atmTicketEvent.getDownCall() == null ? 0 : atmTicketEvent.getDownCall())
					.etaDateTime((atmTicketEvent.getFormattedEtaDateTime() == null ? ""
							: atmTicketEvent.getFormattedEtaDateTime()))
					// .etaDateTime(DateUtil.formatDateString(atmTicketEvent.getEtaDateTime() ==
					// null ? "" : atmTicketEvent.getEtaDateTime()))
					// .etaDateTime(atmTicketEvent.getEtaDateTime() == null ? "" :
					// atmTicketEvent.getEtaDateTime())
					.etaTimeout(atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout())
					// .CreatedDate(ticketDetailsDto.getCalldate()==null?"":ticketDetailsDto.getCalldate())
					.CreatedDate(DateUtil.formatDateString(
							ticketDetailsDto.getCreateddate() == null ? "" : ticketDetailsDto.getCreateddate()))
					.flagStatus(atmTicketEvent.getFlagStatus())
					.flagStatusInsertTime(atmTicketEvent.getFlagStatusInsertTime())
					.updateEnable(
							updateEnableStatus(atmTicketEvent.getSubcall().isEmpty() ? ticketDetailsDto.getSubcalltype()
									: atmTicketEvent.getSubcall()))
					.build();

			log.info("getOpenTicketList()|atmShortDetailsDto:{}", atmShortDetailsDto);

			timedOutList.add(atmShortDetailsDto);
		}

		return timedOutList.stream().filter(e -> e.getDownCall() == 1)
				.sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()).toList();

	}

//	@Loggable
//	private String getDownTimeInHrs(String createDate) {
//		log.info("getDownTimeInHrs()|createdDate:{}", createDate);
//		DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.US);
//		LocalDateTime parsedCreatedDate = LocalDateTime.parse(createDate, formatterInput);
//		Duration duration = Duration.between(parsedCreatedDate, LocalDateTime.now());
//
//		return String.format("%d Hrs", duration.toHours());
//	}
	
	@Loggable
	private String getDownTimeInHrs(String createDate) {
	    log.info("getDownTimeInHrs()|createdDate:{}", createDate);
	    
	    if (createDate == null || createDate.trim().isEmpty()) {
	        return "0 Hrs";
	    }
	    
	    LocalDateTime parsedDate = null;
	    String trimmedDate = createDate.trim();
	    
	    try {
	        // Handle HIMS format with flexible milliseconds
	        if (trimmedDate.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+")) {
	            // Normalize milliseconds to 3 digits
	            String[] parts = trimmedDate.split("\\.");
	            String millisPart = parts[1];
	            
	            // Pad or truncate milliseconds to 3 digits
	            if (millisPart.length() == 1) {
	                millisPart = millisPart + "00";  // .0 -> .000
	            } else if (millisPart.length() == 2) {
	                millisPart = millisPart + "0";   // .00 -> .000
	            } else if (millisPart.length() > 3) {
	                millisPart = millisPart.substring(0, 3); // .0000 -> .000
	            }
	            
	            String normalizedDate = parts[0] + "." + millisPart;
	            parsedDate = LocalDateTime.parse(normalizedDate, 
	                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
	                
	        } else if (trimmedDate.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
	            // HIMS format without milliseconds
	            parsedDate = LocalDateTime.parse(trimmedDate, 
	                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	                
	        } else if (trimmedDate.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}")) {
	            // Original format
	            parsedDate = LocalDateTime.parse(trimmedDate, 
	                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
	        }
	        
	    } catch (Exception e) {
	        log.error("getDownTimeInHrs()|Error parsing date: {}", createDate, e);
	        return "Invalid Date";
	    }
	    
	    if (parsedDate == null) {
	        log.error("getDownTimeInHrs()|Unable to parse date: {}", createDate);
	        return "Invalid Date";
	    }
	    
	    Duration duration = Duration.between(parsedDate, LocalDateTime.now());
	    return String.format("%d Hrs", duration.toHours());
	}

//________________________________________multeathreading concepts ________________________________________________________________________________________________________

	@Loggable
	public AtmUptimeDto getCurrentMonthUptimePer(List<AtmDetailsDto> atms) {
		List<String> atmIds = atms.stream().map(a -> a.getAtmid()).toList();
		return getCurrentMonthUptimePer(atmIds, true);
	}
//_________________________________________________________________________________________

	@Loggable
	private AtmUptimeDto getCurrentMonthUptimePerByUserAtm(List<UserAtmDetails> atms) {
		List<String> atmIds = atms.stream().map(UserAtmDetails::getAtm_code).collect(Collectors.toList());
		return getCurrentMonthUptimePer(atmIds, true);
	}

	@Loggable
	public AtmUptimeDto getCurrentMonthUptimePer(List<String> atms, Boolean toBeRework) {
		SynergyLoginResponse synergyLoginResponse = new SynergyLoginResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());

		if (!"success".equals(synergyLoginResponse.getStatus())) {
			return null;
		}

		String url = synergyBaseUrl + "/getatmuptime";
		int atmCount = atms.size();
		double sum = 0;

		// Parallel Processing using ExecutorService
		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Future<Double>> futures = new ArrayList<>();

		for (String atmId : atms) {
			futures.add(executor.submit(() -> {
				AtmDetailsRequest atmDetailsRequest = new AtmDetailsRequest(synergyLoginResponse.getRequestid(), atmId);
				ResponseEntity<AtmUptimeDetailsResp> responseEntity = restTemplate.postForEntity(url, atmDetailsRequest,
						AtmUptimeDetailsResp.class);
				AtmUptimeDetailsResp atmUptimeDetailsResp = responseEntity.getBody();
				return (atmUptimeDetailsResp != null) ? atmUptimeDetailsResp.getMonthtotilldateuptime() : 0;
			}));
		}

		executor.shutdown();

		for (Future<Double> future : futures) {
			try {
				sum += future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		double avgUptimePer = sum / atmCount;
		return new AtmUptimeDto(LocalDateTime.now().toString(), avgUptimePer, atmCount, "This is current uptime");
	}


	@Loggable
	//Actual Dashboard details method
	public DashboardDataDto getDashboardDataWithStringWithUptime() {
		DisableSslClass.disableSSLVerification();
		long startSpTime = System.currentTimeMillis();
		List<UsersAtmDetailsDto> userAtmDetails = userAtmDetailsService.getUserAtmDetails(helper.getLoggedInUser()); // atmids
		log.debug("Time for execution SP getUserAtmDetails: {} seconds",
				(System.currentTimeMillis() - startSpTime) / 1000);

		List<AtmDetailsDto> atmIdDtoList = new ArrayList<>();
		long startIterationAtmDetailsTime = System.currentTimeMillis();

		for (int i = 0; i < userAtmDetails.stream().distinct().toList().size(); i++) {
			atmIdDtoList.add(new AtmDetailsDto(userAtmDetails.stream().distinct().toList().get(i).getAtm_code()));
		}
		log.debug("Time for execution startIterationAtmDetailsTime: {} seconds",
				(System.currentTimeMillis() - startIterationAtmDetailsTime) / 1000);

		long startGetCurrentMonthUptimePerTime = System.currentTimeMillis();
		// AtmUptimeDto atmCurrentUptime = getCurrentMonthUptimePer(atmIdDtoList);

		log.debug("Time for execution startGetCurrentMonthUptimePerTime: {} seconds",
				(System.currentTimeMillis() - startGetCurrentMonthUptimePerTime) / 1000);

		long startGetOpenTicketListTime = System.currentTimeMillis();
		List<AtmShortDetailsDto> allTicketsResponse = getOpenTicketList(atmIdDtoList, maxAtmListCount);
		
		
		
		// updated ticket list
		List<AtmShortDetailsDto> openTicketsResponse = new ArrayList<>();
		List<AtmShortDetailsDto> updatedOpenTicketResponse = new ArrayList<>();
		List<AtmShortDetailsDto> timedOutTicketResponse = new ArrayList<>();
		for (AtmShortDetailsDto response : allTicketsResponse) {
			if (response.getIsUpdated() == 1 && response.getIsTimedOut() == 0) {

				updatedOpenTicketResponse.add(response);
			} else if (response.getIsUpdated() == 0 && response.getIsTimedOut() == 1) {
				timedOutTicketResponse.add(response);
			} else {
				openTicketsResponse.add(response);
			}
		}
		List<AtmShortDetailsDto> pendingForDispatchTickets = openTicketsResponse.stream().filter(ticket -> ticket.getOwner().equalsIgnoreCase("Pending for Dispatch")).sorted((b, a) -> Integer.compare(a.getFlagStatus(), b.getFlagStatus())).toList();
		List<AtmShortDetailsDto> otherDownTickets =openTicketsResponse.stream().filter(ticket -> !ticket.getOwner().equalsIgnoreCase("Pending for Dispatch")).sorted((b, a) -> Integer.compare(a.getFlagStatus(), b.getFlagStatus())).toList();
		List<AtmShortDetailsDto> downCallTickets = new ArrayList<>(otherDownTickets);
		downCallTickets.addAll(pendingForDispatchTickets);
		// Using lambda
//		openTicketsResponse.sort((b, a) -> Integer.compare(a.getFlagStatus(), b.getFlagStatus()));
		updatedOpenTicketResponse.sort((b, a) -> Integer.compare(a.getFlagStatus(), b.getFlagStatus()));
		timedOutTicketResponse.sort((b, a) -> Integer.compare(a.getFlagStatus(), b.getFlagStatus()));
		log.debug("Time for execution startGetOpenTicketListTime: {} seconds",
				(System.currentTimeMillis() - startGetOpenTicketListTime) / 1000);
		int totalAtmCnt = atmIdDtoList.size();
		long startGetDownAtmCntTime = System.currentTimeMillis();
		int totalDownAtmCnt = (int) getDownAtmCnt(allTicketsResponse);
		log.debug("Time for execution startGetDownAtmCntTime: {} seconds",
				(System.currentTimeMillis() - startGetDownAtmCntTime) / 1000);

		DashboardDataDto dashboardDataDto = new DashboardDataDto();

		dashboardDataDto.setCurrentUptime(new CeAtmUptimeDto(DateUtil.formatPreviousDayTimestamp(),
				mtdUptimeService.getMtdUptimeFromSp().getUptime(), totalAtmCnt, ""));
		dashboardDataDto.setAtmStatusDto(
				buildAtmStatusDto(totalAtmCnt - totalDownAtmCnt, totalDownAtmCnt, "ATM status remarks"));
		dashboardDataDto.setTargetMtd(buildTargetMtdSummaryDto(80.22d, 92.99d, 2d, 60, 40, 50d)); // dummy data for
																									// display
		dashboardDataDto.setDownCallList(downCallTickets);
		dashboardDataDto.setUpdatedList(updatedOpenTicketResponse);
		dashboardDataDto.setTimedOutList(timedOutTicketResponse);
		dashboardDataDto.setSiteVisitList(Collections.emptyList());
		dashboardDataDto.setAtmCashIndentList(Collections.emptyList());
		log.debug("Time for execution getDashboardData: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);
		return dashboardDataDto;
	}
	
	@Loggable
	//Dashboard details with synergy and hims integration
	public DashboardDataDto getDashboardDetails() {
		DisableSslClass.disableSSLVerification();
		long startSpTime = System.currentTimeMillis();
		List<AtmWithSource> atmWithSourceList=atmWithSourceRepository.getAtmWithSourceForCe(helper.getLoggedInUser());
		log.debug("Time for execution SP getUserAtmDetails: {} seconds",
				(System.currentTimeMillis() - startSpTime) / 1000);

		List<AtmDetailsWithSourceDto> atmIdDtoList = new ArrayList<>();
		long startIterationAtmDetailsTime = System.currentTimeMillis();
		
		atmIdDtoList= atmWithSourceList.stream().map(atm -> new AtmDetailsWithSourceDto(atm.getAtmCode(),atm.getSource())).toList();
		log.debug("Time for execution startIterationAtmDetailsTime: {} seconds",
				(System.currentTimeMillis() - startIterationAtmDetailsTime) / 1000);

		long startGetCurrentMonthUptimePerTime = System.currentTimeMillis();
		//Need to work
		//AtmUptimeDto atmCurrentUptime = getCurrentMonthUptimePer(atmIdDtoList);

		log.debug("Time for execution startGetCurrentMonthUptimePerTime: {} seconds",
				(System.currentTimeMillis() - startGetCurrentMonthUptimePerTime) / 1000);
		//List<AtmDetailsDto> atmIdDtoList=new ArrayList<>();;
		long startGetOpenTicketListTime = System.currentTimeMillis();
		List<AtmShortDetailsDto> allTicketsResponse = getOpenTicketListWithSource(atmIdDtoList, maxAtmListCount);
		
		
		
		// updated ticket list
		List<AtmShortDetailsDto> openTicketsResponse = new ArrayList<>();
		List<AtmShortDetailsDto> updatedOpenTicketResponse = new ArrayList<>();
		List<AtmShortDetailsDto> timedOutTicketResponse = new ArrayList<>();
		for (AtmShortDetailsDto response : allTicketsResponse) {
			if (response.getIsUpdated() == 1 && response.getIsTimedOut() == 0) {

				updatedOpenTicketResponse.add(response);
			} else if (response.getIsUpdated() == 0 && response.getIsTimedOut() == 1) {
				timedOutTicketResponse.add(response);
			} else {
				openTicketsResponse.add(response);
			}
		}
		List<AtmShortDetailsDto> pendingForDispatchTickets = openTicketsResponse.stream().filter(ticket -> ticket.getOwner().equalsIgnoreCase("Pending for Dispatch")).sorted((b, a) -> Integer.compare(a.getFlagStatus(), b.getFlagStatus())).toList();
		List<AtmShortDetailsDto> otherDownTickets =openTicketsResponse.stream().filter(ticket -> !ticket.getOwner().equalsIgnoreCase("Pending for Dispatch")).sorted((b, a) -> Integer.compare(a.getFlagStatus(), b.getFlagStatus())).toList();
		List<AtmShortDetailsDto> downCallTickets = new ArrayList<>(otherDownTickets);
		downCallTickets.addAll(pendingForDispatchTickets);
		// Using lambda
//		openTicketsResponse.sort((b, a) -> Integer.compare(a.getFlagStatus(), b.getFlagStatus()));
		updatedOpenTicketResponse.sort((b, a) -> Integer.compare(a.getFlagStatus(), b.getFlagStatus()));
		timedOutTicketResponse.sort((b, a) -> Integer.compare(a.getFlagStatus(), b.getFlagStatus()));
		log.debug("Time for execution startGetOpenTicketListTime: {} seconds",
				(System.currentTimeMillis() - startGetOpenTicketListTime) / 1000);
		int totalAtmCnt = atmIdDtoList.size();
		long startGetDownAtmCntTime = System.currentTimeMillis();
		int totalDownAtmCnt = (int) getDownAtmCnt(allTicketsResponse);
		log.debug("Time for execution startGetDownAtmCntTime: {} seconds",
				(System.currentTimeMillis() - startGetDownAtmCntTime) / 1000);

		DashboardDataDto dashboardDataDto = new DashboardDataDto();

		dashboardDataDto.setCurrentUptime(new CeAtmUptimeDto(DateUtil.formatPreviousDayTimestamp(),
				mtdUptimeService.getMtdUptimeFromSp().getUptime(), totalAtmCnt, ""));
		dashboardDataDto.setAtmStatusDto(
				buildAtmStatusDto(totalAtmCnt - totalDownAtmCnt, totalDownAtmCnt, "ATM status remarks"));
		dashboardDataDto.setTargetMtd(buildTargetMtdSummaryDto(80.22d, 92.99d, 2d, 60, 40, 50d)); // dummy data for
																									// display
		dashboardDataDto.setDownCallList(downCallTickets);
		dashboardDataDto.setUpdatedList(updatedOpenTicketResponse);
		dashboardDataDto.setTimedOutList(timedOutTicketResponse);
		dashboardDataDto.setSiteVisitList(Collections.emptyList());
		dashboardDataDto.setAtmCashIndentList(Collections.emptyList());
		log.debug("Time for execution getDashboardData: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);
		return dashboardDataDto;
	}
	
	@Loggable
	public void getEventCodeDifference() {
		DisableSslClass.disableSSLVerification();
		long startSpTime = System.currentTimeMillis();
		List<UsersAtmDetailsDto> userAtmDetails = userAtmDetailsService.getUserAtmDetails(helper.getLoggedInUser()); // atmids
		log.debug("Time for execution SP getUserAtmDetails: {} seconds",
				(System.currentTimeMillis() - startSpTime) / 1000);

		List<AtmDetailsDto> atmIdDtoList = new ArrayList<>();
		long startIterationAtmDetailsTime = System.currentTimeMillis();

		for (int i = 0; i < userAtmDetails.stream().distinct().toList().size(); i++) {
			atmIdDtoList.add(new AtmDetailsDto(userAtmDetails.stream().distinct().toList().get(i).getAtm_code()));
		}
		log.debug("Time for execution startIterationAtmDetailsTime: {} seconds",
				(System.currentTimeMillis() - startIterationAtmDetailsTime) / 1000);

		long startGetCurrentMonthUptimePerTime = System.currentTimeMillis();
		// AtmUptimeDto atmCurrentUptime = getCurrentMonthUptimePer(atmIdDtoList);

		log.debug("Time for execution startGetCurrentMonthUptimePerTime: {} seconds",
				(System.currentTimeMillis() - startGetCurrentMonthUptimePerTime) / 1000);

		long startGetOpenTicketListTime = System.currentTimeMillis();
		List<AtmShortDetailsDto> allTicketsResponse = getOpenTicketList(atmIdDtoList, maxAtmListCount);
		
	}
	
	@Loggable
	//Actual "/up-down-atmCount-forCE" method
	public AtmStatusDtoForCm getAtmStatus(String CeUserId) {
        // Get user ATM details
		DisableSslClass.disableSSLVerification();
//        List<UsersAtmDetailsDto> userAtmDetails = userAtmDetailsService.getUserAtmDetails(helper.getLoggedInUser());
		List<UsersAtmDetailsDto> userAtmDetails = userAtmDetailsService.getUserAtmDetails(CeUserId);
        // Convert to ATM details list 
        List<AtmDetailsDto> atmIdDtoList = userAtmDetails.stream()
            .distinct()
            .map(atm -> new AtmDetailsDto(atm.getAtm_code()))
            .collect(Collectors.toList());
            
        // Get total ATM count
        int totalAtmCount = atmIdDtoList.size();
        
        // Get list of all tickets to calculate down ATMs
        List<AtmShortDetailsDto> allTickets = getOpenTicketListForUpDownAtm(atmIdDtoList, maxAtmListCount);
        
        // Calculate down ATM count
        int downAtmCount = (int) getDownAtmCnt(allTickets);
        
        // Calculate up ATM count
        int upAtmCount = totalAtmCount - downAtmCount;
        
        return new AtmStatusDtoForCm(upAtmCount, downAtmCount, totalAtmCount);
    }
	
	@Loggable
	//Actual "/up-down-atmCount-forCE" method
	public AtmStatusDtoForCm getAtmStatusWithHims(String CeUserId) {
        // Get user ATM details
//		DisableSslClass.disableSSLVerification();
////        List<UsersAtmDetailsDto> userAtmDetails = userAtmDetailsService.getUserAtmDetails(helper.getLoggedInUser());
//		List<UsersAtmDetailsDto> userAtmDetails = userAtmDetailsService.getUserAtmDetails(CeUserId);
//        // Convert to ATM details list 
//        List<AtmDetailsDto> atmIdDtoList = userAtmDetails.stream()
//            .distinct()
//            .map(atm -> new AtmDetailsDto(atm.getAtm_code()))
//            .collect(Collectors.toList());
//            
//        // Get total ATM count
        
        
        // Get list of all tickets to calculate down ATMs
        DisableSslClass.disableSSLVerification();
		long startSpTime = System.currentTimeMillis();
		List<AtmWithSource> atmWithSourceList=atmWithSourceRepository.getAtmWithSourceForCe(helper.getLoggedInUser());
		log.debug("Time for execution SP getUserAtmDetails: {} seconds",
				(System.currentTimeMillis() - startSpTime) / 1000);

		List<AtmDetailsWithSourceDto> atmIdDtoList = new ArrayList<>();
		long startIterationAtmDetailsTime = System.currentTimeMillis();
		
		atmIdDtoList= atmWithSourceList.stream().map(atm -> new AtmDetailsWithSourceDto(atm.getAtmCode(),atm.getSource())).toList();
		log.debug("Time for execution startIterationAtmDetailsTime: {} seconds",
				(System.currentTimeMillis() - startIterationAtmDetailsTime) / 1000);
		int totalAtmCount = atmIdDtoList.size();
		long startGetCurrentMonthUptimePerTime = System.currentTimeMillis();
		//Need to work
		//AtmUptimeDto atmCurrentUptime = getCurrentMonthUptimePer(atmIdDtoList);

		log.debug("Time for execution startGetCurrentMonthUptimePerTime: {} seconds",
				(System.currentTimeMillis() - startGetCurrentMonthUptimePerTime) / 1000);
		//List<AtmDetailsDto> atmIdDtoList=new ArrayList<>();;
//        List<AtmShortDetailsDto> allTickets = getOpenTicketListForUpDownAtm(atmIdDtoList, maxAtmListCount);
        List<AtmShortDetailsDto> allTickets = getOpenTicketListWithSource(atmIdDtoList, maxAtmListCount);
        // Calculate down ATM count
        int downAtmCount = (int) getDownAtmCnt(allTickets);
        
        // Calculate up ATM count
        int upAtmCount = totalAtmCount - downAtmCount;
        
        return new AtmStatusDtoForCm(upAtmCount, downAtmCount, totalAtmCount);
    }

	@Loggable
	private long getDownAtmCnt(List<AtmShortDetailsDto> openTicketsResponse) {
		return openTicketsResponse.stream().filter(ticket -> ticket.getIsBreakdown() == 1).map(a -> a.getAtmId())
				.distinct().count();
	}

	public TargetMtdSummaryDto buildTargetMtdSummaryDto(Double uptimePer, Double uptimeTargetPer,
			Double differenceUptimePer,

			Integer transactionCount, Integer transactionTarget, Double differenceTransactionPer) {
		TargetMtdSummaryDto targetMtdSummaryDto = new TargetMtdSummaryDto();
		targetMtdSummaryDto.setUptimePer(uptimePer);
		targetMtdSummaryDto.setUptimeTargetPer(uptimeTargetPer);
		targetMtdSummaryDto.setDifferenceUptimePer(differenceUptimePer);
		targetMtdSummaryDto.setTransactionCount(transactionCount);
		targetMtdSummaryDto.setTransactionTarget(transactionTarget);
		targetMtdSummaryDto.setDifferenceTransactionPer(differenceTransactionPer);
		return targetMtdSummaryDto;

	}

	public AtmStatusDto buildAtmStatusDto(Integer upAtmCount, Integer downAtmCount, String remarks) {
		AtmStatusDto atmStatusDto = new AtmStatusDto();
		atmStatusDto.setUpAtmCount(upAtmCount);
		atmStatusDto.setDownAtmCount(downAtmCount);
		atmStatusDto.setRemarks(remarks);
		return atmStatusDto;
	}

	public AtmUptimeDto buildAtmUptimeDto(String lastUpdated, Double uptime, Integer atmCount, String remarks) {
		AtmUptimeDto atmUptimeDto = new AtmUptimeDto();
		atmUptimeDto.setLastUpdated(lastUpdated);
		atmUptimeDto.setUptime(uptime);
		atmUptimeDto.setAtmCount(atmCount);
		atmUptimeDto.setRemarks(remarks);

		return atmUptimeDto;
	}

	@Loggable
	public List<NotificationDto> getNotificationDetails() throws IOException {
		try (InputStreamReader reader = new InputStreamReader(
				new ClassPathResource("notifications.json").getInputStream())) {
			ObjectMapper mapper = new ObjectMapper();
			List<NotificationDto> notifications = mapper.readValue(reader, List.class);
			log.debug("readValue = " + notifications);
			return notifications;
		} catch (IOException e) {
			log.error("Failed to read ATM details from JSON file", e);
			throw e;
		}
	}

	@Loggable
	public OpenTicketsResponse getOpenTicketDetails_cm(List<CEAtmDetailsDtoFor_Cm> atms) {
		SynergyLoginResponse synergyLoginResponse = new SynergyLoginResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());

		String url = synergyBaseUrl + "/getopentickets";
		log.debug("getopenTicketsUrl: {}", url);
		log.debug("atms: {}", atms.toString());
		OpenTicketsRequest_cm synergyRequest = new OpenTicketsRequest_cm(synergyLoginResponse.getRequestid(), atms);
		log.info("Openticket synergyRequest:{}", synergyRequest);
		ResponseEntity<OpenTicketsResponse> responseEntity = restTemplate.postForEntity(url, synergyRequest,
				OpenTicketsResponse.class);
		log.info("OpenTicketsResponse:{}", responseEntity.getBody());
		return responseEntity.getBody();
	}

	@Loggable
	public List<AtmShortDetailsDto> getOpenTicketList_cm(List<CEAtmDetailsDtoFor_Cm> atms, int maxLimit) {
		OpenTicketsResponse openTicketsResponse = getOpenTicketDetails_cm(atms);
		List<AtmShortDetailsDto> timedOutList = new ArrayList<>();

		StringBuilder atmTicketEventCode = new StringBuilder();
		Map<String, TicketDetailsDto> ticketDetailsMap = new HashMap<>();

		for (int i = 0; openTicketsResponse.getAtmdetails() != null
				&& i < openTicketsResponse.getAtmdetails().size(); i++) {
			for (int j = 0; j < openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails().size(); j++) {
				TicketDetailsDto ticketDetailsDto = openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails()
						.get(j);

				String uniqueCode = String.format("%s|%s|%s|%s,", ticketDetailsDto.getEquipmentid(),
						ticketDetailsDto.getSrno(), ticketDetailsDto.getEventcode(),
						ticketDetailsDto.getNextfollowup());
				atmTicketEventCode.append(uniqueCode);
				ticketDetailsMap.put(ticketDetailsDto.getEquipmentid() + ticketDetailsDto.getSrno(), ticketDetailsDto);

				if (openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails().size() > maxLimit) {
					break;
				}
			}
			if (openTicketsResponse.getAtmdetails().size() > maxLimit) {
				break;
			}
		}

		String atmTicketEventCodeList = atmTicketEventCode.toString();

		atmTicketEventCodeList = atmTicketEventCodeList.substring(0, atmTicketEventCodeList.length() - 1);
		log.info("atmTicketEventCode:{}", atmTicketEventCodeList);
		List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository.getAtmTicketEvent(helper.getLoggedInUser(),
				atmTicketEventCodeList);
		log.info("getOpenTicketList()|From SP|atmTicketEventList:{}", atmTicketEventList);
		for (AtmTicketEvent atmTicketEvent : atmTicketEventList) {
			TicketDetailsDto ticketDetailsDto = ticketDetailsMap
					.get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());

			AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
					.atmId(ticketDetailsDto.getEquipmentid()).ticketNumber(ticketDetailsDto.getSrno())
					.bank(ticketDetailsDto.getCustomer()).siteName(ticketDetailsDto.getEquipmentid())
					.owner(atmTicketEvent.getOwner().isEmpty() ? ticketDetailsDto.getSubcalltype()
							: atmTicketEvent.getOwner())
					.vendor(ticketDetailsDto.getVendor()).error(atmTicketEvent.getEventCode())
					.downTime(getDownTimeInHrs(ticketDetailsDto.getCreateddate()))
					.priorityScore(atmTicketEvent.getPriorityScore()).eventGroup(atmTicketEvent.getEventGroup())
					.isBreakdown(atmTicketEvent.getIsBreakdown() == null ? 0 : atmTicketEvent.getIsBreakdown())
					.isUpdated(atmTicketEvent.getIsUpdated() == null ? 0 : atmTicketEvent.getIsUpdated())
					.isTimedOut(atmTicketEvent.getIsTimedOut() == null ? 0 : atmTicketEvent.getIsTimedOut())
					.isTravelling(atmTicketEvent.getIsTravelling() == null ? 0 : atmTicketEvent.getIsTravelling())
					.travelTime(atmTicketEvent.getTravelTime()).travelEta(atmTicketEvent.getTravelEta())
					.downCall(atmTicketEvent.getDownCall() == null ? 0 : atmTicketEvent.getDownCall())
					.etaDateTime((atmTicketEvent.getEtaDateTime() == null ? "" : atmTicketEvent.getEtaDateTime()))
					.etaTimeout(atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout()).build();
			log.info("getOpenTicketList()|atmShortDetailsDto:{}", atmShortDetailsDto);

			timedOutList.add(atmShortDetailsDto);
		}

		return timedOutList.stream().filter(e -> e.getDownCall() == 1)
				.sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()).toList();

	}

	private String getDownTimeInHrs_cm(String createdDate) {
		log.info("getDownTimeInHrs()|createdDate:{}", createdDate);
		DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.US);
		LocalDateTime parsedCreatedDate = LocalDateTime.parse(createdDate, formatterInput);
		Duration duration = Duration.between(parsedCreatedDate, LocalDateTime.now());

		return String.format("%d Hrs", duration.toHoursPart());
	}


	@Loggable
	private AtmUptimeDto getCmCurrentMonthUptime(List<CEAtmDetailsDtoFor_Cm> atms) {
		SynergyLoginResponse synergyLoginResponse = new SynergyLoginResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());

		if (!"success".equals(synergyLoginResponse.getStatus())) {
			return null;
		}

		String url = synergyBaseUrl + "/getatmuptime";
		int atmCount = atms.size();
		double sum = 0;
		List<CompletableFuture<ResponseEntity<AtmUptimeDetailsResp>>> futures = atms.stream()
				.map(atmId -> CompletableFuture.supplyAsync(() -> restTemplate.postForEntity(url,
						new AtmDetailsRequest(synergyLoginResponse.getRequestid(), atmId.getAtmCode()),
						AtmUptimeDetailsResp.class)))
				.toList();

		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

		List<ResponseEntity<AtmUptimeDetailsResp>> responses = futures.stream().map(CompletableFuture::join).toList();
		for (ResponseEntity<AtmUptimeDetailsResp> response : responses) {
			AtmUptimeDetailsResp atmUptimeDetailsResp = response.getBody();
			sum += (atmUptimeDetailsResp != null) ? atmUptimeDetailsResp.getMonthtotilldateuptime() : 0;

		}
		double avgUptimePer = sum / atmCount;
		return new AtmUptimeDto(LocalDateTime.now().toString(), avgUptimePer, atmCount, "This is current uptime");

	}

	@Loggable
	public AtmUptimeDto getCurrentMonthUptimePer_cm(List<CEAtmDetailsDtoFor_Cm> atms) {
		List<String> atmIds = atms.stream().map(CEAtmDetailsDtoFor_Cm::getAtmCode).toList();
		return getCurrentMonthUptimePer_cm(atmIds, true);
	}

	@Loggable
	public AtmUptimeDto getCurrentMonthUptimePer_cm(List<String> atms, Boolean toBeRework) {
		SynergyLoginResponse synergyLoginResponse = new SynergyLoginResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());

		if (!"success".equals(synergyLoginResponse.getStatus())) {
			return null;
		}

		String url = synergyBaseUrl + "/getatmuptime";
		int atmCount = atms.size();
		double sum = 0;

		// Parallel Processing using ExecutorService
		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Future<Double>> futures = new ArrayList<>();

		for (String atmId : atms) {
			futures.add(executor.submit(() -> {
				AtmDetailsRequest atmDetailsRequest = new AtmDetailsRequest(synergyLoginResponse.getRequestid(), atmId);
				ResponseEntity<AtmUptimeDetailsResp> responseEntity = restTemplate.postForEntity(url, atmDetailsRequest,
						AtmUptimeDetailsResp.class);
				AtmUptimeDetailsResp atmUptimeDetailsResp = responseEntity.getBody();
				return (atmUptimeDetailsResp != null) ? atmUptimeDetailsResp.getMonthtotilldateuptime() : 0;
			}));
		}

		executor.shutdown();

		for (Future<Double> future : futures) {
			try {
				sum += future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		double avgUptimePer = sum / atmCount;
		return new AtmUptimeDto(LocalDateTime.now().toString(), avgUptimePer, atmCount, "This is current uptime");
	}

	// ------CM portal
	// -------------------------------------------------------------------------------

	@Loggable
	public CMPortalDashboardDto_AtmStatusDto setAtmStatusDto_cm() {
		long startSpTime = System.currentTimeMillis();
		String userId = helper.getLoggedInUser();

		// List<UserAtmDetails> userAtmDetails =
		// userAtmDetailsService.getUserAtmDetails(userId);
		List<CEAtmDetailsDtoFor_Cm> userAtmDetails = atmDetailsServiceFor_Cm.getCEAtmDetails(userId);
		List<AtmDetailsDto> atmIdDtoList = new ArrayList<>();
		for (int i = 0; i < userAtmDetails.size(); i++) {
			atmIdDtoList.add(new AtmDetailsDto(userAtmDetails.get(i).getAtmCode()));

		}
		List<AtmShortDetailsDto> openTicketsResponse = getOpenTicketList_cm_ActiveApi(atmIdDtoList, maxAtmListCount);
		int totalAtmCnt = userAtmDetails.size();
		int totalDownAtmCnt = (int) getDownAtmCnt(openTicketsResponse);
		CMPortalDashboardDto_AtmStatusDto cmPortalDashboardDto = new CMPortalDashboardDto_AtmStatusDto();
		cmPortalDashboardDto.setAtmStatusDto(
				buildAtmStatusDto(totalAtmCnt - totalDownAtmCnt, totalDownAtmCnt, "ATM status remarks"));
		return cmPortalDashboardDto;

	}

	// @Retryable(maxAttempts = 3, value = { RuntimeException.class })
	@Loggable
	public CMPortalDashboardDto getCmPortalDashboardData() {

		long startSpTime = System.currentTimeMillis();
		String userId = helper.getLoggedInUser();
		List<CEAtmDetailsDtoFor_Cm> userAtmDetails = getCeAtmDetailsByCm(userId, startSpTime);
		NumberOfCeDto numberOfCes = getCeDetailsCountByCm(userId, startSpTime);

		log.warn("Time for A5: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);
		AtmUptimeDto atmCurrentUptime = getCurrentMonthUptimePer_cm(userAtmDetails);
		log.warn("Time for A6: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);

		
		log.warn("Time for A7: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);
		log.warn("Time for A8: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);
		log.warn("Time for A9: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);

		CMPortalDashboardDto cmPortalDashboardDto = new CMPortalDashboardDto();
		log.warn("Time for A10: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);
		cmPortalDashboardDto.setCurrentUptime(atmCurrentUptime);
		log.warn("Time for A11: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);
		log.warn("Time for A12: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);

		log.warn("Time for A13: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);

		log.warn("Time for A14: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);
		cmPortalDashboardDto.setNumberOfCeDto(numberOfCes);
		log.warn("Time for A15: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);
		return cmPortalDashboardDto;

	}

	@Loggable
	public CMPortalDashboardDto getNumberOfCes() {
		long startSpTime = System.currentTimeMillis();
		String userId = helper.getLoggedInUser();
		NumberOfCeDto numberOfCes = getCeDetailsCountByCm(userId, startSpTime);
		CMPortalDashboardDto cmPortalDashboardDto = new CMPortalDashboardDto();
		log.warn("Time for getNumberOfCes #1: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);
		cmPortalDashboardDto.setNumberOfCeDto(numberOfCes);
		log.warn("Time for getNumberOfCes #2: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);
		return cmPortalDashboardDto;
	}


	@Loggable
	public AtmMtdUptimeDto getCurrentUptime() {
		long startSpTime = System.currentTimeMillis();
		String userId = helper.getLoggedInUser();

		// Get the data from repositories
		CmMtdUptime cmMtdUptime = cmMtdUptimeRepository.getUptimeForCM(userId);
		AtmUpDownCount getATMCountForCM = atmUpDownCountRepository.getATMCountForCM(userId);

		log.warn("Time for getCurrentUptime #3: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);

		// Create response DTO
		AtmMtdUptimeDto atmUptimeDto = new AtmMtdUptimeDto();
		atmUptimeDto.setUptime(cmMtdUptime.getCmMtdUptime());
		atmUptimeDto.setAtmCount(getATMCountForCM.getTotalAtm());

		// Add previous day's timestamp (T-1) with truncated nanoseconds
		LocalDateTime previousDay = LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59)
				.withNano(0); // This line removes the nanoseconds
		atmUptimeDto.setTimestamp(previousDay);

		return atmUptimeDto;
	}

	@Loggable
	private NumberOfCeDto getCeDetailsCountByCm(String userId, long startSpTime) {
		NumberOfCeDto numberOfCes = numberOfCeService.getCMAgainstCEDetailsCount(userId);
		log.warn("Time for getCeDetailsCountByCm: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);
		return numberOfCes;
	}

	@Loggable
	private List<CEAtmDetailsDtoFor_Cm> getCeAtmDetailsByCm(String userId, long startSpTime) {
		List<CEAtmDetailsDtoFor_Cm> userAtmDetails = atmDetailsServiceFor_Cm.getCEAtmDetails(userId);
		log.warn("Time for getCeAtmDetailsByCm: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);
		return userAtmDetails;
	}

	@Loggable
	public AtmUpDownCountDto getATMCountForCM() {
		String CmUserId = helper.getLoggedInUser();
		AtmUpDownCount getATMCountForCM = atmUpDownCountRepository.getATMCountForCM(CmUserId);
		return getATMCountForCMmapper(getATMCountForCM);
	}

	private AtmUpDownCountDto getATMCountForCMmapper(AtmUpDownCount atmCount) {
		AtmUpDownCountDto atmCountDto = new AtmUpDownCountDto();
//		atmCountDto.setSrNo(atmCount.getSrNo());
		atmCountDto.setDownAtm(atmCount.getDownAtm());
		atmCountDto.setUpAtm(atmCount.getUpAtm());
		atmCountDto.setTotalAtm(atmCount.getTotalAtm());
		return atmCountDto;

	}

	@Loggable
	private OpenTicketsResponse getOpenTicketListFromSynergy() {
		List<UsersAtmDetailsDto> userAtmDetails = userAtmDetailsService.getUserAtmDetails(helper.getLoggedInUser());
		List<AtmDetailsDto> atmIdDtoList = new ArrayList<>();

		for (int i = 0; i < userAtmDetails.size(); i++) {
			atmIdDtoList.add(new AtmDetailsDto(userAtmDetails.get(i).getAtm_code()));
		}
		OpenTicketsResponse openTicketsResponse = getOpenTicketDetails(atmIdDtoList);
		return openTicketsResponse;
	}

	@Loggable
	private Map<String, Integer> getRaisedTicketByEvent() {
		OpenTicketsResponse openTicketsResponse = getOpenTicketListFromSynergy();

		Map<String, Integer> ticketCountByEvent = new HashMap<>();
		for (int i = 0; openTicketsResponse.getAtmdetails() != null
				&& i < openTicketsResponse.getAtmdetails().size(); i++) {
			for (int j = 0; j < openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails().size(); j++) {
				TicketDetailsDto ticketDetailsDto = openTicketsResponse.getAtmdetails().get(i).getOpenticketdetails()
						.get(j);
				String eventCode = ticketDetailsDto.getEventcode();
				if (!ticketCountByEvent.containsKey(eventCode)) {
					ticketCountByEvent.put(eventCode, 1);
				} else {
					ticketCountByEvent.put(eventCode, ticketCountByEvent.get(eventCode) + 1);
				}
			}
		}
		return ticketCountByEvent;
	}

	@Loggable
	public TicketsRaisedCountResponseDto getTicketsRaisedCount() {
		long startSpTime = System.currentTimeMillis();
		String userId = helper.getLoggedInUser();
		TicketsRaisedCount entity = ticketRaisedCountRepository.getTicketsRaisedCount(userId);
		TicketsRaisedCountResponseDto result = mapToDto(entity);
		log.warn("Time for getTicketsRaisedCount: {} seconds", (System.currentTimeMillis() - startSpTime) / 1000);
		return result;
	}

	private TicketsRaisedCountResponseDto mapToDto(TicketsRaisedCount entity) {
		TicketsRaisedCountResponseDto dto = new TicketsRaisedCountResponseDto();
		dto.setBreakdown(entity.getBreakdown());
		dto.setService(entity.getService());
		dto.setUpdated(entity.getUpdated());
		dto.setTotal(entity.getTotal());
		return dto;
	}

	@Loggable
	public TicketsRaisedCountDTO getCETicketsRaisedCount(String userId) {
		// String userId = helper.getLoggedInUser();

		TicketsRaisedCount ticketraised = ticketRaisedCountRepository.getCETicketsRaisedCount(userId);
		return getTicketsRaisedCountResponseMapper(ticketraised);
	}

	private TicketsRaisedCountDTO getTicketsRaisedCountResponseMapper(TicketsRaisedCount count) {
		TicketsRaisedCountDTO countresponseDto = new TicketsRaisedCountDTO();
		countresponseDto.setSrNo(count.getSr_no());
		countresponseDto.setBreakdown(count.getBreakdown());
		countresponseDto.setService(count.getService());
		countresponseDto.setTotal(count.getTotal());
		countresponseDto.setUpdated(count.getUpdated());
		return countresponseDto;

	}


	@Loggable
	public GetCmPortalATMScreenDataDto getCmPortalATMScreenData(String userId) {

		long startSpTime = System.currentTimeMillis();
		
		List<AtmDetailsDto> atmIdDtoList = cmSynopsisService.getCEAtmListUnderCM(userId);

		log.info("Time for execution SP getCMUnderCEAtmDetails: {} seconds",
				(System.currentTimeMillis() - startSpTime) / 1000);

		long startIterationAtmDetailsTime = System.currentTimeMillis();

		log.info("Time for execution startIterationAtmDetailsTime: {} seconds",
				(System.currentTimeMillis() - startIterationAtmDetailsTime) / 1000);
		long startGetCurrentMonthUptimePerTime = System.currentTimeMillis();

		AtmUptimeDto atmCurrentUptime = getCurrentMonthUptimePer(atmIdDtoList);

		log.info("Time for execution startGetCurrentMonthUptimePerTime: {} seconds",
				(System.currentTimeMillis() - startGetCurrentMonthUptimePerTime) / 1000);


		List<AtmShortDetailsDto> openTicketsResponse = getOpenTicketList(atmIdDtoList, maxAtmListCount);

		int totalAtmCnt = atmIdDtoList.size();
		int totalDownAtmCnt = (int) getDownAtmCnt(openTicketsResponse);

		GetCmPortalATMScreenDataDto getCmPortalATMScreenDataDto = new GetCmPortalATMScreenDataDto();
		getCmPortalATMScreenDataDto.setCurrentUptime(atmCurrentUptime);
		getCmPortalATMScreenDataDto.setAtmStatusDto(
				buildAtmStatusDto(totalAtmCnt - totalDownAtmCnt, totalDownAtmCnt, "ATM status remarks"));

		return getCmPortalATMScreenDataDto;

	}

	@Loggable
	public List<AtmShortDetailsDto> getOpenTicketList_cm_ActiveApi(List<AtmDetailsDto> atms, int maxLimit) {
		OpenTicketsResponse openTicketsResponse = getOpenTicketDetails(atms);
		List<AtmShortDetailsDto> timedOutList = new ArrayList<>();

		if (openTicketsResponse == null || openTicketsResponse.getAtmdetails() == null) {
			log.warn("Open tickets response or ATM details are null.");
			return timedOutList;
		}

		StringBuilder atmTicketEventCode = new StringBuilder();
		Map<String, TicketDetailsDto> ticketDetailsMap = new HashMap<>();

		if (openTicketsResponse.getAtmdetails() != null && openTicketsResponse.getAtmdetails().size() <= maxLimit) {
			for (int i = 0; i < openTicketsResponse.getAtmdetails().size(); i++) {
				List<TicketDetailsDto> openTicketDetails = openTicketsResponse.getAtmdetails().get(i)
						.getOpenticketdetails();

				if (openTicketDetails.size() <= maxLimit) {
					for (int j = 0; j < openTicketDetails.size(); j++) {
						TicketDetailsDto ticketDetailsDto = openTicketDetails.get(j);

						String uniqueCode = String.format("%s|%s|%s|%s,", ticketDetailsDto.getEquipmentid(),
								ticketDetailsDto.getSrno(), ticketDetailsDto.getEventcode(),
								ticketDetailsDto.getNextfollowup());
						atmTicketEventCode.append(uniqueCode);
						ticketDetailsMap.put(ticketDetailsDto.getEquipmentid() + ticketDetailsDto.getSrno(),
								ticketDetailsDto);
					}
				}
			}
		}

		String atmTicketEventCodeList = atmTicketEventCode.toString();

		if (!atmTicketEventCodeList.isEmpty()) {
			atmTicketEventCodeList = atmTicketEventCodeList.substring(0, atmTicketEventCodeList.length() - 1);
			log.info("atmTicketEventCode:{}", atmTicketEventCodeList);
			List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
					.getAtmTicketEvent(helper.getLoggedInUser(), atmTicketEventCodeList);
			log.info("getOpenTicketList()|From SP|atmTicketEventList:{}", atmTicketEventList);

			for (AtmTicketEvent atmTicketEvent : atmTicketEventList) {
				TicketDetailsDto ticketDetailsDto = ticketDetailsMap
						.get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());

				AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
						.atmId(ticketDetailsDto.getEquipmentid()).ticketNumber(ticketDetailsDto.getSrno())
						.bank(ticketDetailsDto.getCustomer()).siteName(ticketDetailsDto.getEquipmentid())
						.owner(atmTicketEvent.getOwner().isEmpty() ? ticketDetailsDto.getSubcalltype()
								: atmTicketEvent.getOwner())
						.vendor(ticketDetailsDto.getVendor()).error(atmTicketEvent.getEventCode())
						.downTime(getDownTimeInHrs(ticketDetailsDto.getCreateddate()))
						.priorityScore(atmTicketEvent.getPriorityScore()).eventGroup(atmTicketEvent.getEventGroup())
						.isBreakdown(atmTicketEvent.getIsBreakdown() == null ? 0 : atmTicketEvent.getIsBreakdown())
						.isUpdated(atmTicketEvent.getIsUpdated() == null ? 0 : atmTicketEvent.getIsUpdated())
						.isTimedOut(atmTicketEvent.getIsTimedOut() == null ? 0 : atmTicketEvent.getIsTimedOut())
						.isTravelling(atmTicketEvent.getIsTravelling() == null ? 0 : atmTicketEvent.getIsTravelling())
						.travelTime(atmTicketEvent.getTravelTime()).travelEta(atmTicketEvent.getTravelEta())
						.downCall(atmTicketEvent.getDownCall() == null ? 0 : atmTicketEvent.getDownCall())
						.etaDateTime((atmTicketEvent.getEtaDateTime() == null ? "" : atmTicketEvent.getEtaDateTime()))
						.etaTimeout(atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout())
						.build();
				log.info("getOpenTicketList()|atmShortDetailsDto:{}", atmShortDetailsDto);

				timedOutList.add(atmShortDetailsDto);
			}
		}

		return timedOutList.stream().filter(e -> e.getDownCall() == 1)
				.sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()).toList();
	}

	@Loggable
	public CeAtmUptimeDto getCeUptimeFromSp() {
		List<UsersAtmDetailsDto> userAtmDetails = userAtmDetailsService.getUserAtmDetails(helper.getLoggedInUser());
		int totalAtms = 0;
		if (!userAtmDetails.isEmpty()) {
			totalAtms = userAtmDetails.size();
		}

		return new CeAtmUptimeDto("", mtdUptimeService.getMtdUptimeFromSp().getUptime(), totalAtms, "");
	}

	@Loggable
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
				log.info("startIdx:{}", startIdx);
				log.info("endIdx:{}", endIdx);
				List<AtmDetailsDto> atmBatch = atms.subList(startIdx, endIdx);
				Callable<OpenTicketsResponse> task = () -> getOpenTicketDetails(atmBatch);
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
		// consolidatedResponse.setRequestid("Consolidated");
		consolidatedResponse.setRequestid(allResponses.get(0).getRequestid());

		List<AtmDetailsWithTickets> consolidatedAtmDetails = allResponses.stream()
				.flatMap(response -> response.getAtmdetails().stream()).toList();

		consolidatedResponse.setAtmdetails(consolidatedAtmDetails);
		return consolidatedResponse;

	}
	
	public OpenTicketsResponse getOpenTicketDetailsInBatchesForUpDownAtm(List<AtmDetailsDto> atms, int batchSize) {
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
				log.info("startIdx:{}", startIdx);
				log.info("endIdx:{}", endIdx);
				List<AtmDetailsDto> atmBatch = atms.subList(startIdx, endIdx);
				Callable<OpenTicketsResponse> task = () -> getOpenTicketDetailsForUpDwonAtm(atmBatch);
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
		// consolidatedResponse.setRequestid("Consolidated");
		consolidatedResponse.setRequestid(allResponses.get(0).getRequestid());
		
		List<AtmDetailsWithTickets> consolidatedAtmDetails = allResponses.stream()
				.flatMap(response -> response.getAtmdetails().stream()).toList();
		
		consolidatedResponse.setAtmdetails(consolidatedAtmDetails);
		return consolidatedResponse;
		
	}
	
	@Loggable
	public OpenTicketsResponse getOpenTicketDetailsInBatchesWithSource(List<AtmDetailsWithSourceDto> atmIdDtoList, int batchSize) {

	    List<AtmDetailsWithSourceDto> synergyAtmList = atmIdDtoList.stream()
	            .filter(atm -> atm.getSource().equals("synergy"))
	            .toList();
	    List<AtmDetailsWithSourceDto> himsAtmList = atmIdDtoList.stream()
	            .filter(atm -> atm.getSource().equals("hims"))
	            .toList();

	    List<OpenTicketsResponse> allResponses = new ArrayList<>();
	    OpenTicketsResponse consolidatedResponse = new OpenTicketsResponse();
	    
	    // Initialize consolidated ATM details list
	    List<AtmDetailsWithTickets> consolidatedAtmDetails = new ArrayList<>();
	    
	    // Process Synergy ATMs
	    if (!synergyAtmList.isEmpty()) {
	        try {
	            log.info("Processing Synergy ATMs - batchSize:{}", batchSize);
	            // Calculate the number of batches needed
	            int numBatches = (synergyAtmList.size() + batchSize - 1) / batchSize;
	            log.info("numBatches:{}", numBatches);

	            // Create an ExecutorService with a fixed thread pool size
	            ExecutorService executor = Executors.newFixedThreadPool(10);

	            try {
	                List<Future<OpenTicketsResponse>> futures = new ArrayList<>();

	                for (int i = 0; i < numBatches; i++) {
	                    int startIdx = i * batchSize;
	                    int endIdx = Math.min((i + 1) * batchSize, synergyAtmList.size());
	                    log.info("Processing batch {} - startIdx:{}, endIdx:{}", i + 1, startIdx, endIdx);
	                    
	                    List<AtmDetailsWithSourceDto> atmBatchWithSource = synergyAtmList.subList(startIdx, endIdx);
	                    List<AtmDetailsDto> atms = atmBatchWithSource.stream()
	                            .map(atm -> new AtmDetailsDto(atm.getAtmid()))
	                            .toList();
	                    
	                    Callable<OpenTicketsResponse> task = () -> {
	                        try {
	                            return getOpenTicketDetails(atms);
	                        } catch (Exception e) {
	                            log.error("Error processing batch with ATMs: {}", atms, e);
	                            throw new RuntimeException("Failed to process Synergy ticket batch", e);
	                        }
	                    };
	                    futures.add(executor.submit(task));
	                }

	                for (int i = 0; i < futures.size(); i++) {
	                    try {
	                        OpenTicketsResponse response = futures.get(i).get();
	                        allResponses.add(response);
	                        log.info("Successfully processed batch {}", i + 1);
	                    } catch (Exception e) {
	                        log.error("Error getting result from batch {}", i + 1, e);
	                        throw new RuntimeException("Failed to retrieve Synergy ticket batch results", e);
	                    }
	                }

	            } finally {
	                executor.shutdown();
	                try {
	                    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
	                        executor.shutdownNow();
	                        log.warn("Synergy executor did not terminate gracefully");
	                    }
	                } catch (InterruptedException e) {
	                    executor.shutdownNow();
	                    Thread.currentThread().interrupt();
	                }
	            }

	            // Consolidate Synergy ATM details
	            List<AtmDetailsWithTickets> synergyAtmDetails = allResponses.stream()
	                    .flatMap(response -> response.getAtmdetails().stream())
	                    .toList();
	            
	            consolidatedAtmDetails.addAll(synergyAtmDetails);
	            log.info("Successfully consolidated {} Synergy ATM details", synergyAtmDetails.size());
	            
	        } catch (Exception e) {
	            log.error("Error processing Synergy tickets", e);
	            throw new RuntimeException("Failed to process Synergy tickets", e);
	        }
	    }
	    
	    // Process HIMS ATMs
	    if (!himsAtmList.isEmpty()) {
	        try {
	            log.info("Processing HIMS tickets for {} ATMs", himsAtmList.size());
	            
	            // Process HIMS tickets in batches for better performance
	            List<AtmDetailsWithTickets> himsAtmDetails = processHimsTicketsInBatches(himsAtmList, batchSize);
	            consolidatedAtmDetails.addAll(himsAtmDetails);
	            
	            log.info("Successfully processed {} HIMS ATM details", himsAtmDetails.size());
	            
	        } catch (Exception e) {
	            log.error("Error processing HIMS tickets", e);
	            throw new RuntimeException("Failed to process HIMS tickets", e);
	        }
	    }
	    
	    // Set consolidated response
	    consolidatedResponse.setAtmdetails(consolidatedAtmDetails.isEmpty()?Collections.emptyList():consolidatedAtmDetails);
	    consolidatedResponse.setRequestid("COMBINED-" + System.currentTimeMillis());
	    
	    log.info("Successfully consolidated {} total ATM details from both sources", consolidatedAtmDetails.size());
	    
	    return consolidatedResponse;
	}

	// Helper method to process HIMS tickets in batches for better performance
	private List<AtmDetailsWithTickets> processHimsTicketsInBatches(List<AtmDetailsWithSourceDto> himsAtmList, int batchSize) {
	    List<AtmDetailsWithTickets> himsAtmDetails = new ArrayList<>();
	    
	    try {
	        // Get all HIMS tickets for the logged-in user
	        List<OpenTicketHimsView> allHimsTickets = openTicketHimsViewRepository.getOpenTicketListHims(helper.getLoggedInUser());
	        log.info("Retrieved {} HIMS tickets for user", allHimsTickets.size());
	        
	        if (allHimsTickets.isEmpty()) {
	            log.info("No HIMS tickets found for user");
	            return himsAtmDetails;
	        }
	        
	        // Group HIMS tickets by equipment ID for efficient lookup
	        Map<String, List<OpenTicketHimsView>> himsTicketsByAtm = allHimsTickets.stream()
	                .collect(Collectors.groupingBy(OpenTicketHimsView::getEquipmentid));
	        
	        // Calculate batches for HIMS processing
	        int numBatches = (himsAtmList.size() + batchSize - 1) / batchSize;
	        log.info("Processing HIMS ATMs in {} batches", numBatches);
	        
	        ExecutorService executor = Executors.newFixedThreadPool(5); // Smaller pool for HIMS processing
	        
	        try {
	            List<Future<List<AtmDetailsWithTickets>>> futures = new ArrayList<>();
	            
	            for (int i = 0; i < numBatches; i++) {
	                int startIdx = i * batchSize;
	                int endIdx = Math.min((i + 1) * batchSize, himsAtmList.size());
	                
	                List<AtmDetailsWithSourceDto> himsBatch = himsAtmList.subList(startIdx, endIdx);
	                
	                Callable<List<AtmDetailsWithTickets>> task = () -> {
	                    List<AtmDetailsWithTickets> batchResults = new ArrayList<>();
	                    
	                    for (AtmDetailsWithSourceDto himsAtm : himsBatch) {
	                        try {
	                            String atmId = himsAtm.getAtmid();
	                            List<OpenTicketHimsView> atmTickets = himsTicketsByAtm.getOrDefault(atmId, new ArrayList<>());
	                            
	                            // Convert HIMS tickets to TicketDetailsDto
	                            List<TicketDetailsDto> ticketDetails = atmTickets.stream()
	                                    .map(this::convertHimsToTicketDetails)
	                                    .toList();
	                            
	                            AtmDetailsWithTickets atmWithTickets = new AtmDetailsWithTickets();
	                            atmWithTickets.setAtmid(atmId);
	                            atmWithTickets.setOpenticketdetails(ticketDetails);
	                            
	                            batchResults.add(atmWithTickets);
	                            
	                        } catch (Exception e) {
	                            log.error("Error processing HIMS ATM: {}", himsAtm.getAtmid(), e);
	                            // Continue processing other ATMs in the batch
	                        }
	                    }
	                    
	                    return batchResults;
	                };
	                
	                futures.add(executor.submit(task));
	            }
	            
	            // Collect results from all batches
	            for (int i = 0; i < futures.size(); i++) {
	                try {
	                    List<AtmDetailsWithTickets> batchResults = futures.get(i).get();
	                    himsAtmDetails.addAll(batchResults);
	                    log.info("Successfully processed HIMS batch {}", i + 1);
	                } catch (Exception e) {
	                    log.error("Error getting results from HIMS batch {}", i + 1, e);
	                    // Continue processing other batches
	                }
	            }
	            
	        } finally {
	            executor.shutdown();
	            try {
	                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
	                    executor.shutdownNow();
	                    log.warn("HIMS executor did not terminate gracefully");
	                }
	            } catch (InterruptedException e) {
	                executor.shutdownNow();
	                Thread.currentThread().interrupt();
	            }
	        }
	        
	    } catch (Exception e) {
	        log.error("Error in HIMS ticket processing", e);
	        throw new RuntimeException("Failed to process HIMS tickets in batches", e);
	    }
	    
	    return himsAtmDetails;
	}

	// Helper method to convert OpenTicketHimsView to TicketDetailsDto
	private TicketDetailsDto convertHimsToTicketDetails(OpenTicketHimsView himsView) {
	    try {
	        TicketDetailsDto ticketDetails = new TicketDetailsDto();
	        
	        ticketDetails.setSrno(himsView.getSrno()); // Direct assignment since both are String now
	        ticketDetails.setCustomer(himsView.getCustomer());
	        ticketDetails.setEquipmentid(himsView.getEquipmentid());
	        ticketDetails.setModel(himsView.getModel());
	        ticketDetails.setAtmcategory(himsView.getAtmcategory());
	        ticketDetails.setAtmclassification(himsView.getAtmclassification());
	        ticketDetails.setCalldate(himsView.getCalldate());
	        ticketDetails.setCreateddate(himsView.getCreateddate());
	        ticketDetails.setCalltype(himsView.getCalltype());
	        ticketDetails.setSubcalltype(himsView.getSubcalltype());
	        ticketDetails.setCompletiondatewithtime(himsView.getCompletiondatewithtime());
	        ticketDetails.setDowntimeinmins(himsView.getDowntimeinmins() != null ? himsView.getDowntimeinmins() : 0);
	        ticketDetails.setVendor(himsView.getVendor());
	        ticketDetails.setServicecode(himsView.getServicecode());
	        ticketDetails.setDiagnosis(himsView.getDiagnosis());
	        ticketDetails.setEventcode(himsView.getEventcode());
	        ticketDetails.setHelpdeskname(himsView.getHelpdeskname());
	        ticketDetails.setLastallocatedtime(himsView.getLastallocatedtime());
	        ticketDetails.setLastcomment(himsView.getLastcomment());
	        ticketDetails.setLastactivity(himsView.getLastactivity());
	        ticketDetails.setStatus(himsView.getStatus());
	        ticketDetails.setSubstatus(himsView.getSubstatus());
	        ticketDetails.setRo(himsView.getRo());
	        ticketDetails.setSite(himsView.getSite());
	        ticketDetails.setAddress(himsView.getAddress());
	        ticketDetails.setCity(himsView.getCity());
	        ticketDetails.setLocationname(himsView.getLocationname());
	        ticketDetails.setState(himsView.getState());
	        ticketDetails.setNextfollowup(himsView.getNextfollowup());
	        
	        // Set the missing HIMS-specific fields
	        ticketDetails.setEtadatetime(himsView.getEtadatetime());
	        ticketDetails.setOwner(himsView.getOwner());
	        ticketDetails.setCustomerRemark(himsView.getCustomerRemark());
	        
	        return ticketDetails;
	        
	    } catch (Exception e) {
	        log.error("Error converting HIMS ticket to TicketDetailsDto: {}", himsView, e);
	        // Return a basic ticket with minimal info rather than null
	        TicketDetailsDto errorTicket = new TicketDetailsDto();
	        errorTicket.setEquipmentid(himsView.getEquipmentid());
	        errorTicket.setStatus("ERROR_PROCESSING");
	        return errorTicket;
	    }
	}
}
