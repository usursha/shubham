package com.hpy.ops360.ticketing.service;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.AtmDetailsWithTickets;
import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;
import com.hpy.ops360.ticketing.cm.entity.OpenTicketHimsView;
import com.hpy.ops360.ticketing.config.AssetServiceClient;
import com.hpy.ops360.ticketing.dto.AtmDetailsWithSourceDto;
import com.hpy.ops360.ticketing.dto.TicketDetailsDto;
import com.hpy.ops360.ticketing.entity.AtmTicketEvent;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.AtmTicketEventRepository;
import com.hpy.ops360.ticketing.repository.BroadCategoryRepository;
import com.hpy.ops360.ticketing.repository.CheckAtmSourceRepository;
import com.hpy.ops360.ticketing.repository.ManualTicketValidationRepository;
import com.hpy.ops360.ticketing.repository.OpenTicketHimsViewRepository;
import com.hpy.ops360.ticketing.repository.TaskRepository;
import com.hpy.ops360.ticketing.repository.TickethistoryForCEHism;
import com.hpy.ops360.ticketing.response.OpenTicketsResponse;
import com.hpy.ops360.ticketing.utils.CustomDateFormattor;
import com.hpy.ops360.ticketing.utils.CustomDateFormattor.FormatStyle;
import com.hpy.ops360.ticketing.utils.TicketColorUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskService2 {
	
	@Autowired
	private  TaskRepository taskRepository;

	@Autowired
	private TicketColorUtils ticketColorUtils;

	@Autowired
	private  LoginService loginService;

	@Autowired
	private  SynergyService synergyService;

	@Autowired
	private  AtmTicketEventRepository atmTicketEventRepository;

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
	
	@Autowired
	private OpenTicketHimsViewRepository openTicketHimsViewRepository; 
	
	
//	public List<AtmShortDetailsDto> getTicketList(List<AtmDetailsWithSourceDto> atms, int maxLimit,String CeId) {
//	    // Convert AtmDetailsDto to AtmDetailsWithSourceDto for batch processing
//	    List<AtmDetailsWithSourceDto> atmIdDtoList = atms.stream()
//	            .map(dto -> new AtmDetailsWithSourceDto(dto.getAtmid(), dto.getSource()))
//	            .toList();
//
////		OpenTicketsResponse openTicketsResponse = synergyService.getOpenTicketDetailsHims(atms);
////		if (openTicketsResponse.getAtmdetails().isEmpty()) {
////			return Collections.emptyList();
////		}
//
//	    // Use the batch processing method with a specified batch size (e.g., 100)
//		System.out.println(atms);
//	    OpenTicketsResponse openTicketsResponse = getOpenTicketDetailsInBatchesWithSource(atmIdDtoList, maxLimit,CeId);
//
//	    if (openTicketsResponse.getAtmdetails().isEmpty()) {
//	        return Collections.emptyList();
//	    }
//
//	    Set<String> atmTicketEventCodes = ConcurrentHashMap.newKeySet();
//	    Map<String, TicketDetailsDto> ticketDetailsMap = new ConcurrentHashMap<>();
//
//	    openTicketsResponse.getAtmdetails().parallelStream().forEach(atmDetail -> {
//	        atmDetail.getOpenticketdetails().stream().forEach(ticketDetailsDto -> {
//	            String uniqueCode = String.format("%s|%s|%s|%s|%s", ticketDetailsDto.getEquipmentid(),
//	                    ticketDetailsDto.getSrno(), ticketDetailsDto.getEventcode(), ticketDetailsDto.getNextfollowup(),
//	                    ticketDetailsDto.getCalldate());
//
//	            atmTicketEventCodes.add(uniqueCode);
//	            ticketDetailsMap.put(ticketDetailsDto.getEquipmentid() + ticketDetailsDto.getSrno(), ticketDetailsDto);
//	        });
//	    });
//
//	    String atmTicketEventCodeList = String.join(",", atmTicketEventCodes);
//
//	    List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
//	            .getAtmTicketEvent(loginService.getLoggedInUser(), atmTicketEventCodeList);
//	    log.info("event atmTicketEventCodeList ===================" + atmTicketEventCodeList);
//
//	    List<AtmShortDetailsDto> downCallList = atmTicketEventList.parallelStream().map(atmTicketEvent -> {
//	        TicketDetailsDto ticketDetailsDto = ticketDetailsMap
//	                .get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());
//
//	        AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
//	                .requestid(openTicketsResponse.getRequestid())
//	                .atmId(ticketDetailsDto.getEquipmentid())
//	                .ticketNumber(ticketDetailsDto.getSrno())
//	                .bank(ticketDetailsDto.getCustomer())
//	                .siteName(ticketDetailsDto.getEquipmentid())
//	                .owner(atmTicketEvent.getOwner().isEmpty() ? "" : atmTicketEvent.getOwner())
//	                .subcall(ticketDetailsDto.getSubcalltype())
//	                .vendor(ticketDetailsDto.getVendor())
//	                .error(atmTicketEvent.getEventCode() == null ? "Default" : atmTicketEvent.getEventCode())
//	                .downTime(getDownTimeInHrs(ticketDetailsDto.getCreateddate()))
//	                .priorityScore(atmTicketEvent.getPriorityScore())
//	                .eventGroup(atmTicketEvent.getEventGroup())
//	                .isBreakdown(atmTicketEvent.getIsBreakdown() == null ? 0 : atmTicketEvent.getIsBreakdown())
//	                .isUpdated(atmTicketEvent.getIsUpdated() == null ? 0 : atmTicketEvent.getIsUpdated())
//	                .isTimedOut(atmTicketEvent.getIsTimedOut() == null ? 0 : atmTicketEvent.getIsTimedOut())
//	                .isTravelling(atmTicketEvent.getIsTravelling() == null ? 0 : atmTicketEvent.getIsTravelling())
//	                .travelTime(atmTicketEvent.getTravelTime())
//	                .travelEta(atmTicketEvent.getTravelEta() == null ? 0 : atmTicketEvent.getTravelEta())
//	                .downCall(atmTicketEvent.getDownCall() == null ? 0 : atmTicketEvent.getDownCall())
//	                .etaDateTime(atmTicketEvent.getEtaDateTime() == null ? "" : atmTicketEvent.getEtaDateTime())
//	                .etaTimeout(atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout())
//	                .CreatedDate(CustomDateFormattor.convert(ticketDetailsDto.getCreateddate(), FormatStyle.DATABASE)
//	                		)
//	                .flagStatus(atmTicketEvent.getFlagStatus())
//	                .flagStatusInsertTime(atmTicketEvent.getFlagStatusInsertTime())
//	                .ceName(atmTicketEvent.getCeName())
//	                .build();
//
//	        // Apply color logic
//	        atmShortDetailsDto.setColor(ticketColorUtils.getColor(atmShortDetailsDto.getIsUpdated(),
//	                atmShortDetailsDto.getIsTimedOut(), atmShortDetailsDto.getIsTravelling()));
//
//	        return atmShortDetailsDto;
//	    }).sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()).toList();
//
//	    return downCallList.stream()
//	            .filter(e -> e.getDownCall() == 1)
//	            .sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()
//	                    .thenComparing(AtmShortDetailsDto::getDownTime).reversed())
//	            .limit(maxLimit)
//	            .toList();
//	}

	
	public List<AtmShortDetailsDto> getTicketList(List<AtmDetailsWithSourceDto> atms, int maxLimit, String ceId) {
	    // Convert AtmDetailsDto to AtmDetailsWithSourceDto for batch processing
	    List<AtmDetailsWithSourceDto> atmIdDtoList = atms.stream()
	            .map(dto -> new AtmDetailsWithSourceDto(dto.getAtmid(), dto.getSource()))
	            .toList();

	    log.info("Fetching open ticket details for {} ATMs", atmIdDtoList.size());
	    OpenTicketsResponse openTicketsResponse = getOpenTicketDetailsInBatchesWithSource(atmIdDtoList, maxLimit, ceId);

	    if (openTicketsResponse.getAtmdetails().isEmpty()) {
	        log.warn("No ATM details found in the response for CE ID: {}", ceId);
	        return Collections.emptyList();
	    }

	    Set<String> atmTicketEventCodes = ConcurrentHashMap.newKeySet();
	    Map<String, TicketDetailsDto> ticketDetailsMap = new ConcurrentHashMap<>();

	    openTicketsResponse.getAtmdetails().parallelStream().forEach(atmDetail -> {
	        atmDetail.getOpenticketdetails().forEach(ticketDetailsDto -> {
	            String uniqueCode = String.format("%s|%s|%s|%s|%s", ticketDetailsDto.getEquipmentid(),
	                    ticketDetailsDto.getSrno(), ticketDetailsDto.getEventcode(), ticketDetailsDto.getNextfollowup(),
	                    ticketDetailsDto.getCalldate());

	            atmTicketEventCodes.add(uniqueCode);
	            ticketDetailsMap.put(ticketDetailsDto.getEquipmentid() + ticketDetailsDto.getSrno(), ticketDetailsDto);
	        });
	    });

	    String atmTicketEventCodeList = String.join(",", atmTicketEventCodes);
	    List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
	            .getAtmTicketEvent(loginService.getLoggedInUser (), atmTicketEventCodeList);
	    log.info("Event ATM Ticket Event Code List: {}", atmTicketEventCodeList);

	    List<AtmShortDetailsDto> downCallList = atmTicketEventList.parallelStream().map(atmTicketEvent -> {
	        TicketDetailsDto ticketDetailsDto = ticketDetailsMap.get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());

	        if (ticketDetailsDto == null) {
	            log.warn("No ticket details found for ATM ID: {} and Ticket ID: {}", atmTicketEvent.getAtmId(), atmTicketEvent.getTicketId());
	            return null; // Handle missing ticket details gracefully
	        }

	        AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
	                .requestid(openTicketsResponse.getRequestid())
	                .atmId(ticketDetailsDto.getEquipmentid())
	                .ticketNumber(ticketDetailsDto.getSrno())
	                .bank(ticketDetailsDto.getCustomer())
	                .siteName(ticketDetailsDto.getEquipmentid())
	                .owner(atmTicketEvent.getOwner().isEmpty() ? "" : atmTicketEvent.getOwner())
	                .subcall(ticketDetailsDto.getSubcalltype())
	                .vendor(ticketDetailsDto.getVendor())
	                .error(atmTicketEvent.getEventCode() == null ? "Default" : atmTicketEvent.getEventCode())
	                .downTime(getDownTimeInHrs(ticketDetailsDto.getCreateddate()))
	                .priorityScore(atmTicketEvent.getPriorityScore())
	                .eventGroup(atmTicketEvent.getEventGroup())
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
	                .flagStatusInsertTime(atmTicketEvent.getFlagStatusInsertTime())
	                .ceName(atmTicketEvent.getCeName())
	                .build();

	        // Apply color logic
	        atmShortDetailsDto.setColor(ticketColorUtils.getColor(atmShortDetailsDto.getIsUpdated(),
	                atmShortDetailsDto.getIsTimedOut(), atmShortDetailsDto.getIsTravelling()));

	        return atmShortDetailsDto;
	    }).filter(Objects::nonNull) // Filter out nulls from missing ticket details
	    .sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()).toList();

	    return downCallList.stream()
	            .filter(e -> e.getDownCall() == 1)
	            .sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()
	                    .thenComparing(AtmShortDetailsDto::getDownTime).reversed())
	            .limit(maxLimit)
	            .toList();
	}

	
	
	
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
	
	
	
	
	@Loggable
	public OpenTicketsResponse getOpenTicketDetailsInBatchesWithSource(List<AtmDetailsWithSourceDto> atmIdDtoList, int batchSize,String ceid) {

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
	                    List<AtmDetailsWithSourceDto> atms = atmBatchWithSource.stream()
	                            .map(atm -> new AtmDetailsWithSourceDto(atm.getAtmid(),atm.getSource()))
	                            .toList();
	                    
	                    Callable<OpenTicketsResponse> task = () -> {
	                        try {
	                            return synergyService.getOpenTicketDetailsHims(atms);
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
	            List<AtmDetailsWithTickets> himsAtmDetails = processHimsTicketsInBatches(himsAtmList, ceid);
	            consolidatedAtmDetails.addAll(himsAtmDetails);
	            
	            log.info("Successfully processed {} HIMS ATM details", himsAtmDetails.size());
	            
	        } catch (Exception e) {
	            log.error("Error processing HIMS tickets", e);
	            throw new RuntimeException("Failed to process HIMS tickets", e);
	        }
	    }
	    
	    // Set consolidated response
	    consolidatedResponse.setAtmdetails(consolidatedAtmDetails);
	    consolidatedResponse.setRequestid("COMBINED-" + System.currentTimeMillis());
	    
	    log.info("Successfully consolidated {} total ATM details from both sources", consolidatedAtmDetails.size());
	    
	    return consolidatedResponse;
	}

	// Helper method to process HIMS tickets in batches for better performance
//	private List<AtmDetailsWithTickets> processHimsTicketsInBatches(List<AtmDetailsWithSourceDto> himsAtmList, int batchSize,String ceid) {
//	    List<AtmDetailsWithTickets> himsAtmDetails = new ArrayList<>();
//	    log.info("Number of hims Atm Lists found for user---------------------"+himsAtmList);
//	    try {
//	        // Get all HIMS tickets for the logged-in user
//	        List<OpenTicketHimsView> allHimsTickets = openTicketHimsViewRepository.getOpenTicketListHims(ceid);
//	        log.info("Retrieved {} HIMS tickets for user----------"+ceid, allHimsTickets.size());
//	        
//	        if (allHimsTickets.isEmpty()) {
//	            log.info("No HIMS tickets found for user");
//	            return himsAtmDetails;
//	        }
//	        
//	        // Group HIMS tickets by equipment ID for efficient lookup
//	        Map<String, List<OpenTicketHimsView>> himsTicketsByAtm = allHimsTickets.stream()
//	                .collect(Collectors.groupingBy(OpenTicketHimsView::getEquipmentid));
//	        
//	        // Calculate batches for HIMS processing
//	        int numBatches = (himsAtmList.size() + batchSize - 1) / batchSize;
//	        log.info("Processing HIMS ATMs in {} batches", numBatches);
//	        
//	        ExecutorService executor = Executors.newFixedThreadPool(5); // Smaller pool for HIMS processing
//	        
//	        try {
//	            List<Future<List<AtmDetailsWithTickets>>> futures = new ArrayList<>();
//	            
//	            for (int i = 0; i < numBatches; i++) {
//	                int startIdx = i * batchSize;
//	                int endIdx = Math.min((i + 1) * batchSize, himsAtmList.size());
//	                
//	                List<AtmDetailsWithSourceDto> himsBatch = himsAtmList.subList(startIdx, endIdx);
//	                
//	                Callable<List<AtmDetailsWithTickets>> task = () -> {
//	                    List<AtmDetailsWithTickets> batchResults = new ArrayList<>();
//	                    
//	                    for (AtmDetailsWithSourceDto himsAtm : himsBatch) {
//	                        try {
//	                            String atmId = himsAtm.getAtmid();
//	                            List<OpenTicketHimsView> atmTickets = himsTicketsByAtm.getOrDefault(atmId, new ArrayList<>());
//	                            
//	                            // Convert HIMS tickets to TicketDetailsDto
//	                            List<TicketDetailsDto> ticketDetails = atmTickets.stream()
//	                                    .map(this::convertHimsToTicketDetails)
//	                                    .toList();
//	                            
//	                            AtmDetailsWithTickets atmWithTickets = new AtmDetailsWithTickets();
//	                            atmWithTickets.setAtmid(atmId);
//	                            atmWithTickets.setOpenticketdetails(ticketDetails);
//	                            
//	                            batchResults.add(atmWithTickets);
//	                            
//	                        } catch (Exception e) {
//	                            log.error("Error processing HIMS ATM: {}", himsAtm.getAtmid(), e);
//	                            // Continue processing other ATMs in the batch
//	                        }
//	                    }
//	                    
//	                    return batchResults;
//	                };
//	                
//	                futures.add(executor.submit(task));
//	            }
//	            
//	            // Collect results from all batches
//	            for (int i = 0; i < futures.size(); i++) {
//	                try {
//	                    List<AtmDetailsWithTickets> batchResults = futures.get(i).get();
//	                    himsAtmDetails.addAll(batchResults);
//	                    log.info("Successfully processed HIMS batch {}", i + 1);
//	                } catch (Exception e) {
//	                    log.error("Error getting results from HIMS batch {}", i + 1, e);
//	                    // Continue processing other batches
//	                }
//	            }
//	            
//	        } finally {
//	            executor.shutdown();
//	            try {
//	                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
//	                    executor.shutdownNow();
//	                    log.warn("HIMS executor did not terminate gracefully");
//	                }
//	            } catch (InterruptedException e) {
//	                executor.shutdownNow();
//	                Thread.currentThread().interrupt();
//	            }
//	        }
//	        
//	    } catch (Exception e) {
//	        log.error("Error in HIMS ticket processing", e);
//	        throw new RuntimeException("Failed to process HIMS tickets in batches", e);
//	    }
//	    
//	    return himsAtmDetails;
//	}
	
	// Optimized HIMS processor
	
//	private List<AtmDetailsWithTickets> processHimsTicketsInBatches(List<AtmDetailsWithSourceDto> himsAtmList, String ceid) {
//	    // Normalize ATM IDs for case-insensitive matching
//	    Set<String> normalizedAtmIds = himsAtmList.stream()
//	        .map(atm -> atm.getAtmid().trim().toUpperCase())
//	        .collect(Collectors.toSet());
//	    
//	    log.info("Fetching HIMS tickets for {} ATMs", normalizedAtmIds.size());
//	    
//	    // Fetch ALL tickets without CEID filtering
//	    List<OpenTicketHimsView> allTickets = openTicketHimsViewRepository.getOpenTicketListHims(ceid);
//	    log.info("Total HIMS tickets found: {}", allTickets.size());
//	    
//	    // Filter tickets and convert them to TicketDetailsDto directly
//	    List<TicketDetailsDto> ticketDetailsList = allTickets.stream()
//	        .filter(ticket -> normalizedAtmIds.contains(ticket.getEquipmentid().trim().toUpperCase()))
//	        .map(this::convertHimsToTicketDetails)
//	        .filter(Objects::nonNull) // Skip any null conversions
//	        .collect(Collectors.toList());
//	    
//	    log.info("Converted {} relevant tickets to TicketDetailsDto", ticketDetailsList.size());
//	    
//	    // Process each ATM and associate ticket details
//	    List<AtmDetailsWithTickets> results = new ArrayList<>();
//	    for (AtmDetailsWithSourceDto atm : himsAtmList) {
//	        String normalizedId = atm.getAtmid().trim().toUpperCase();
//	        
//	        // Filter ticket details for the current ATM
//	        List<TicketDetailsDto> atmTicketDetails = ticketDetailsList.stream()
//	            .filter(dto -> dto.getEquipmentid().trim().toUpperCase().equals(normalizedId))
//	            .collect(Collectors.toList());
//	        
//	        AtmDetailsWithTickets atmWithTickets = new AtmDetailsWithTickets();
//	        atmWithTickets.setAtmid(atm.getAtmid()); // preserve original casing
//	        atmWithTickets.setOpenticketdetails(atmTicketDetails);
//	        results.add(atmWithTickets);
//	        
//	        // Diagnostic logging
//	        if (atmTicketDetails.isEmpty()) {
//	            log.debug("No tickets found for ATM: {}", atm.getAtmid());
//	        } else {
//	            log.debug("Found {} tickets for ATM: {}", atmTicketDetails.size(), atm.getAtmid());
//	        }
//	    }
//	    
//	    return results;
//	}

	
	/*
	1)   ---------------------------------------------------------------
	
	*/
	
	
	
	
//	private List<AtmDetailsWithTickets> processHimsTicketsInBatches(List<AtmDetailsWithSourceDto> himsAtmList, String ceid) {
//	    // Normalize ATM IDs for case-insensitive matching
//	    Set<String> normalizedAtmIds = himsAtmList.stream()
//	        .map(atm -> atm.getAtmid().trim().toUpperCase())
//	        .collect(Collectors.toSet());
//	    
//	    log.info("Fetching HIMS tickets for {} ATMs", normalizedAtmIds.size());
//	     
//	    
//	    // Fetch ALL tickets without CEID filtering
//	    List<OpenTicketHimsView> allTickets = openTicketHimsViewRepository.getOpenTicketListHims(ceid);
//	    log.info("Total HIMS tickets found: {}", allTickets.size());
//	    
//	    // Filter and group tickets by normalized ATM ID
//	    Map<String, List<OpenTicketHimsView>> ticketsByAtm = allTickets.stream()
//	        .filter(ticket -> normalizedAtmIds.contains(ticket.getEquipmentid().trim().toUpperCase()))
//	        .collect(Collectors.groupingBy(t -> t.getEquipmentid().trim().toUpperCase()));
//	    
//	    log.info("Relevant tickets grouped for {} ATMs", ticketsByAtm.size());
//	    
////	    Map<String, List<TicketDetailsDto>> ticketsByAtm = new HashMap<>();
////
////	    allTickets.forEach(ticket -> {
////	        String key = ticket.getEquipmentid().trim().toUpperCase();
////	        if (normalizedAtmIds.contains(key)) {
////	            TicketDetailsDto dto = convertHimsToTicketDetails(ticket);
////	            if (dto != null) {
////	                ticketsByAtm.merge(
////	                    key,
////	                    new ArrayList<>(Collections.singletonList(dto)),
////	                    (existing, newList) -> {
////	                        existing.addAll(newList);
////	                        return existing;
////	                    }
////	                );
////	            }
////	        }
////	    });
//
//	    return himsAtmList.stream()
//	    	    .map(atm -> {
//	    	        String normalizedId = atm.getAtmid().trim().toUpperCase();
//	    	        
//	    	        AtmDetailsWithTickets result = new AtmDetailsWithTickets();
//	    	        result.setAtmid(atm.getAtmid());
//	    	        
//	    	        // Filter tickets for this ATM using stream
//	    	        List<TicketDetailsDto> ticketDetails = allTickets.stream()
//
//	    	        		.filter(t -> normalizedId.equals(t.getEquipmentid().trim().toUpperCase()))
//	    	            .map(this::convertHimsToTicketDetails)
//	    	            .filter(Objects::nonNull)
//	    	            .toList();
//	    	        
//	    	        result.setOpenticketdetails(ticketDetails);
//	    	        return result;
//	    	    })
//	    	    .toList();
//	}
	
	
	
	
	
	
	private List<AtmDetailsWithTickets> processHimsTicketsInBatches(List<AtmDetailsWithSourceDto> himsAtmList, String ceid) {
	    // Normalize ATM IDs for case-insensitive matching
	    Set<String> normalizedAtmIds = himsAtmList.stream()
	        .map(atm -> atm.getAtmid().trim().toUpperCase())
	        .collect(Collectors.toSet());

	    log.info("Fetching HIMS tickets for {} ATMs", normalizedAtmIds.size());

	    // Fetch ALL tickets without CEID filtering
	    List<OpenTicketHimsView> allTickets = openTicketHimsViewRepository.getOpenTicketListHims(ceid);
	    log.info("Total HIMS tickets found: {}", allTickets.size());

	    // Filter and group tickets by normalized ATM ID
	    Map<String, List<TicketDetailsDto>> ticketsByAtm = allTickets.stream()
	        .filter(ticket -> normalizedAtmIds.contains(ticket.getEquipmentid().trim().toUpperCase()))
	        .map(this::convertHimsToTicketDetails) // Convert to TicketDetailsDto
	        .filter(Objects::nonNull) // Skip null conversions
	        .collect(Collectors.groupingBy(ticket -> ticket.getEquipmentid().trim().toUpperCase()));

	    log.info("Relevant tickets grouped for {} ATMs", ticketsByAtm.size());

	    // Process each ATM and associate ticket details
	    return himsAtmList.stream()
	        .map(atm -> {
	            String normalizedId = atm.getAtmid().trim().toUpperCase();
	            AtmDetailsWithTickets result = new AtmDetailsWithTickets();
	            result.setAtmid(atm.getAtmid()); // Preserve original casing

	            // Get the ticket details for this ATM from the grouped map
	            List<TicketDetailsDto> ticketDetails = ticketsByAtm.getOrDefault(normalizedId, Collections.emptyList());
	            result.setOpenticketdetails(ticketDetails);
	            return result;
	        })
	        .toList();
	}


	 // Process each ATM using for-each
//	 // Process each ATM using for-each
//        List<AtmDetailsWithTickets> results = new ArrayList<>();
//        int atmsWithNoTickets = 0; // For logging/tracking
//
//        for (int i = 0; i < himsAtmList.size(); i++) { // Using a traditional for loop as per your snippet
//            AtmDetailsWithSourceDto atm = himsAtmList.get(i);
//            String originalAtmid = atm.getAtmid();
//
//            // Handle potential null ATM ID from input list gracefully
//            if (originalAtmid == null || originalAtmid.trim().isEmpty()) {
//                log.warn("Skipping ATM at index {} with null or empty ATMID from input list.", i);
//                // Optionally add an entry for this ATM with empty tickets
//                AtmDetailsWithTickets atmWithTickets = new AtmDetailsWithTickets();
//                atmWithTickets.setAtmid(originalAtmid);
//                atmWithTickets.setOpenticketdetails(Collections.emptyList());
//                results.add(atmWithTickets);
//                continue; // Skip to the next ATM
//            }
//
//            String normalizedId = originalAtmid.trim().toUpperCase();
//
//            // Retrieve tickets for the current ATM, defaulting to an empty list if none found
//            //List<OpenTicketHimsView> atmTickets = ticketsByAtm.getOrDefault(normalizedId, Collections.emptyList());
//            log.debug("Normalized ID being looked up: {}", normalizedId);
//            List<OpenTicketHimsView> atmTickets = ticketsByAtm.get(normalizedId);
//            AtmDetailsWithTickets atmWithTickets = new AtmDetailsWithTickets();
//            atmWithTickets.setAtmid(originalAtmid); // Preserve original casing
//
//            // List to hold converted TicketDetailsDto objects for this ATM
//            List<TicketDetailsDto> ticketDetails = new ArrayList<>();
//
//            // Iterate through the tickets found for this ATM and convert them
//            for (OpenTicketHimsView ticket : atmTickets) {
//                TicketDetailsDto dto = convertHimsToTicketDetails(ticket); // <<<<<< CALL TO HELPER METHOD HERE
//                if (dto != null) { // Only add if conversion was successful
//                    ticketDetails.add(dto);
//                } else {
//                    log.warn("Conversion of HIMS ticket to TicketDetailsDto failed for ticket ID: {}", ticket.getSrno());
//                }
//            }
//
//            // Set the list of converted ticket details to the ATM object
//            atmWithTickets.setOpenticketdetails(ticketDetails);
//            results.add(atmWithTickets);
//
//            // Diagnostic logging for each ATM
//            if (ticketDetails.isEmpty()) {
//                log.debug("ATM {} (normalized {}) had no matching tickets after conversion.", originalAtmid, normalizedId);
//                atmsWithNoTickets++;
//            } else {
//                log.debug("ATM {} (normalized {}) found {} matching tickets after conversion.", originalAtmid, normalizedId, ticketDetails.size());
//            }
//        }
//        log.info("Finished processing {} ATMs. {} ATMs had no matching tickets.", himsAtmList.size(), atmsWithNoTickets);
//
//        return results;
	

	// Helper method to convert OpenTicketHimsView to TicketDetailsDto
	private TicketDetailsDto convertHimsToTicketDetails(OpenTicketHimsView himsView) {
	    
		 log.info("  HIMS ticket himsView {} ",himsView.getEquipmentid());
		try {
	    	
	        TicketDetailsDto ticketDetails = new TicketDetailsDto();
	        
	        ticketDetails.setSrno(himsView.getSrno()); // Direct assignment since both are String now
	        ticketDetails.setCustomer(himsView.getCustomer());
	        String normalizedId = himsView.getEquipmentid() != null 
	                ? himsView.getEquipmentid().trim().toUpperCase() 
	                : "UNKNOWN";
	            ticketDetails.setEquipmentid(normalizedId);
	        ticketDetails.setEquipmentid(himsView.getEquipmentid());
	        ticketDetails.setModel(himsView.getModel());
	        ticketDetails.setAtmcategory(himsView.getAtmcategory());
	        ticketDetails.setAtmclassification(himsView.getAtmclassification());
	        ticketDetails.setCalldate(himsView.getCalldate());
	        ticketDetails.setCreateddate(CustomDateFormattor.convert(himsView.getCreateddate(), FormatStyle.DATABASE));
	        ticketDetails.setCalltype(himsView.getCalltype());
	        ticketDetails.setSubcalltype(himsView.getSubcalltype());
	        //ticketDetails.setCompletiondatewithtime(himsView.getCompletiondatewithtime());
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
	
	
	
	//--------------------	my logic -------------------------------------------

	
//	@Loggable
//	public List<AtmShortDetailsDto> getTicketList(List<AtmDetailsWithSourceDto> atmsWithSource, int maxLimit) {
//	    log.info("Fetching ticket list for {} ATMs with source information. Input ATMs: {}", atmsWithSource.size(), atmsWithSource);
//
//		List<AtmShortDetailsDto> allTickets = new ArrayList<>();
//
//		System.out.println("Atm List with source-------------"+atmsWithSource);
//		// Group ATMs by source
//		Map<String, List<AtmDetailsWithSourceDto>> atmsBySource = atmsWithSource.stream()
//				.collect(Collectors.groupingBy(AtmDetailsWithSourceDto::getSource));
//
//		// Process Synergy tickets if any
//		List<AtmDetailsWithSourceDto> synergyAtms = atmsBySource.getOrDefault("synergy", Collections.emptyList());
//		if (!synergyAtms.isEmpty()) {
//			log.info("Fetching Synergy tickets for {} ATMs.", synergyAtms.size());
//			// Convert to List<AtmDetailsDto> as synergyService.getOpenTicketDetails expects this type
//			List<AtmDetailsDto> atmDetailsForSynergy = synergyAtms.stream()
//					.map(atm -> new AtmDetailsDto(atm.getAtmid()))
//					.collect(Collectors.toList());
//			OpenTicketsResponse synergyResponse = synergyService.getOpenTicketDetails(atmDetailsForSynergy);
//			if (synergyResponse != null && !synergyResponse.getAtmdetails().isEmpty()) {
//				allTickets.addAll(processSynergyTickets(synergyResponse));
//			}
//		}
//
//		// Process HIMS tickets if any
//		List<AtmDetailsWithSourceDto> himsAtms = atmsBySource.getOrDefault("hims", Collections.emptyList());
//		if (!himsAtms.isEmpty()) {
//			log.info("Fetching HIMS tickets for {} ATMs.", himsAtms.size());
//			// Retrieve all open HIMS tickets for the logged-in user
//			List<OpenTicketHimsView> allHimsTicketsForUser = openTicketHimsViewRepository.getOpenTicketListHims(loginService.getLoggedInUser());
//
//			// Filter the retrieved HIMS tickets to only include those from the current `himsAtms` list
////			Set<String> himsAtmIdsInRequest = himsAtms.stream()
////					.map(AtmDetailsWithSourceDto::getAtmid)
////					.collect(Collectors.toSet());
////
////			List<OpenTicketHimsView> filteredHimsTickets = allHimsTicketsForUser.stream()
////					.filter(ticket -> himsAtmIdsInRequest.contains(ticket.getEquipmentid()))
////					.collect(Collectors.toList());
//
//			allTickets.addAll(processHimsTickets(allHimsTicketsForUser));
//		}
//
//		log.info("Total retrieved tickets across all sources: {}", allTickets.size());
//
//		// Apply common filtering and sorting for all collected tickets
//		return allTickets.stream()
//				.filter(e -> e.getDownCall() == 1) // Assuming 'down call' is a universal filter
//				.sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()
//						.thenComparing(AtmShortDetailsDto::getDownTime).reversed())
//				.limit(maxLimit)
//				.collect(Collectors.toList());
//	}
//
//	@Loggable
//	public List<AtmShortDetailsDto> getTicketListHims(List<AtmDetailsWithSourceDto> atmsWithSource, int maxLimit,String CeId) {
//	    log.info("Fetching ticket list for {} ATMs with source information. Input ATMs: {}", atmsWithSource.size(), atmsWithSource);
//
//		List<AtmShortDetailsDto> allTickets = new ArrayList<>();
//
//		System.out.println("Atm List with source-------------"+atmsWithSource);
//		// Group ATMs by source
//		Map<String, List<AtmDetailsWithSourceDto>> atmsBySource = atmsWithSource.stream()
//				.collect(Collectors.groupingBy(AtmDetailsWithSourceDto::getSource));
//
//		// Process Synergy tickets if any
//		List<AtmDetailsWithSourceDto> synergyAtms = atmsBySource.getOrDefault("synergy", Collections.emptyList());
//		if (!synergyAtms.isEmpty()) {
//			log.info("Fetching Synergy tickets for {} ATMs.", synergyAtms.size());
//			// Convert to List<AtmDetailsDto> as synergyService.getOpenTicketDetails expects this type
//			List<AtmDetailsDto> atmDetailsForSynergy = synergyAtms.stream()
//					.map(atm -> new AtmDetailsDto(atm.getAtmid()))
//					.collect(Collectors.toList());
//			OpenTicketsResponse synergyResponse = synergyService.getOpenTicketDetails(atmDetailsForSynergy);
//			if (synergyResponse != null && !synergyResponse.getAtmdetails().isEmpty()) {
//				allTickets.addAll(processSynergyTickets(synergyResponse));
//			}
//		}
//
//		// Process HIMS tickets if any
//		List<AtmDetailsWithSourceDto> himsAtms = atmsBySource.getOrDefault("hims", Collections.emptyList());
//		if (!himsAtms.isEmpty()) {
//			log.info("Fetching HIMS tickets for {} ATMs.", himsAtms.size());
//			// Retrieve all open HIMS tickets for the logged-in user
//			List<OpenTicketHimsView> allHimsTicketsForUser = openTicketHimsViewRepository.getOpenTicketListHims(CeId);
//
//			// Filter the retrieved HIMS tickets to only include those from the current `himsAtms` list
////			Set<String> himsAtmIdsInRequest = himsAtms.stream()
////					.map(AtmDetailsWithSourceDto::getAtmid)
////					.collect(Collectors.toSet());
////
////			List<OpenTicketHimsView> filteredHimsTickets = allHimsTicketsForUser.stream()
////					.filter(ticket -> himsAtmIdsInRequest.contains(ticket.getEquipmentid()))
////					.collect(Collectors.toList());
//
//			allTickets.addAll(processHimsTickets(allHimsTicketsForUser));
//		}
//
//		log.info("Total retrieved tickets across all sources: {}", allTickets.size());
//
//		// Apply common filtering and sorting for all collected tickets
//		return allTickets.stream()
//				.filter(e -> e.getDownCall() == 1) // Assuming 'down call' is a universal filter
//				.sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()
//						.thenComparing(AtmShortDetailsDto::getDownTime).reversed())
//				.limit(maxLimit)
//				.collect(Collectors.toList());
//	}
//	
//	
//	/**
//	 * Processes the OpenTicketsResponse from Synergy and maps it to AtmShortDetailsDto.
//	 *
//	 * @param openTicketsResponse The response object from Synergy API.
//	 * @return A list of AtmShortDetailsDto for Synergy tickets.
//	 */
//	private List<AtmShortDetailsDto> processSynergyTickets(OpenTicketsResponse openTicketsResponse) {
//		log.debug("Processing Synergy tickets.");
//		Set<String> atmTicketEventCodes = ConcurrentHashMap.newKeySet();
//		Map<String, TicketDetailsDto> ticketDetailsMap = new ConcurrentHashMap<>();
//
//		openTicketsResponse.getAtmdetails().parallelStream().forEach(atmDetail -> {
//			atmDetail.getOpenticketdetails().stream().forEach(ticketDetailsDto -> {
//				// Construct a unique code for each ticket event to query AtmTicketEventRepository
//				String uniqueCode = String.format("%s|%s|%s|%s|%s", ticketDetailsDto.getEquipmentid(),
//						ticketDetailsDto.getSrno(), ticketDetailsDto.getEventcode(),
//						ticketDetailsDto.getNextfollowup(), ticketDetailsDto.getCalldate());
//
//				atmTicketEventCodes.add(uniqueCode);
//				// Map using ATM ID + SRNO for quick lookup
//				ticketDetailsMap.put(ticketDetailsDto.getEquipmentid() + ticketDetailsDto.getSrno(), ticketDetailsDto);
//			});
//		});
//
//		String atmTicketEventCodeList = String.join(",", atmTicketEventCodes);
//		log.info("Synergy ticket event codes generated for AtmTicketEventRepository query: {}", atmTicketEventCodeList);
//
//		// Fetch additional event details from internal database
//		List<AtmTicketEvent> atmTicketEventList = atmTicketEventRepository
//				.getAtmTicketEvent(loginService.getLoggedInUser(), atmTicketEventCodeList);
//
//		return atmTicketEventList.parallelStream().map(atmTicketEvent -> {
//			// Retrieve the corresponding TicketDetailsDto from the map
//			TicketDetailsDto ticketDetailsDto = ticketDetailsMap
//					.get(atmTicketEvent.getAtmId() + atmTicketEvent.getTicketId());
//
//			// Build AtmShortDetailsDto from combined data
//			AtmShortDetailsDto atmShortDetailsDto = AtmShortDetailsDto.builder()
//					.requestid(openTicketsResponse.getRequestid()) // Assuming request ID from overall response
//					.atmId(ticketDetailsDto.getEquipmentid())
//					.ticketNumber(ticketDetailsDto.getSrno())
//					.bank(ticketDetailsDto.getCustomer())
//					.siteName(ticketDetailsDto.getEquipmentid()) // Assuming site name is ATM ID from Synergy
//					.owner(atmTicketEvent.getOwner().isEmpty() ? "" : atmTicketEvent.getOwner())
//					.subcall(ticketDetailsDto.getSubcalltype())
//					.vendor(ticketDetailsDto.getVendor())
//					.error(atmTicketEvent.getEventCode() == null ? "Default" : atmTicketEvent.getEventCode())
//					.downTime(getDownTimeInHrs(ticketDetailsDto.getCreateddate()))
//					.priorityScore(atmTicketEvent.getPriorityScore())
//					.eventGroup(atmTicketEvent.getEventGroup())
//					.isBreakdown(atmTicketEvent.getIsBreakdown() == null ? 0 : atmTicketEvent.getIsBreakdown())
//					.isUpdated(atmTicketEvent.getIsUpdated() == null ? 0 : atmTicketEvent.getIsUpdated())
//					.isTimedOut(atmTicketEvent.getIsTimedOut() == null ? 0 : atmTicketEvent.getIsTimedOut())
//					.isTravelling(atmTicketEvent.getIsTravelling() == null ? 0 : atmTicketEvent.getIsTravelling())
//					.travelTime(atmTicketEvent.getTravelTime())
//					.travelEta(atmTicketEvent.getTravelEta() == null ? 0 : atmTicketEvent.getTravelEta())
//					.downCall(atmTicketEvent.getDownCall() == null ? 0 : atmTicketEvent.getDownCall())
//					.etaDateTime(atmTicketEvent.getEtaDateTime() == null ? "" : atmTicketEvent.getEtaDateTime())
//					.etaTimeout(atmTicketEvent.getEtaTimeout() == null ? "" : atmTicketEvent.getEtaTimeout())
//					.CreatedDate(CustomDateFormattor.convert(ticketDetailsDto.getCreateddate(), FormatStyle.DATABASE))
//					.flagStatus(atmTicketEvent.getFlagStatus())
//					.flagStatusInsertTime(atmTicketEvent.getFlagStatusInsertTime())
//					.ceName(atmTicketEvent.getCeName())
//					.build();
//
//			// Apply color logic based on the calculated status flags
//			atmShortDetailsDto.setColor(ticketColorUtils.getColor(atmShortDetailsDto.getIsUpdated(),
//					atmShortDetailsDto.getIsTimedOut(), atmShortDetailsDto.getIsTravelling()));
//
//			return atmShortDetailsDto;
//		}).sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()).toList();
//	}
//
//	/**
//	 * Processes the list of OpenTicketHimsView objects and maps them to AtmShortDetailsDto.
//	 * This method assumes that the HIMS data will directly provide most of the required fields,
//	 * or default values will be used where HIMS data is not available.
//	 *
//	 * @param himsTickets A list of OpenTicketHimsView objects from the HIMS database.
//	 * @return A list of AtmShortDetailsDto for HIMS tickets.
//	 */
//	private List<AtmShortDetailsDto> processHimsTickets(List<OpenTicketHimsView> himsTickets) {
//		log.debug("Processing HIMS tickets.");
//		
//		 AtmShortDetailsDto atmShortDetailsDto = new AtmShortDetailsDto();
//		 
//		if (himsTickets.isEmpty()|| himsTickets==null) {
//			//return collect(Collectors.e  ;
//			log.info("Processing HIMS tickets."+himsTickets);
//		}
//		List<AtmShortDetailsDto> atmShortDetailsDtoList = new ArrayList<>();
//
//        for (OpenTicketHimsView himsTicket : himsTickets) {
//            try {
//                log.debug("Processing HIMS ticket: {}", himsTicket.getSrno());
//
//                // CRITICAL: Create a NEW AtmShortDetailsDto object for EACH ticket
//               // AtmShortDetailsDto atmShortDetailsDto = new AtmShortDetailsDto();
//
//                // Manually set each field
//                atmShortDetailsDto.setRequestid(himsTicket.getSrno());
//                atmShortDetailsDto.setAtmId(himsTicket.getEquipmentid());
//                atmShortDetailsDto.setTicketNumber(himsTicket.getSrno());
//                atmShortDetailsDto.setBank(himsTicket.getCustomer());
//                atmShortDetailsDto.setSiteName(himsTicket.getSite());
//                atmShortDetailsDto.setOwner(himsTicket.getOwner());
//                atmShortDetailsDto.setSubcall(himsTicket.getSubcalltype());
//                atmShortDetailsDto.setVendor(himsTicket.getVendor());
//                atmShortDetailsDto.setError(himsTicket.getEventcode());
//                atmShortDetailsDto.setDownTime(getDownTimeInHrs(himsTicket.getCreateddate()));
//                atmShortDetailsDto.setPriorityScore(0.0);
//                atmShortDetailsDto.setEventGroup(himsTicket.getCalltype());
//                atmShortDetailsDto.setIsBreakdown(0);
//                atmShortDetailsDto.setIsUpdated(himsTicket.getLastactivity() != null ? 1 : 0);
//                atmShortDetailsDto.setIsTimedOut(1);
//                atmShortDetailsDto.setIsTravelling(0);
//                atmShortDetailsDto.setTravelTime(null);
//                atmShortDetailsDto.setTravelEta(0);
//                atmShortDetailsDto.setDownCall(1);
//                atmShortDetailsDto.setEtaDateTime(himsTicket.getEtadatetime() != null ? String.valueOf(himsTicket.getEtadatetime()) : "");
//                atmShortDetailsDto.setEtaTimeout("");
//                atmShortDetailsDto.setCreatedDate(CustomDateFormattor.convert(himsTicket.getCreateddate(), FormatStyle.DATABASE));
//                atmShortDetailsDto.setCloseDate("");
//                atmShortDetailsDto.setSynergyStatus("");
//                atmShortDetailsDto.setRemark(himsTicket.getLastcomment());
//                atmShortDetailsDto.setHimsStatus(himsTicket.getStatus());
//                atmShortDetailsDto.setFlagStatus(0);
//                atmShortDetailsDto.setFlagStatusInsertTime(null);
//                atmShortDetailsDto.setCeName(himsTicket.getOwner());
//                atmShortDetailsDto.setCreatedTime(CustomDateFormattor.convert(himsTicket.getCreateddate(), FormatStyle.DATABASE));
//                atmShortDetailsDto.setClosedTime("");
//                atmShortDetailsDto.setHr(himsTicket.getDowntimeinmins() != null ?
//                                          String.valueOf(himsTicket.getDowntimeinmins() / 60) : "0");
//
//
//                // Apply color logic based on ticket status
//                atmShortDetailsDto.setColor(ticketColorUtils.getColor(
//                    atmShortDetailsDto.getIsUpdated(),
//                    atmShortDetailsDto.getIsTimedOut(),
//                    atmShortDetailsDto.getIsTravelling()));
//
//                log.debug("Successfully processed HIMS ticket: {}", himsTicket.getSrno());
//                atmShortDetailsDtoList.add(atmShortDetailsDto); // Add the processed DTO to the list
//
//            } catch (Exception e) {
//                log.error("Error processing HIMS ticket: {} - Error: {}", himsTicket.getSrno(), e.getMessage(), e);
//                // In a for loop, if an error occurs for one ticket, you can choose to:
//                // 1. Log the error and skip this ticket (current behavior)
//                // 2. Add a partially populated DTO with error flags
//                // 3. Re-throw a custom exception if processing one ticket failure should stop the whole process
//            }
//        }
//        return atmShortDetailsDtoList; // Return the list after the loop completes
//    
//	}
	
	
//--------------------	my logic -------------------------------------------
	
	
	
	
	
	
	
	
	
//	private String getDownTimeInHrs(String createdDate) {
//	    log.info("Calculating downtime for created date: {}", createdDate);
//	    if (createdDate == null || createdDate.isEmpty()) {
//	        return "0 Hrs";
//	    }
//	    try {
//	        DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.US);
//	        LocalDateTime parsedCreatedDate = LocalDateTime.parse(createdDate, formatterInput);
//	        Duration duration = Duration.between(parsedCreatedDate, LocalDateTime.now());
//	        return String.format("%d Hrs", duration.toHours());
//	    } catch (Exception e) {
//	        log.error("Error parsing createdDate: {}", createdDate, e);
//	        return "0 Hrs";
//	    }
//	}
	
	

}
