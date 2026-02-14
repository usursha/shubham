package com.hpy.ops360.ticketing.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
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
public class TaskService3 {
	@Autowired
	private TaskRepository taskRepository;

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
	
	// Updated getTicketList method with improved logic
	public List<AtmShortDetailsDto> getTicketList(List<AtmDetailsWithSourceDto> atms, int maxLimit, String ceId) {
	    log.info("=== Starting getTicketList for {} ATMs with limit {} ===", atms.size(), maxLimit);
	    
	    // Get tickets from both sources
	    OpenTicketsResponse openTicketsResponse = getOpenTicketDetailsInBatchesWithSource(atms, maxLimit, ceId);

	    if (openTicketsResponse.getAtmdetails().isEmpty()) {
	        log.warn("No ATM details found in the response for CE ID: {}", ceId);
	        return Collections.emptyList();
	    }

	    // Debug: Log ATM details count
	    log.info("ATM details received: {}", openTicketsResponse.getAtmdetails().size());
	    openTicketsResponse.getAtmdetails().forEach(atmDetail -> 
	        log.debug("ATM: {} has {} tickets", atmDetail.getAtmid(), atmDetail.getOpenticketdetails().size()));

	    // Create comprehensive tracking
	    Set<String> processedTickets = ConcurrentHashMap.newKeySet();
	    Map<String, TicketDetailsDto> ticketDetailsMap = new ConcurrentHashMap<>();
	    Set<String> atmTicketEventCodes = ConcurrentHashMap.newKeySet();

	    // Process all tickets
	    openTicketsResponse.getAtmdetails().parallelStream().forEach(atmDetail -> {
	        atmDetail.getOpenticketdetails().forEach(ticketDetailsDto -> {
	            String uniqueTicketKey = String.format("%s_%s", 
	                    ticketDetailsDto.getEquipmentid(), 
	                    ticketDetailsDto.getSrno());
	            
	            if (processedTickets.add(uniqueTicketKey)) {
	                String uniqueCode = String.format("%s|%s|%s|%s|%s", 
	                        ticketDetailsDto.getEquipmentid(),
	                        ticketDetailsDto.getSrno(), 
	                        ticketDetailsDto.getEventcode() != null ? ticketDetailsDto.getEventcode() : "",
	                        ticketDetailsDto.getNextfollowup() != null ? ticketDetailsDto.getNextfollowup() : "",
	                        ticketDetailsDto.getCalldate() != null ? ticketDetailsDto.getCalldate() : "");

	                atmTicketEventCodes.add(uniqueCode);
	                ticketDetailsMap.put(uniqueTicketKey, ticketDetailsDto);
	                
	                log.debug("Processed ticket: {} for ATM: {} (CallType: {})", 
	                          ticketDetailsDto.getSrno(), 
	                          ticketDetailsDto.getEquipmentid(),
	                          ticketDetailsDto.getCalltype());
	            }
	        });
	    });

	    log.info("Total unique tickets processed: {}", processedTickets.size());
	    log.info("Unique event codes created: {}", atmTicketEventCodes.size());

	    // Get ATM ticket events
	    String atmTicketEventCodeList = String.join(",", atmTicketEventCodes);
	    log.info("Fetching ATM ticket events for codes: {}", atmTicketEventCodeList);
	    List<AtmTicketEvent> atmTicketEventList = Collections.emptyList();
	    
	    
	        atmTicketEventList = atmTicketEventRepository
	                .getAtmTicketEvent(loginService.getLoggedInUser(), atmTicketEventCodeList);
	      //  log.info("ATM Ticket Events retrieved from DB: {}", atmTicketEventList.size());
	
	    log.info("Total ATM Ticket Events List retrieved: {}", atmTicketEventList);
	    // Create AtmShortDetailsDto objects
	    List<AtmShortDetailsDto> allTickets = atmTicketEventList.parallelStream()
	            .map(atmTicketEvent -> {
	                String ticketKey = String.format("%s_%s", 
	                        atmTicketEvent.getAtmId(), 
	                        atmTicketEvent.getTicketId());
	                
	                TicketDetailsDto ticketDetailsDto = ticketDetailsMap.get(ticketKey);
	                if (ticketDetailsDto == null) {
	                    log.warn("No ticket details found for key: {}", ticketKey);
	                    return null;
	                }

	                // Create the ticket object
	                AtmShortDetailsDto ticket = AtmShortDetailsDto.builder()
	                        .requestid(openTicketsResponse.getRequestid())
	                        .atmId(ticketDetailsDto.getEquipmentid())
	                        .ticketNumber(ticketDetailsDto.getSrno())
	                        .bank(ticketDetailsDto.getCustomer())
	                        .siteName(ticketDetailsDto.getEquipmentid())
	                        .owner(atmTicketEvent.getOwner() != null ? atmTicketEvent.getOwner() : "")
	                        .subcall(ticketDetailsDto.getSubcalltype())
	                        .vendor(ticketDetailsDto.getVendor())
	                        .error(atmTicketEvent.getEventCode() != null ? atmTicketEvent.getEventCode() : "Default")
	                        .downTime(getDownTimeInHrs(ticketDetailsDto.getCreateddate()))
	                        .priorityScore(atmTicketEvent.getPriorityScore())
	                        .eventGroup(atmTicketEvent.getEventGroup())
	                        .isBreakdown(atmTicketEvent.getIsBreakdown() != null ? atmTicketEvent.getIsBreakdown() : 0)
	                        .isUpdated(atmTicketEvent.getIsUpdated() != null ? atmTicketEvent.getIsUpdated() : 0)
	                        .isTimedOut(atmTicketEvent.getIsTimedOut() != null ? atmTicketEvent.getIsTimedOut() : 0)
	                        .isTravelling(atmTicketEvent.getIsTravelling() != null ? atmTicketEvent.getIsTravelling() : 0)
	                        .travelTime(atmTicketEvent.getTravelTime())
	                        .travelEta(atmTicketEvent.getTravelEta() != null ? atmTicketEvent.getTravelEta() : 0)
	                        .downCall(determineDownCallStatus(atmTicketEvent, ticketDetailsDto))
	                        .etaDateTime(atmTicketEvent.getEtaDateTime() != null ? atmTicketEvent.getEtaDateTime() : "")
	                        .etaTimeout(atmTicketEvent.getEtaTimeout() != null ? atmTicketEvent.getEtaTimeout() : "")
	                        .CreatedDate(CustomDateFormattor.convert(ticketDetailsDto.getCreateddate(), FormatStyle.DATABASE))
	                        .flagStatus(atmTicketEvent.getFlagStatus())
	                        .flagStatusInsertTime(atmTicketEvent.getFlagStatusInsertTime())
	                        .ceName(atmTicketEvent.getCeName())
	                        .build();

	                // Apply color logic
	                ticket.setColor(ticketColorUtils.getColor(
	                        ticket.getIsUpdated(),
	                        ticket.getIsTimedOut(), 
	                        ticket.getIsTravelling()));

	                // Debug log
	                log.debug("Created ticket: {} | DownCall: {} | EventGroup: {} | CallType: {} | IsBreakdown: {}", 
	                          ticket.getTicketNumber(),
	                          ticket.getDownCall(),
	                          ticket.getEventGroup(),
	                          ticketDetailsDto.getCalltype(),
	                          ticket.getIsBreakdown());

	                return ticket;
	            })
	            .filter(Objects::nonNull)
	            .collect(Collectors.toList());

	    log.info("Created {} AtmShortDetailsDto objects", allTickets.size());

	    // Apply intelligent filtering - Include tickets based on multiple criteria
	    List<AtmShortDetailsDto> filteredTickets = allTickets.stream()
	            .filter(ticket -> {
	                boolean include = shouldIncludeTicket(ticket);
	                if (!include) {
	                    log.debug("Excluding ticket {}: DownCall={}, EventGroup={}, IsBreakdown={}", 
	                              ticket.getTicketNumber(), 
	                              ticket.getDownCall(), 
	                              ticket.getEventGroup(), 
	                              ticket.getIsBreakdown());
	                }
	                return include;
	            })
	            .sorted(Comparator.comparing(AtmShortDetailsDto::getPriorityScore).reversed()
	                    .thenComparing(AtmShortDetailsDto::getDownTime).reversed())
	            .limit(maxLimit)
	            .collect(Collectors.toList());

	    log.info("Final result: {} tickets (after filtering from {} total)", 
	             filteredTickets.size(), allTickets.size());
	    
	    // Log final ticket numbers
	    log.info("Final tickets: {}", 
	             filteredTickets.stream()
	                           .map(AtmShortDetailsDto::getTicketNumber)
	                           .collect(Collectors.joining(", ")));

	    return filteredTickets.stream().filter(tickets -> tickets.getDownCall() == 1).toList();
	}
	
	private int determineDownCallStatus(AtmTicketEvent atmTicketEvent, TicketDetailsDto ticketDetailsDto) {
	    try {
	        int originalDownCall = atmTicketEvent.getDownCall() != null ? atmTicketEvent.getDownCall() : 0;
	        String callType = ticketDetailsDto.getCalltype();
	        Integer isBreakdown = atmTicketEvent.getIsBreakdown();
	        String eventGroup = atmTicketEvent.getEventGroup();
	        
	        // Logic from stored procedure: down_call = 1 if original down_call = 1 AND calltype = 'Complaint'
	        if (originalDownCall == 1 && "Complaint".equalsIgnoreCase(callType)) {
	            return 1;
	        }
	        
	        // Additional business logic: Include breakdown tickets
	        if (isBreakdown != null && isBreakdown == 1) {
	            return 1;
	        }
	        
	        // Include important event groups regardless of original down_call
	        if (eventGroup != null && 
	            Arrays.asList("Hardware Fault", "Cash", "Communication", "No Transactions", "Supervisory")
	                  .contains(eventGroup)) {
	            return 1;
	        }
	        
	        return originalDownCall; // Return original value for other cases
	        
	    } catch (Exception e) {
	        log.error("Error determining down_call for ticket {}: {}", 
	                  ticketDetailsDto.getSrno(), e.getMessage());
	        return 1; // Default to include on error
	    }
	}

	// Method to determine if a ticket should be included in results
//	private boolean shouldIncludeTicket(AtmShortDetailsDto ticket) {
//	    // Always include if down_call = 1
//	    if (ticket.getDownCall() == 1) {
//	        return true;
//	    }
//	    
//	    // Include breakdown tickets
//	    if (ticket.getIsBreakdown() == 1) {
//	        return true;
//	    }
//	    
//	    // Include tickets from important event groups
//	    String eventGroup = ticket.getEventGroup();
//	    if (eventGroup != null && 
//	        Arrays.asList("Hardware Fault", "Cash", "Communication", "No Transactions", "Supervisory")
//	              .contains(eventGroup)) {
//	        return true;
//	    }
//	    
//	    // Include flagged tickets
//	    if (ticket.getFlagStatus() == 1) {
//	        return true;
//	    }
//	    
//	    // Include tickets that are updated, travelling, or timed out (active tickets)
//	    if (ticket.getIsUpdated() == 1 || ticket.getIsTravelling() == 1 || ticket.getIsTimedOut() == 1) {
//	        return true;
//	    }
//	    
//	    // For debugging - temporarily include all tickets to see what's being filtered
//	    // Remove this in production
//	    log.debug("Ticket {} would be excluded - DownCall:{}, EventGroup:{}, IsBreakdown:{}, FlagStatus:{}", 
//	              ticket.getTicketNumber(), 
//	              ticket.getDownCall(), 
//	              ticket.getEventGroup(), 
//	              ticket.getIsBreakdown(),
//	              ticket.getFlagStatus());
//	    
//	    return false; // Exclude ticket if none of the above conditions are met
//	}
	
	// Simplified and more inclusive filtering method
	private boolean shouldIncludeTicket(AtmShortDetailsDto ticket) {
	    // Log current ticket evaluation
	    log.debug("Evaluating ticket {} - DownCall:{}, EventGroup:{}, IsBreakdown:{}, FlagStatus:{}", 
	              ticket.getTicketNumber(), 
	              ticket.getDownCall(), 
	              ticket.getEventGroup(), 
	              ticket.getIsBreakdown(),
	              ticket.getFlagStatus());
	    
	    // Rule 1: Always include if down_call = 1
	    if (ticket.getDownCall() == 1) {
	        log.debug("Including ticket {} - DownCall = 1", ticket.getTicketNumber());
	        return true;
	    }
	    
	    // Rule 2: Include breakdown tickets
	    if (ticket.getIsBreakdown() == 1) {
	        log.debug("Including ticket {} - IsBreakdown = 1", ticket.getTicketNumber());
	        return true;
	    }
	    
	    // Rule 3: Include flagged tickets
	    if (ticket.getFlagStatus() == 1) {
	        log.debug("Including ticket {} - FlagStatus = 1", ticket.getTicketNumber());
	        return true;
	    }
	    
	    // Rule 4: Include active tickets (updated, travelling, or timed out)
	    if (ticket.getIsUpdated() == 1 || ticket.getIsTravelling() == 1 || ticket.getIsTimedOut() == 1) {
	        log.debug("Including ticket {} - Active ticket", ticket.getTicketNumber());
	        return true;
	    }
	    
	    // Rule 5: Include tickets from critical event groups
	    String eventGroup = ticket.getEventGroup();
	    if (eventGroup != null && 
	        Arrays.asList("Hardware Fault", "Cash", "Communication", "No Transactions", "Supervisory")
	              .contains(eventGroup)) {
	        log.debug("Including ticket {} - Critical EventGroup: {}", ticket.getTicketNumber(), eventGroup);
	        return true;
	    }
	    
	    // Rule 6: *** MORE INCLUSIVE *** - Include tickets with recent activity
	    // Check if the ticket has recent downtime (less than 72 hours old)
	    try {
	        String downTimeStr = ticket.getDownTime();
	        if (downTimeStr != null && downTimeStr.contains("Hrs")) {
	            int hours = Integer.parseInt(downTimeStr.replace(" Hrs", "").trim());
	            if (hours <= 72) { // Include tickets less than 3 days old
	                log.debug("Including ticket {} - Recent activity ({})", ticket.getTicketNumber(), downTimeStr);
	                return true;
	            }
	        }
	    } catch (Exception e) {
	        log.debug("Could not parse downtime for ticket {}: {}", ticket.getTicketNumber(), e.getMessage());
	    }
	    
	    // Rule 7: *** BUSINESS RULE *** - Include all tickets that have valid event codes
	    if (ticket.getError() != null && !ticket.getError().isEmpty() && !"Default".equals(ticket.getError())) {
	        log.debug("Including ticket {} - Has valid error code: {}", ticket.getTicketNumber(), ticket.getError());
	        return true;
	    }
	    
	    // If none of the above criteria are met, exclude the ticket
	    log.debug("Excluding ticket {} - Does not meet inclusion criteria", ticket.getTicketNumber());
	    return false;
	}

	// Alternative: If you want to include ALL tickets for now (for debugging)
	private boolean shouldIncludeTicket_IncludeAll(AtmShortDetailsDto ticket) {
	    log.debug("Including ALL tickets for debugging - Ticket: {}", ticket.getTicketNumber());
	    return true;
	}

	// Alternative method - include ALL tickets (for debugging)
	/*
	 * private boolean shouldIncludeTicket_IncludeAll(AtmShortDetailsDto ticket) {
	 * // Temporarily include all tickets to debug the filtering issue
	 * log.debug("Including all tickets - Ticket: {}, DownCall: {}, EventGroup: {}",
	 * ticket.getTicketNumber(), ticket.getDownCall(), ticket.getEventGroup());
	 * return true; }
	 */
	

	
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

	
	// Improved HIMS ticket processing method
//	private List<AtmDetailsWithTickets> processHimsTicketsInBatches(List<AtmDetailsWithSourceDto> himsAtmList, String ceid) {
//	    log.info("=== Processing HIMS tickets for {} ATMs ===", himsAtmList.size());
//
//	    if (himsAtmList.isEmpty()) {
//	        return Collections.emptyList();
//	    }
//
//	    // Create ATM lookup map (case-insensitive)
//	    Map<String, String> atmIdMap = himsAtmList.stream()
//	            .collect(Collectors.toMap(
//	                    atm -> atm.getAtmid().trim().toUpperCase(), 
//	                    AtmDetailsWithSourceDto::getAtmid,
//	                    (existing, replacement) -> existing
//	            ));
//
//	    log.info("Created ATM lookup map with {} entries", atmIdMap.size());
//
//	    // Fetch ALL HIMS tickets
//	    List<OpenTicketHimsView> allHimsTickets = Collections.emptyList();
//	    try {
//	        allHimsTickets = openTicketHimsViewRepository.getOpenTicketListHims(ceid);
//	        log.info("Total HIMS tickets fetched: {}", allHimsTickets.size());
//	    } catch (Exception e) {
//	        log.error("Error fetching HIMS tickets: {}", e.getMessage(), e);
//	        return Collections.emptyList();
//	    }
//
//	    // Filter and convert tickets
//	    Map<String, List<TicketDetailsDto>> ticketsByAtm = allHimsTickets.stream()
//	            .filter(ticket -> {
//	                if (ticket == null || ticket.getEquipmentid() == null) {
//	                    return false;
//	                }
//	                String normalizedId = ticket.getEquipmentid().trim().toUpperCase();
//	                boolean matches = atmIdMap.containsKey(normalizedId);
//	                if (matches) {
//	                    log.debug("HIMS ticket {} matches ATM {}", ticket.getSrno(), ticket.getEquipmentid());
//	                }
//	                return matches;
//	            })
//	            .map(this::convertHimsToTicketDetails)
//	            .filter(Objects::nonNull)
//	            
//	            .collect(Collectors.groupingBy(
//	                    ticket -> ticket.getEquipmentid().trim().toUpperCase(),
//	                    LinkedHashMap::new,
//	                    Collectors.toList()
//	            ));
//
//	    log.info("HIMS tickets grouped for {} ATMs", ticketsByAtm.size());
//	    
//	    // Log ticket counts per ATM
//	    ticketsByAtm.forEach((atmId, tickets) -> 
//	            log.debug("ATM {} has {} HIMS tickets", atmId, tickets.size(),tickets));
//
//	    // Create result list
//	    List<AtmDetailsWithTickets> result = himsAtmList.stream()
//	            .map(atm -> {
//	                String normalizedId = atm.getAtmid().trim().toUpperCase();
//	                AtmDetailsWithTickets atmWithTickets = new AtmDetailsWithTickets();
//	                atmWithTickets.setAtmid(atm.getAtmid());
//
//	                List<TicketDetailsDto> tickets = ticketsByAtm.getOrDefault(normalizedId, new ArrayList<>());
//	                atmWithTickets.setOpenticketdetails(tickets);
//	                
//	                if (!tickets.isEmpty()) {
//	                    log.debug("ATM {} ------------------------assigned {} --------------------HIMS tickets by shubham------------------------: {}", 
//	                              atm.getAtmid(), 
//	                              tickets.size(),
//	                              tickets.stream().map(TicketDetailsDto::getSrno).collect(Collectors.joining(", ")));
//	                }
//	                
//	                return atmWithTickets;
//	            })
//	            .collect(Collectors.toList());
//
//	    int totalTickets = result.stream().mapToInt(atm -> atm.getOpenticketdetails().size()).sum();
//	    log.info("HIMS processing complete: {} ATMs with {} total tickets", result.size(), totalTickets);
//	    
//	    return result;
//	}
	
	// Improved HIMS ticket processing method with detailed debugging
	private List<AtmDetailsWithTickets> processHimsTicketsInBatches(List<AtmDetailsWithSourceDto> himsAtmList, String ceid) {
	    log.info("=== Processing HIMS tickets for {} ATMs ===", himsAtmList.size());

	    if (himsAtmList.isEmpty()) {
	        return Collections.emptyList();
	    }

	    // Create ATM lookup map (case-insensitive)
	    Map<String, String> atmIdMap = himsAtmList.stream()
	            .collect(Collectors.toMap(
	                    atm -> atm.getAtmid().trim().toUpperCase(), 
	                    AtmDetailsWithSourceDto::getAtmid,
	                    (existing, replacement) -> existing
	            ));

	    log.info("Created ATM lookup map with {} entries", atmIdMap.size());
	    // Debug: Print all ATM IDs in the map
	    atmIdMap.forEach((key, value) -> log.debug("ATM Map - Key: {}, Value: {}", key, value));

	    // Fetch ALL HIMS tickets
	    List<OpenTicketHimsView> allHimsTickets = Collections.emptyList();
	    try {
	        allHimsTickets = openTicketHimsViewRepository.getOpenTicketListHims(ceid);
	        log.info("Total HIMS tickets fetched: {}", allHimsTickets.size());
	        log.info("All HIMS open tickets fetched:{}",allHimsTickets);
	    } catch (Exception e) {
	        log.error("Error fetching HIMS tickets: {}", e.getMessage(), e);
	        return Collections.emptyList();
	    }

	    if (allHimsTickets.isEmpty()) {
	        log.warn("No HIMS tickets found for CEID: {}", ceid);
	        return himsAtmList.stream()
	                .map(atm -> {
	                    AtmDetailsWithTickets atmWithTickets = new AtmDetailsWithTickets();
	                    atmWithTickets.setAtmid(atm.getAtmid());
	                    atmWithTickets.setOpenticketdetails(new ArrayList<>());
	                    return atmWithTickets;
	                })
	                .collect(Collectors.toList());
	    }

	    // Debug: Log first few tickets
	    allHimsTickets.stream().limit(5).forEach(ticket -> 
	        log.debug("Sample HIMS ticket - SRNO: {}, EquipmentID: {}, CallType: {}", 
	                  ticket.getSrno(), ticket.getEquipmentid(), ticket.getCalltype()));

	    // Filter tickets that match our ATMs
	    List<OpenTicketHimsView> matchingTickets = allHimsTickets.stream()
	            .filter(ticket -> {
	                if (ticket == null || ticket.getEquipmentid() == null) {
	                    log.debug("Skipping null ticket or null equipment ID");
	                    return false;
	                }
	                String normalizedId = ticket.getEquipmentid().trim().toUpperCase();
	                boolean matches = atmIdMap.containsKey(normalizedId);
	                if (matches) {
	                    log.debug("HIMS ticket {} matches ATM {} (normalized: {})", 
	                              ticket.getSrno(), ticket.getEquipmentid(), normalizedId);
	                } else {
	                    log.debug("HIMS ticket {} does NOT match any ATM - EquipmentID: {} (normalized: {})", 
	                              ticket.getSrno(), ticket.getEquipmentid(), normalizedId);
	                }
	                return matches;
	            })
	            .collect(Collectors.toList());

	    log.info("Filtered {} matching HIMS tickets from {} total", matchingTickets.size(), allHimsTickets.size());

	    // Convert tickets with detailed logging
	    List<TicketDetailsDto> convertedTickets = new ArrayList<>();
	    int conversionErrors = 0;
	    
	    for (OpenTicketHimsView himsTicket : matchingTickets) {
	        try {
	            TicketDetailsDto converted = convertHimsToTicketDetails(himsTicket);
	            if (converted != null) {
	                convertedTickets.add(converted);
	                log.debug("Successfully converted ticket: {} for ATM: {}", 
	                          converted.getSrno(), converted.getEquipmentid());
	            } else {
	                conversionErrors++;
	                log.warn("Conversion returned null for ticket: {} ATM: {}", 
	                         himsTicket.getSrno(), himsTicket.getEquipmentid());
	            }
	        } catch (Exception e) {
	            conversionErrors++;
	            log.error("Error converting ticket {} for ATM {}: {}", 
	                      himsTicket.getSrno(), himsTicket.getEquipmentid(), e.getMessage());
	        }
	    }

	    log.info("Converted {} tickets successfully, {} conversion errors", 
	             convertedTickets.size(), conversionErrors);

	    // Group tickets by ATM ID
	    Map<String, List<TicketDetailsDto>> ticketsByAtm = convertedTickets.stream()
	            .collect(Collectors.groupingBy(
	                    ticket -> ticket.getEquipmentid().trim().toUpperCase(),
	                    LinkedHashMap::new,
	                    Collectors.toList()
	            ));

	    log.info("HIMS tickets grouped for {} ATMs", ticketsByAtm.size());
	    
	    // Log ticket counts per ATM with detailed info
	    ticketsByAtm.forEach((atmId, tickets) -> {
	        log.info("ATM {} has {} HIMS tickets: {}", 
	                 atmId, 
	                 tickets.size(),
	                 tickets.stream().map(TicketDetailsDto::getSrno).collect(Collectors.joining(", ")));
	    });

	    // Create result list
	    List<AtmDetailsWithTickets> result = himsAtmList.stream()
	            .map(atm -> {
	                String normalizedId = atm.getAtmid().trim().toUpperCase();
	                AtmDetailsWithTickets atmWithTickets = new AtmDetailsWithTickets();
	                atmWithTickets.setAtmid(atm.getAtmid()); // Keep original case

	                List<TicketDetailsDto> tickets = ticketsByAtm.getOrDefault(normalizedId, new ArrayList<>());
	                atmWithTickets.setOpenticketdetails(tickets);
	                
	                log.info("Final - ATM {} assigned {} HIMS tickets", 
	                         atm.getAtmid(), tickets.size());
	                
	                if (!tickets.isEmpty()) {
	                    log.info("ATM {} ticket details: {}", 
	                             atm.getAtmid(),
	                             tickets.stream()
	                                   .map(t -> String.format("SRNO:%s,CallType:%s", t.getSrno(), t.getCalltype()))
	                                   .collect(Collectors.joining("; ")));
	                }
	                
	                return atmWithTickets;
	            })
	            .collect(Collectors.toList());

	    int totalTickets = result.stream().mapToInt(atm -> atm.getOpenticketdetails().size()).sum();
	    log.info("HIMS processing complete: {} ATMs with {} total tickets", result.size(), totalTickets);
	    
	    // Final verification
	    if (totalTickets == 0 && !matchingTickets.isEmpty()) {
	        log.error("ERROR: Found {} matching tickets but final result has 0 tickets!", matchingTickets.size());
	        log.error("This indicates a problem in the conversion or grouping process");
	        
	        // Additional debugging
	        log.error("ATM ID Map keys: {}", atmIdMap.keySet());
	        log.error("Ticket equipment IDs: {}", 
	                  matchingTickets.stream()
	                                .map(OpenTicketHimsView::getEquipmentid)
	                                .collect(Collectors.toSet()));
	    }
	    
	    return result;
	}
	

	// Enhanced HIMS to TicketDetailsDto conversion with better error handling
//	private TicketDetailsDto convertHimsToTicketDetails(OpenTicketHimsView himsView) {
//	    if (himsView == null) {
//	        log.warn("Received null OpenTicketHimsView");
//	        return null;
//	    }
//
//	    try {
//	        TicketDetailsDto ticketDetails = new TicketDetailsDto();
//	        
//	        // Set required fields with null safety
//	        ticketDetails.setSrno(himsView.getSrno() != null ? himsView.getSrno() : "");
//	        ticketDetails.setCustomer(himsView.getCustomer() != null ? himsView.getCustomer() : "");
//	        ticketDetails.setEquipmentid(himsView.getEquipmentid() != null ? himsView.getEquipmentid().trim() : "");
//	        ticketDetails.setModel(himsView.getModel());
//	        ticketDetails.setAtmcategory(himsView.getAtmcategory());
//	        ticketDetails.setAtmclassification(himsView.getAtmclassification());
//	        ticketDetails.setCalldate(himsView.getCalldate());
//	        
//	        // Safe date conversion
//	        try {
//	            if (himsView.getCreateddate() != null) {
//	                ticketDetails.setCreateddate(CustomDateFormattor.convert(himsView.getCreateddate(), FormatStyle.DATABASE));
//	            } else {
//	                ticketDetails.setCreateddate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//	            }
//	        } catch (Exception e) {
//	            log.warn("Date conversion error for ticket {}: {}", himsView.getSrno(), e.getMessage());
//	            ticketDetails.setCreateddate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//	        }
//	        
//	        ticketDetails.setCalltype(himsView.getCalltype() != null ? himsView.getCalltype() : "");
//	        ticketDetails.setSubcalltype(himsView.getSubcalltype() != null ? himsView.getSubcalltype() : "");
//	        ticketDetails.setCompletiondatewithtime(himsView.getCompletiondatewithtime());
//	        ticketDetails.setDowntimeinmins(himsView.getDowntimeinmins() != null ? himsView.getDowntimeinmins() : 0);
//	        ticketDetails.setVendor(himsView.getVendor());
//	        ticketDetails.setServicecode(himsView.getServicecode());
//	        ticketDetails.setDiagnosis(himsView.getDiagnosis());
//	        ticketDetails.setEventcode(himsView.getEventcode() != null ? himsView.getEventcode() : "");
//	        ticketDetails.setHelpdeskname(himsView.getHelpdeskname());
//	        ticketDetails.setLastallocatedtime(himsView.getLastallocatedtime());
//	        ticketDetails.setLastcomment(himsView.getLastcomment());
//	        ticketDetails.setLastactivity(himsView.getLastactivity());
//	        ticketDetails.setStatus(himsView.getStatus());
//	        ticketDetails.setSubstatus(himsView.getSubstatus());
//	        ticketDetails.setRo(himsView.getRo());
//	        ticketDetails.setSite(himsView.getSite());
//	        ticketDetails.setAddress(himsView.getAddress());
//	        ticketDetails.setCity(himsView.getCity());
//	        ticketDetails.setLocationname(himsView.getLocationname());
//	        ticketDetails.setState(himsView.getState());
//	        ticketDetails.setNextfollowup(himsView.getNextfollowup() != null ? himsView.getNextfollowup() : "");
//	        
//	        // HIMS-specific fields
//	        ticketDetails.setEtadatetime(himsView.getEtadatetime());
//	        ticketDetails.setOwner(himsView.getOwner() != null ? himsView.getOwner() : "");
//	        ticketDetails.setCustomerRemark(himsView.getCustomerRemark());
//	        
//	        log.debug("Successfully converted HIMS ticket: {} for ATM: {} (CallType: {})", 
//	                  ticketDetails.getSrno(), 
//	                  ticketDetails.getEquipmentid(),
//	                  ticketDetails.getCalltype());
//	        
//	        return ticketDetails;
//	        
//	    } catch (Exception e) {
//	        log.error("Error converting HIMS ticket {} for ATM {}: {}", 
//	                  himsView.getSrno(), 
//	                  himsView.getEquipmentid(), 
//	                  e.getMessage(), e);
//	        
//	        // Return minimal ticket to avoid complete failure
//	        TicketDetailsDto errorTicket = new TicketDetailsDto();
//	        errorTicket.setEquipmentid(himsView.getEquipmentid() != null ? himsView.getEquipmentid() : "UNKNOWN");
//	        errorTicket.setSrno(himsView.getSrno() != null ? himsView.getSrno() : "ERROR");
//	        errorTicket.setStatus("ERROR_PROCESSING");
//	        errorTicket.setCalltype("Complaint"); // Default to complaint to ensure inclusion
//	        return errorTicket;
//	    }
//	}
	
	// Enhanced HIMS to TicketDetailsDto conversion with comprehensive error handling
	private TicketDetailsDto convertHimsToTicketDetails(OpenTicketHimsView himsView) {
	    if (himsView == null) {
	        log.warn("Received null OpenTicketHimsView");
	        return null;
	    }

	    log.debug("Converting HIMS ticket - SRNO: {}, EquipmentID: {}, CallType: {}", 
	              himsView.getSrno(), himsView.getEquipmentid(), himsView.getCalltype());

	    try {
	        TicketDetailsDto ticketDetails = new TicketDetailsDto();
	        
	        // Critical fields - ensure they're never null
	        String srno = himsView.getSrno();
	        String equipmentId = himsView.getEquipmentid();
	        
	        if (srno == null || srno.trim().isEmpty()) {
	            log.warn("HIMS ticket has null/empty SRNO, using fallback");
	            srno = "UNKNOWN_" + System.currentTimeMillis();
	        }
	        if (equipmentId == null || equipmentId.trim().isEmpty()) {
	            log.warn("HIMS ticket {} has null/empty EquipmentID, using fallback", srno);
	            equipmentId = "UNKNOWN_ATM";
	        }
	        
	        // Set required fields with null safety
	        ticketDetails.setSrno(srno);
	        ticketDetails.setCustomer(himsView.getCustomer() != null ? himsView.getCustomer() : "");
	        ticketDetails.setEquipmentid(equipmentId.trim());
	        ticketDetails.setModel(himsView.getModel());
	        ticketDetails.setAtmcategory(himsView.getAtmcategory());
	        ticketDetails.setAtmclassification(himsView.getAtmclassification());
	        ticketDetails.setCalldate(himsView.getCalldate());
	        
	        // Safe date conversion with more robust handling
	        String createdDate = null;
	        try {
	            if (himsView.getCreateddate() != null) {
	                createdDate = CustomDateFormattor.convert(himsView.getCreateddate(), FormatStyle.DATABASE);
	                ticketDetails.setCreateddate(createdDate);
	            } else {
	                createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	                ticketDetails.setCreateddate(createdDate);
	                log.debug("Using current time for created date for ticket {}", srno);
	            }
	        } catch (Exception e) {
	            log.warn("Date conversion error for ticket {}: {}, using current time", srno, e.getMessage());
	            createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	            ticketDetails.setCreateddate(createdDate);
	        }
	        
	        // Set call type - this is crucial for filtering
	        String callType = himsView.getCalltype();
	        if (callType == null || callType.trim().isEmpty()) {
	            log.warn("HIMS ticket {} has null/empty CallType, defaulting to 'Complaint'", srno);
	            callType = "Complaint"; // Default to ensure inclusion in filtering
	        }
	        ticketDetails.setCalltype(callType);
	        
	        // Set other fields
	        ticketDetails.setSubcalltype(himsView.getSubcalltype() != null ? himsView.getSubcalltype() : "");
	        ticketDetails.setCompletiondatewithtime(himsView.getCompletiondatewithtime());
	        ticketDetails.setDowntimeinmins(himsView.getDowntimeinmins() != null ? himsView.getDowntimeinmins() : 0);
	        ticketDetails.setVendor(himsView.getVendor());
	        ticketDetails.setServicecode(himsView.getServicecode());
	        ticketDetails.setDiagnosis(himsView.getDiagnosis());
	        ticketDetails.setEventcode(himsView.getEventcode() != null ? himsView.getEventcode() : "");
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
	        ticketDetails.setNextfollowup(himsView.getNextfollowup() != null ? himsView.getNextfollowup() : "");
	        
	        // HIMS-specific fields
	        ticketDetails.setEtadatetime(himsView.getEtadatetime());
	        ticketDetails.setOwner(himsView.getOwner() != null ? himsView.getOwner() : "");
	        ticketDetails.setCustomerRemark(himsView.getCustomerRemark());
	        
	        log.debug("Successfully converted HIMS ticket: {} for ATM: {} (CallType: {}, CreatedDate: {})", 
	                  ticketDetails.getSrno(), 
	                  ticketDetails.getEquipmentid(),
	                  ticketDetails.getCalltype(),
	                  createdDate);
	        
	        return ticketDetails;
	        
	    } catch (Exception e) {
	        log.error("Critical error converting HIMS ticket {} for ATM {}: {}", 
	                  himsView.getSrno(), 
	                  himsView.getEquipmentid(), 
	                  e.getMessage(), e);
	        
	        // Return minimal but valid ticket to avoid complete failure
	        try {
	            TicketDetailsDto errorTicket = new TicketDetailsDto();
	            errorTicket.setEquipmentid(himsView.getEquipmentid() != null ? himsView.getEquipmentid().trim() : "ERROR_ATM");
	            errorTicket.setSrno(himsView.getSrno() != null ? himsView.getSrno() : "ERROR_" + System.currentTimeMillis());
	            errorTicket.setStatus("ERROR_PROCESSING");
	            errorTicket.setCalltype("Complaint"); // Default to complaint to ensure inclusion
	            errorTicket.setCustomer(himsView.getCustomer() != null ? himsView.getCustomer() : "");
	            errorTicket.setCreateddate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	            errorTicket.setEventcode("");
	            errorTicket.setNextfollowup("");
	            errorTicket.setSubcalltype("");
	            errorTicket.setOwner("");
	            
	            log.warn("Returning error ticket for failed conversion: {}", errorTicket.getSrno());
	            return errorTicket;
	        } catch (Exception fallbackError) {
	            log.error("Failed to create even error ticket: {}", fallbackError.getMessage());
	            return null;
	        }
	    }
	}
}
