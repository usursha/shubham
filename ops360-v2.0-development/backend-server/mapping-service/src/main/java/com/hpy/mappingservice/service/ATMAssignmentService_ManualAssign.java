package com.hpy.mappingservice.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.entity.ATMReassignmentHistory_AtmWise;
import com.hpy.mappingservice.repository.ATMAssignmentRepository;
import com.hpy.mappingservice.repository.ATMReassignmentHistory_AtmWiseRepository;
import com.hpy.mappingservice.response.dto.ATMReassignmentResponse;
import com.hpy.mappingservice.response.dto.CEUserLocation;
import com.hpy.mappingservice.service.GeoapifyService.DistanceResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ATMAssignmentService_ManualAssign {



	    private final ATMAssignmentRepository repository;
	    private final GeoapifyService geoapifyService;
	    
	    private final ATMReassignmentHistory_AtmWiseRepository historyRepository;

//	    /**
//	     * NEW METHOD: Manual reassignment for single ATM
//	     * Get ATM coordinates by ATM ID, get all available CEs, 
//	     * calculate distances and return sorted by shortest distance
//	     */
//	    public List<ATMReassignmentResponse> manualReassignSingleATM(String atmId) {
//	        log.info("Starting manual reassignment for single ATM: {}", atmId);
//	        
//	        // Step 1: Get ATM coordinates using inline query
//	        List<Object[]> atmCoordinates = repository.getATMCoordinates(atmId);
//	        
//	        if (atmCoordinates.isEmpty()) {
//	            log.error("ATM not found: {}", atmId);
//	            throw new RuntimeException("ATM not found: " + atmId);
//	        }
//	        
//	        Object[] atmData = atmCoordinates.get(0);
//	        if (atmData[0] == null || atmData[1] == null) {
//	            log.error("ATM {} has missing coordinates", atmId);
//	            throw new RuntimeException("ATM " + atmId + " has missing coordinates");
//	        }
//	        
//	        Double atmLat = ((Number) atmData[0]).doubleValue();
//	        Double atmLon = ((Number) atmData[1]).doubleValue();
//	        
//	        log.info("ATM {} coordinates: ({}, {})", atmId, atmLat, atmLon);
//	        
//	        // Step 2: Get all available CEs using inline query
//	        List<CEUserLocation> availableCEs = getAvailableCEs(atmId, atmId);
//	        
//	        
//	        if (availableCEs.isEmpty()) {
//	            log.error("No available CEs found");
//	            throw new RuntimeException("No available CEs found");
//	        }
//	        
//	        log.info("Found {} available CEs", availableCEs.size());
//	        
//	        // Step 3: Calculate distances for this ATM to all CEs
//	        List<ATMReassignmentResponse> ceDistances = new ArrayList<>();
//	        
//	        for (CEUserLocation ce : availableCEs) {
//	            log.info("Calculating distance from ATM {} to CE {}", atmId, ce.getCeUserId());
//	            
//	            try {
//	                DistanceResult result = geoapifyService.calculateDistance(
//	                    ce.getUserLat(), ce.getUserLong(),
//	                    atmLat, atmLon
//	                );
//	                
//	                ATMReassignmentResponse response = new ATMReassignmentResponse(
//	                    atmId,
//	                    "MANUAL", // Original CE (manual process)
//	                    ce.getCeUserId(),
//	                    result.getDistance(),
//	                    result.getTravelTime()
//	                );
//	                
//	                ceDistances.add(response);
//	                
//	                log.info("Distance from ATM {} to CE {}: {} km, {} min", 
//	                        atmId, ce.getCeUserId(), 
//	                        String.format("%.2f", result.getDistance()),
//	                        String.format("%.2f", result.getTravelTime()));
//	                
//	            } catch (Exception e) {
//	                log.error("Error calculating distance from ATM {} to CE {}: {}", 
//	                         atmId, ce.getCeUserId(), e.getMessage());
//	            }
//	        }
//	        
//	        // Step 4: Sort by shortest distance (ascending order)
//	        ceDistances.sort(Comparator.comparing(ATMReassignmentResponse::getDistance));
//	        
//	        log.info("Completed manual reassignment for ATM {}. Found {} CE options sorted by distance", 
//	                atmId, ceDistances.size());
//	        
//	        // Log the sorted results
//	        for (int i = 0; i < ceDistances.size(); i++) {
//	            ATMReassignmentResponse response = ceDistances.get(i);
//	            log.info("Rank {}: CE {} - {} km, {} min", 
//	                    i + 1, response.getNewCeUserId(),
//	                    String.format("%.2f", response.getDistance()),
//	                    String.format("%.2f", response.getTravelTime()));
//	        }
//	        
//	        return ceDistances;
//	    }
//
//	    /**
//	     * EXISTING METHOD: Bulk reassignment (kept for backward compatibility)
//	     */
//	    public List<ATMReassignmentResponse> reassignATMs(ATMReassignmentRequest request) {
//	        log.info("Starting bulk ATM reassignment for CE: {} under CM: {}", 
//	                request.getLeavingCeUserId(), request.getCmUserId());
//
//	        // Step 1: Get all ATMs assigned to the leaving CE
//	        List<ATMCEMapping> leavingCeATMs = getATMsForCE(request.getCmUserId(), request.getLeavingCeUserId());
//	        log.info("Found {} ATMs to reassign", leavingCeATMs.size());
//
//	        if (leavingCeATMs.isEmpty()) {
//	            log.warn("No ATMs found for CE: {}", request.getLeavingCeUserId());
//	            return new ArrayList<>();
//	        }
//
//	        // Step 2: Get all other CE locations (excluding the leaving CE)
//	        List<CEUserLocation> availableCEs = getAvailableCEs(request.getCmUserId(), request.getLeavingCeUserId());
//	        log.info("Found {} available CEs", availableCEs.size());
//
//	        if (availableCEs.isEmpty()) {
//	            throw new RuntimeException("No available CEs to reassign ATMs");
//	        }
//
//	        // Step 3: Process each ATM sequentially
//	        List<ATMReassignmentResponse> reassignments = new ArrayList<>();
//
//	        for (ATMCEMapping atm : leavingCeATMs) {
//	            log.info("Processing ATM: {}", atm.getAtmCode());
//
//	            ATMReassignmentResponse assignment = findNearestCEForATM(atm, availableCEs, request.getLeavingCeUserId());
//
//	            if (assignment != null) {
//	                reassignments.add(assignment);
//
//	                log.info("ATM {} reassigned from {} to {} (distance: {} km, time: {} min)", 
//	                        assignment.getAtmCode(), assignment.getOriginalCeUserId(), 
//	                        assignment.getNewCeUserId(), 
//	                        String.format("%.2f", assignment.getDistance()),
//	                        String.format("%.2f", assignment.getTravelTime()));
//	            }
//	        }
//
//	        log.info("Successfully reassigned {} ATMs", reassignments.size());
//	        return reassignments;
//	    }
//
//	    /**
//	     * Find nearest CE for one specific ATM (used in bulk processing)
//	     */
//	    private ATMReassignmentResponse findNearestCEForATM(ATMCEMapping atm, 
//	                                                       List<CEUserLocation> availableCEs,
//	                                                       String originalCeUserId) {
//	        
//	        CEUserLocation nearestCE = null;
//	        Double minDistance = Double.MAX_VALUE;
//	        Double minTravelTime = 0.0;
//
//	        for (CEUserLocation ce : availableCEs) {
//	            try {
//	                DistanceResult result = geoapifyService.calculateDistance(
//	                    ce.getUserLat(), ce.getUserLong(),
//	                    atm.getAtmLat(), atm.getAtmLong()
//	                );
//
//	                if (result.getDistance() < minDistance) {
//	                    minDistance = result.getDistance();
//	                    minTravelTime = result.getTravelTime();
//	                    nearestCE = ce;
//	                }
//
//	            } catch (Exception e) {
//	                log.warn("Error calculating distance from ATM {} to CE {}: {}", 
//	                        atm.getAtmCode(), ce.getCeUserId(), e.getMessage());
//	            }
//	        }
//
//	        if (nearestCE == null) {
//	            log.error("Could not find any suitable CE for ATM: {}", atm.getAtmCode());
//	            return null;
//	        }
//
//	        return new ATMReassignmentResponse(
//	            atm.getAtmCode(),
//	            originalCeUserId,
//	            nearestCE.getCeUserId(),
//	            minDistance,
//	            minTravelTime
//	        );
//	    }
//
//	    /**
//	     * EXISTING METHODS: For backward compatibility with bulk processing
//	     */
//	    private List<ATMCEMapping> getATMsForCE(String cmUserId, String ceUserId) {
//	        List<Object[]> results = repository.getATMCEMappingDetails(cmUserId, ceUserId);
//
//	        List<ATMCEMapping> validATMs = new ArrayList<>();
//	        int skippedCount = 0;
//
//	        for (Object[] row : results) {
//	            String atmCode = (String) row[1];
//
//	            if (row[2] == null || row[3] == null) {
//	                log.warn("Skipping ATM {} - missing coordinates", atmCode);
//	                skippedCount++;
//	                continue;
//	            }
//
//	            try {
//	                ATMCEMapping mapping = new ATMCEMapping(
//	                    (String) row[0],
//	                    atmCode,
//	                    ((Number) row[2]).doubleValue(),
//	                    ((Number) row[3]).doubleValue()
//	                );
//	                validATMs.add(mapping);
//	            } catch (Exception e) {
//	                log.warn("Skipping ATM {} - invalid data: {}", atmCode, e.getMessage());
//	                skippedCount++;
//	            }
//	        }
//
//	        if (skippedCount > 0) {
//	            log.warn("Skipped {} ATMs due to invalid data", skippedCount);
//	        }
//
//	        return validATMs;
//	    }
//
//	    private List<CEUserLocation> getAvailableCEs(String cmUserId, String excludedCeUserId) {
//	        List<Object[]> results = repository.getCEUserLocations(cmUserId, excludedCeUserId);
//
//	        List<CEUserLocation> validCEs = new ArrayList<>();
//	        int skippedCount = 0;
//
//	        for (Object[] row : results) {
//	            String ceUserId = (String) row[0];
//
//	            if (row[1] == null || row[2] == null) {
//	                log.warn("Skipping CE {} - missing coordinates", ceUserId);
//	                skippedCount++;
//	                continue;
//	            }
//
//	            try {
//	                CEUserLocation location = new CEUserLocation(
//	                    ceUserId,
//	                    ((Number) row[1]).doubleValue(),
//	                    ((Number) row[2]).doubleValue()
//	                );
//	                validCEs.add(location);
//	            } catch (Exception e) {
//	                log.warn("Skipping CE {} - invalid data: {}", ceUserId, e.getMessage());
//	                skippedCount++;
//	            }
//	        }
//
//	        if (skippedCount > 0) {
//	            log.warn("Skipped {} CEs due to invalid data", skippedCount);
//	        }
//
//	        return validCEs;
//	    }
	    
	    
	    
	    
	    
	    //----------------------------------------------
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
//	    public List<ATMReassignmentResponse> manualReassignSingleATM(String atmId) {
//	        log.info("Starting manual reassignment for single ATM: {}", atmId);
//	        
//	        // Validate input
//	        if (atmId == null || atmId.trim().isEmpty()) {
//	            throw new IllegalArgumentException("ATM ID cannot be null or empty");
//	        }
//	        
//	        // Step 1: Get ATM coordinates using inline query
//	        List<Object[]> atmCoordinates = repository.getATMCoordinates(atmId);
//	        
//	        if (atmCoordinates.isEmpty()) {
//	            log.error("ATM not found: {}", atmId);
//	            throw new RuntimeException("ATM not found: " + atmId);
//	        }
//	        
//	        Object[] atmData = atmCoordinates.get(0);
//	        if (atmData[0] == null || atmData[1] == null) {
//	            log.error("ATM {} has missing coordinates", atmId);
//	            throw new RuntimeException("ATM " + atmId + " has missing coordinates");
//	        }
//	        
//	        Double atmLat = ((Number) atmData[0]).doubleValue();
//	        Double atmLon = ((Number) atmData[1]).doubleValue();
//	        
//	        log.info("ATM {} coordinates: ({}, {})", atmId, atmLat, atmLon);
//	        
//	        // Step 2: Get all available CEs (not filtered by CM or excluding any CE)
//	        List<CEUserLocation> availableCEs = getAllAvailableCEsForManualAssignment();
//	        
//	        if (availableCEs.isEmpty()) {
//	            log.error("No available CEs found for manual assignment");
//	            throw new RuntimeException("No available CEs found");
//	        }
//	        
//	        log.info("Found {} available CEs for manual assignment", availableCEs.size());
//	        
//	        // Step 3: Calculate distances for this ATM to all CEs
//	        List<ATMReassignmentResponse> ceDistances = new ArrayList<>();
//	        
//	        for (CEUserLocation ce : availableCEs) {
//	            log.debug("Calculating distance from ATM {} to CE {}", atmId, ce.getCeUserId());
//	            
//	            try {
//	                DistanceResult result = geoapifyService.calculateDistance(
//	                    ce.getUserLat(), ce.getUserLong(),
//	                    atmLat, atmLon
//	                );
//	                
//	                ATMReassignmentResponse response = new ATMReassignmentResponse(
//	                    atmId,
//	                    "MANUAL", // Original CE (manual process)
//	                    ce.getCeUserId(),
//	                    result.getDistance(),
//	                    result.getTravelTime()
//	                );
//	                
//	                ceDistances.add(response);
//	                
//	                log.debug("Distance from ATM {} to CE {}: {} km, {} min", 
//	                        atmId, ce.getCeUserId(), 
//	                        String.format("%.2f", result.getDistance()),
//	                        String.format("%.2f", result.getTravelTime()));
//	                
//	            } catch (Exception e) {
//	                log.error("Error calculating distance from ATM {} to CE {}: {}", 
//	                         atmId, ce.getCeUserId(), e.getMessage());
//	                // Continue with other CEs instead of failing completely
//	            }
//	        }
//	        
//	        if (ceDistances.isEmpty()) {
//	            log.error("Could not calculate distances to any CEs for ATM {}", atmId);
//	            throw new RuntimeException("Could not calculate distances to any CEs");
//	        }
//	        
//	        // Step 4: Sort by shortest distance (ascending order)
//	        ceDistances.sort(Comparator.comparing(ATMReassignmentResponse::getDistance));
//	        
//	        log.info("Completed manual reassignment for ATM {}. Found {} CE options sorted by distance", 
//	                atmId, ceDistances.size());
//	        
//	        // Log the top 5 sorted results for debugging
//	        int logCount = Math.min(5, ceDistances.size());
//	        for (int i = 0; i < logCount; i++) {
//	            ATMReassignmentResponse response = ceDistances.get(i);
//	            log.info("Rank {}: CE {} - {} km, {} min", 
//	                    i + 1, response.getNewCeUserId(),
//	                    String.format("%.2f", response.getDistance()),
//	                    String.format("%.2f", response.getTravelTime()));
//	        }
//	        
//	        return ceDistances;
//	    }
//
//	    /**
//	     * Helper method to get all available CEs for manual assignment
//	     * This gets ALL CEs with valid coordinates, not filtered by CM or excluded CEs
//	     */
//	    private List<CEUserLocation> getAllAvailableCEsForManualAssignment() {
//	        List<Object[]> results = repository.getAllAvailableCEs();
//	        
//	        List<CEUserLocation> validCEs = new ArrayList<>();
//	        int skippedCount = 0;
//	        
//	        for (Object[] row : results) {
//	            String ceUserId = (String) row[0];
//	            
//	            if (row[1] == null || row[2] == null) {
//	                log.warn("Skipping CE {} - missing coordinates", ceUserId);
//	                skippedCount++;
//	                continue;
//	            }
//	            
//	            try {
//	                CEUserLocation location = new CEUserLocation(
//	                    ceUserId,
//	                    ((Number) row[1]).doubleValue(),
//	                    ((Number) row[2]).doubleValue()
//	                );
//	                validCEs.add(location);
//	            } catch (Exception e) {
//	                log.warn("Skipping CE {} - invalid coordinate data: {}", ceUserId, e.getMessage());
//	                skippedCount++;
//	            }
//	        }
//	        
//	        if (skippedCount > 0) {
//	            log.warn("Skipped {} CEs due to invalid data", skippedCount);
//	        }
//	        
//	        log.info("Retrieved {} valid CEs for manual assignment", validCEs.size());
//	        return validCEs;
//	    }
	   
	    /**
	     * NEW METHOD: Manual reassignment for single ATM
	     * Get ATM coordinates by ATM ID, get all available CEs using stored procedure,
	     * calculate distances and return sorted by shortest distance
	     */
	    
	    
	    
	    
	    
	    
	    
//	    public List<ATMReassignmentResponse> manualReassignSingleATM(String atmId) {
//	        log.info("Starting manual reassignment for single ATM: {}", atmId);
//	        
//	        // Validate input
//	        if (atmId == null || atmId.trim().isEmpty()) {
//	            throw new IllegalArgumentException("ATM ID cannot be null or empty");
//	        }
//	        
//	        // Step 1: Get ATM coordinates using existing query
//	        List<Object[]> atmCoordinates = repository.getATMCoordinates(atmId);
//	        
//	        if (atmCoordinates.isEmpty()) {
//	            log.error("ATM not found: {}", atmId);
//	            throw new RuntimeException("ATM not found: " + atmId);
//	        }
//	        
//	        Object[] atmData = atmCoordinates.get(0);
//	        if (atmData[0] == null || atmData[1] == null) {
//	            log.error("ATM {} has missing coordinates", atmId);
//	            throw new RuntimeException("ATM " + atmId + " has missing coordinates");
//	        }
//	        
//	        Double atmLat = ((Number) atmData[0]).doubleValue();
//	        Double atmLon = ((Number) atmData[1]).doubleValue();
//	        
//	        log.info("ATM {} coordinates: ({}, {})", atmId, atmLat, atmLon);
//	        
//	        // Step 2: Get all available CEs using stored procedure
//	        List<CEUserLocation> availableCEs = getAllAvailableCEsForManualAssignment();
//	        
//	        if (availableCEs.isEmpty()) {
//	            log.error("No available CEs found for manual assignment");
//	            throw new RuntimeException("No available CEs found");
//	        }
//	        
//	        log.info("Found {} available CEs for manual assignment", availableCEs.size());
//	        
//	        // Step 3: Calculate distances for this ATM to all CEs
//	        List<ATMReassignmentResponse> ceDistances = new ArrayList<>();
//	        
//	        for (CEUserLocation ce : availableCEs) {
//	            log.debug("Calculating distance from ATM {} to CE {}", atmId, ce.getCeUserId());
//	            
//	            try {
//	                DistanceResult result = geoapifyService.calculateDistance(
//	                    ce.getUserLat(), ce.getUserLong(),
//	                    atmLat, atmLon
//	                );
//	                
//	                ATMReassignmentResponse response = new ATMReassignmentResponse(
//	                    atmId,
//	                    "MANUAL", // Original CE (manual process)
//	                    ce.getCeUserId(),
//	                    result.getDistance(),
//	                    result.getTravelTime()
//	                );
//	                
//	                ceDistances.add(response);
//	                
//	                log.debug("Distance from ATM {} to CE {}: {} km, {} min", 
//	                        atmId, ce.getCeUserId(), 
//	                        String.format("%.2f", result.getDistance()),
//	                        String.format("%.2f", result.getTravelTime()));
//	                
//	            } catch (Exception e) {
//	                log.error("Error calculating distance from ATM {} to CE {}: {}", 
//	                         atmId, ce.getCeUserId(), e.getMessage());
//	                // Continue with other CEs instead of failing completely
//	            }
//	        }
//	        
//	        if (ceDistances.isEmpty()) {
//	            log.error("Could not calculate distances to any CEs for ATM {}", atmId);
//	            throw new RuntimeException("Could not calculate distances to any CEs");
//	        }
//	        
//	        // Step 4: Sort by shortest distance (ascending order)
//	        ceDistances.sort(Comparator.comparing(ATMReassignmentResponse::getDistance));
//	        
//	        log.info("Completed manual reassignment for ATM {}. Found {} CE options sorted by distance", 
//	                atmId, ceDistances.size());
//	        
//	        // Log the top 5 sorted results for debugging
//	        int logCount = Math.min(5, ceDistances.size());
//	        for (int i = 0; i < logCount; i++) {
//	            ATMReassignmentResponse response = ceDistances.get(i);
//	            log.info("Rank {}: CE {} - {} km, {} min", 
//	                    i + 1, response.getNewCeUserId(),
//	                    String.format("%.2f", response.getDistance()),
//	                    String.format("%.2f", response.getTravelTime()));
//	        }
//	        
//	        return ceDistances;
//	    }
//
//	    /**
//	     * Helper method to get all available CEs for manual assignment
//	     * Uses the existing stored procedure to get all CEs with valid coordinates
//	     */
//	    private List<CEUserLocation> getAllAvailableCEsForManualAssignment() {
//	        
//	        // Option 1: Create a new SP that gets all CEs without filtering
//	    	String cm_userid="nitin.waghmare";
//	    	
//	    	String excluded_ce_user_id	="mahesh.patil"	;
//	        List<Object[]> results = repository.getCEUserLocations(cm_userid,excluded_ce_user_id);
//	        
//	        // Option 2: Use existing SP with a dummy/default CM user to get broader results
//	        // You can modify this to use any valid CM user that has access to most CEs
//	        // or create a special case in your SP to return all CEs when a special parameter is passed
//	        
//	        // For now, let's use a approach that gets all unique CEs by calling the SP
//	        // You might need to adjust this based on your business logic
//	        //List<Object[]> results = repository.getCEUserLocationsForManualAssignment("ALL_CES", null);
//	        
//	        // If the above doesn't work, you can try getting CEs from different CM users and combining them
//	        // This is a fallback approach:
//	        if (results.isEmpty()) {
//	            log.warn("No CEs found with 'ALL_CES' parameter, trying alternative approach");
//	            // You could call the SP with different known CM user IDs and combine results
//	            // results = getUniqueAvailableCEs();
//	        }
//	        
//	        List<CEUserLocation> validCEs = new ArrayList<>();
//	        int skippedCount = 0;
//	        
//	        for (Object[] row : results) {
//	            String ceUserId = (String) row[0];
//	            
//	            if (row[1] == null || row[2] == null) {
//	                log.warn("Skipping CE {} - missing coordinates", ceUserId);
//	                skippedCount++;
//	                continue;
//	            }
//	            
//	            try {
//	                CEUserLocation location = new CEUserLocation(
//	                    ceUserId,
//	                    ((Number) row[1]).doubleValue(),
//	                    ((Number) row[2]).doubleValue()
//	                );
//	                validCEs.add(location);
//	            } catch (Exception e) {
//	                log.warn("Skipping CE {} - invalid coordinate data: {}", ceUserId, e.getMessage());
//	                skippedCount++;
//	            }
//	        }
//	        
//	        if (skippedCount > 0) {
//	            log.warn("Skipped {} CEs due to invalid data", skippedCount);
//	        }
//	        
//	        log.info("Retrieved {} valid CEs for manual assignment", validCEs.size());
//	        return validCEs;
//	    }
//	    
	    
	    
	   //          working 2nd ------------------- 
	    
	    
	    /**
	     * IMPROVE METHOD: Manual reassignment for single ATM
	     * 1. Get current CE/CM assignment for the ATM
	     * 2. Get ATM coordinates
	     * 3. Get all available CEs excluding the current CE
	     * 4. Calculate distances and return sorted results
	     */
//	    public List<ATMReassignmentResponse> manualReassignSingleATM(String atmId) {
//	        log.info("Starting manual reassignment for ATM: {}", atmId);
//	        
//	        // Step 1: Validate input
//	        validateATMId(atmId);
//	        
//	        // Step 2: Get current ATM assignment (CE and CM)
//	        ATMCurrentAssignment currentAssignment = getCurrentATMAssignment(atmId);
//	        log.info("ATM {} current assignment - CE: {}, CM: {}", 
//	                atmId, currentAssignment.getCurrentCeUserId(), currentAssignment.getCmUserId());
//	        
//	        // Step 3: Get ATM coordinates
//	        double[] atmCoordinates = getATMCoordinates(atmId);
//	        log.info("ATM {} coordinates: ({}, {})", atmId, atmCoordinates[0], atmCoordinates[1]);
//	        
//	        // Step 4: Get available CEs (excluding current CE)
//	        List<CEUserLocation> availableCEs = getAvailableCEsExcludingCurrent(
//	            currentAssignment.getCmUserId(), 
//	            currentAssignment.getCurrentCeUserId()
//	        );
//	        
//	        if (availableCEs.isEmpty()) {
//	            throw new RuntimeException("No alternative CEs found for manual assignment");
//	        }
//	        
//	        log.info("Found {} alternative CEs for manual assignment", availableCEs.size());
//	        
//	        // Step 5: Calculate distances to all alternative CEs
//	        List<ATMReassignmentResponse> ceDistances = calculateDistancesToCEs(
//	            atmId, 
//	            atmCoordinates[0], 
//	            atmCoordinates[1], 
//	            availableCEs, 
//	            currentAssignment.getCurrentCeUserId()
//	        );
//	        
//	        if (ceDistances.isEmpty()) {
//	            throw new RuntimeException("Could not calculate distances to any alternative CEs");
//	        }
//	        
//	        // Step 6: Sort by shortest distance
//	        ceDistances.sort(Comparator.comparing(ATMReassignmentResponse::getDistance));
//	        
//	        log.info("Completed manual reassignment for ATM {}. Found {} alternative CE options", 
//	                atmId, ceDistances.size());
//	        
//	        logTopResults(ceDistances, 5);
//	        return ceDistances;
//	    }
//
//	    /**
//	     * Validate ATM ID input
//	     */
//	    private void validateATMId(String atmId) {
//	        if (atmId == null || atmId.trim().isEmpty()) {
//	            throw new IllegalArgumentException("ATM ID cannot be null or empty");
//	        }
//	    }
//
//	    /**
//	     * Get current CE and CM assignment for the ATM
//	     */
//	    private ATMCurrentAssignment getCurrentATMAssignment(String atmId) {
//	        List<Object[]> currentAssignment = repository.getCurrentATMAssignment(atmId);
//	        
//	        if (currentAssignment.isEmpty()) {
//	            throw new RuntimeException("No current assignment found for ATM: " + atmId);
//	        }
//	        
//	        Object[] assignmentData = currentAssignment.get(0);
//	        String currentCeUserId = (String) assignmentData[0];
//	        String cmUserId = (String) assignmentData[1];
//	        
//	        if (currentCeUserId == null || cmUserId == null) {
//	            throw new RuntimeException("ATM " + atmId + " has incomplete assignment data");
//	        }
//	        
//	        return new ATMCurrentAssignment(currentCeUserId, cmUserId);
//	    }
//
//	    /**
//	     * Get ATM coordinates with proper error handling
//	     */
//	    private double[] getATMCoordinates(String atmId) {
//	        List<Object[]> atmCoordinates = repository.getATMCoordinates(atmId);
//	        
//	        if (atmCoordinates.isEmpty()) {
//	            throw new RuntimeException("ATM not found: " + atmId);
//	        }
//	        
//	        Object[] atmData = atmCoordinates.get(0);
//	        if (atmData[0] == null || atmData[1] == null) {
//	            throw new RuntimeException("ATM " + atmId + " has missing coordinates");
//	        }
//	        
//	        return new double[]{
//	            ((Number) atmData[0]).doubleValue(),
//	            ((Number) atmData[1]).doubleValue()
//	        };
//	    }
//
//	    /**
//	     * Get available CEs excluding the current CE assigned to the ATM
//	     */
//	    private List<CEUserLocation> getAvailableCEsExcludingCurrent(String cmUserId, String currentCeUserId) {
//	        List<Object[]> results = repository.getCEUserLocations(cmUserId, currentCeUserId);
//	        
//	        List<CEUserLocation> validCEs = new ArrayList<>();
//	        int skippedCount = 0;
//	        
//	        for (Object[] row : results) {
//	            String ceUserId = (String) row[0];
//	            
//	            if (row[1] == null || row[2] == null) {
//	                log.warn("Skipping CE {} - missing coordinates", ceUserId);
//	                skippedCount++;
//	                continue;
//	            }
//	            
//	            try {
//	                CEUserLocation location = new CEUserLocation(
//	                    ceUserId,
//	                    ((Number) row[1]).doubleValue(),
//	                    ((Number) row[2]).doubleValue()
//	                );
//	                validCEs.add(location);
//	            } catch (Exception e) {
//	                log.warn("Skipping CE {} - invalid coordinate data: {}", ceUserId, e.getMessage());
//	                skippedCount++;
//	            }
//	        }
//	        
//	        if (skippedCount > 0) {
//	            log.warn("Skipped {} CEs due to invalid data", skippedCount);
//	        }
//	        
//	        log.info("Retrieved {} valid alternative CEs (excluding current CE: {})", 
//	                validCEs.size(), currentCeUserId);
//	        return validCEs;
//	    }
//
//	    /**
//	     * Calculate distances to all available CEs
//	     */
//	    private List<ATMReassignmentResponse> calculateDistancesToCEs(
//	            String atmId, double atmLat, double atmLon, 
//	            List<CEUserLocation> availableCEs, String originalCeUserId) {
//	        
//	        List<ATMReassignmentResponse> ceDistances = new ArrayList<>();
//	        
//	        for (CEUserLocation ce : availableCEs) {
//	            log.debug("Calculating distance from ATM {} to CE {}", atmId, ce.getCeUserId());
//	            
//	            try {
//	                DistanceResult result = geoapifyService.calculateDistance(
//	                    ce.getUserLat(), ce.getUserLong(), atmLat, atmLon);
//	                
//	                ATMReassignmentResponse response = new ATMReassignmentResponse(
//	                    atmId,
//	                    originalCeUserId, // Original CE (current assignment)
//	                    ce.getCeUserId(), // New CE (alternative)
//	                    result.getDistance(),
//	                    result.getTravelTime()
//	                );
//	                
//	                ceDistances.add(response);
//	                
//	                log.debug("Distance from ATM {} to CE {}: {} km, {} min", 
//	                        atmId, ce.getCeUserId(), 
//	                        String.format("%.2f", result.getDistance()),
//	                        String.format("%.2f", result.getTravelTime()));
//	                
//	            } catch (Exception e) {
//	                log.error("Error calculating distance from ATM {} to CE {}: {}", 
//	                         atmId, ce.getCeUserId(), e.getMessage());
//	            }
//	        }
//	        
//	        return ceDistances;
//	    }
//
//	    /**
//	     * Log top results for debugging
//	     */
//	    private void logTopResults(List<ATMReassignmentResponse> results, int count) {
//	        int logCount = Math.min(count, results.size());
//	        for (int i = 0; i < logCount; i++) {
//	            ATMReassignmentResponse response = results.get(i);
//	            log.info("Rank {}: CE {} - {} km, {} min", 
//	                    i + 1, response.getNewCeUserId(),
//	                    String.format("%.2f", response.getDistance()),
//	                    String.format("%.2f", response.getTravelTime()));
//	        }
//	    }
//
//	    /**
//	     * Inner class to hold current ATM assignment data
//	     */
//	    private static class ATMCurrentAssignment {
//	        private final String currentCeUserId;
//	        private final String cmUserId;
//	        
//	        public ATMCurrentAssignment(String currentCeUserId, String cmUserId) {
//	            this.currentCeUserId = currentCeUserId;
//	            this.cmUserId = cmUserId;
//	        }
//	        
//	        public String getCurrentCeUserId() {
//	            return currentCeUserId;
//	        }
//	        
//	        public String getCmUserId() {
//	            return cmUserId;
//	        }
//	    }
	    
	    
	    //------------------------------------------------
	    
	    /**
	     * IMPROVE METHOD: Manual reassignment for single ATM
	     * 1. Get current CE/CM assignment for the ATM
	     * 2. Get ATM coordinates
	     * 3. Get all available CEs excluding the current CE
	     * 4. Calculate distances and return sorted results
	     */
	    
	    
//	    public List<ATMReassignmentResponse> manualReassignSingleATM(String atmId) {
//	        log.info("Starting manual reassignment for ATM: {}", atmId);
//	        
//	        // Step 1: Validate input
//	        validateATMId(atmId);
//	        
//	        // Step 2: Get current ATM assignment (CE and CM)
//	        ATMCurrentAssignment currentAssignment = getCurrentATMAssignment(atmId);
//	        log.info("ATM {} current assignment - CE: {}, CM: {}", 
//	                atmId, currentAssignment.getCurrentCeUserId(), currentAssignment.getCmUserId());
//	        
//	        // Step 3: Get ATM coordinates
//	        double[] atmCoordinates = getATMCoordinates(atmId);
//	        log.info("ATM {} coordinates: ({}, {})", atmId, atmCoordinates[0], atmCoordinates[1]);
//	        
//	        // Step 4: Get available CEs (excluding current CE)
//	        List<CEUserLocation> availableCEs = getAvailableCEsExcludingCurrent(
//	            currentAssignment.getCmUserId(), 
//	            currentAssignment.getCurrentCeUserId()
//	        );
//	        
//	        if (availableCEs.isEmpty()) {
//	            throw new RuntimeException("No alternative CEs found for manual assignment");
//	        }
//	        
//	        log.info("Found {} alternative CEs for manual assignment", availableCEs.size());
//	        
//	        // Step 5: Calculate distances to all alternative CEs
//	        List<ATMReassignmentResponse> ceDistances = calculateDistancesToCEs(
//	            atmId, 
//	            atmCoordinates[0], 
//	            atmCoordinates[1], 
//	            availableCEs, 
//	            currentAssignment.getCurrentCeUserId()
//	        );
//	        
//	        if (ceDistances.isEmpty()) {
//	            throw new RuntimeException("Could not calculate distances to any alternative CEs");
//	        }
//	        
//	        // Step 6: Sort by shortest distance
//	        ceDistances.sort(Comparator.comparing(ATMReassignmentResponse::getDistance));
//	        
//	        log.info("Completed manual reassignment for ATM {}. Found {} alternative CE options", 
//	                atmId, ceDistances.size());
//	        
//	        logTopResults(ceDistances, 5);
//	        return ceDistances;
//	    }
//
//	    /**
//	     * Validate ATM ID input
//	     */
//	    private void validateATMId(String atmId) {
//	        if (atmId == null || atmId.trim().isEmpty()) {
//	            throw new IllegalArgumentException("ATM ID cannot be null or empty");
//	        }
//	    }
//
//	    /**
//	     * Get current CE and CM assignment for the ATM
//	     */
//	    private ATMCurrentAssignment getCurrentATMAssignment(String atmId) {
//	        List<Object[]> currentAssignment = repository.getCurrentATMAssignment(atmId);
//	        
//	        if (currentAssignment.isEmpty()) {
//	            throw new RuntimeException("No current assignment found for ATM: " + atmId);
//	        }
//	        
//	        Object[] assignmentData = currentAssignment.get(0);
//	        String currentCeUserId = (String) assignmentData[0];
//	        String cmUserId = (String) assignmentData[1];
//	        
//	        if (currentCeUserId == null || cmUserId == null) {
//	            throw new RuntimeException("ATM " + atmId + " has incomplete assignment data");
//	        }
//	        
//	        return new ATMCurrentAssignment(currentCeUserId, cmUserId);
//	    }
//
//	    /**
//	     * Get ATM coordinates with proper error handling
//	     */
//	    private double[] getATMCoordinates(String atmId) {
//	        List<Object[]> atmCoordinates = repository.getATMCoordinates(atmId);
//	        
//	        if (atmCoordinates.isEmpty()) {
//	            throw new RuntimeException("ATM not found: " + atmId);
//	        }
//	        
//	        Object[] atmData = atmCoordinates.get(0);
//	        if (atmData[0] == null || atmData[1] == null) {
//	            throw new RuntimeException("ATM " + atmId + " has missing coordinates");
//	        }
//	        
//	        return new double[]{
//	            ((Number) atmData[0]).doubleValue(),
//	            ((Number) atmData[1]).doubleValue()
//	        };
//	    }
//
//	    /**
//	     * Get available CEs excluding the current CE assigned to the ATM
//	     * Handles 6 columns from stored procedure: ce_user_id, USER_LAT, USER_LONG, employee_code, home_address, atm_count
//	     */
//	    private List<CEUserLocation> getAvailableCEsExcludingCurrent(String cmUserId, String currentCeUserId) {
//	        List<Object[]> results = repository.getCEUserLocations(cmUserId, currentCeUserId);
//	        
//	        List<CEUserLocation> validCEs = new ArrayList<>();
//	        int skippedCount = 0;
//	        
//	        log.info("Processing {} CE records from stored procedure", results.size());
//	        
//	        for (Object[] row : results) {
//	            try {
//	                // Log the complete row data for debugging
//	                log.debug("Processing row with {} columns: {}", row.length, java.util.Arrays.toString(row));
//	                
//	                String ceUserId = (String) row[0];
//	                
//	                // Check for required coordinates
//	                if (row[1] == null || row[2] == null) {
//	                    log.warn("Skipping CE {} - missing coordinates", ceUserId);
//	                    skippedCount++;
//	                    continue;
//	                }
//	                
//	                // Extract all fields from the 6-column result set
//	                String employeeCode = row.length > 3 && row[3] != null ? row[3].toString().trim() : null;
//	                String homeAddress = row.length > 4 && row[4] != null ? row[4].toString().trim() : null;
//	                Integer atmCount = row.length > 5 && row[5] != null ? ((Number) row[5]).intValue() : 0;
//	                
//	                CEUserLocation location = new CEUserLocation(
//	                    ceUserId,
//	                    ((Number) row[1]).doubleValue(),
//	                    ((Number) row[2]).doubleValue(),
//	                    employeeCode,
//	                    homeAddress,
//	                    atmCount
//	                );
//	                
//	                validCEs.add(location);
//	                
//	                log.debug("Successfully processed CE: {} - Employee: {}, ATM Count: {}", 
//	                         ceUserId, employeeCode, atmCount);
//	                
//	            } catch (Exception e) {
//	                log.error("Error processing CE data at row index: {} - Error: {}", 
//	                         validCEs.size() + skippedCount, e.getMessage(), e);
//	                skippedCount++;
//	            }
//	        }
//	        
//	        if (skippedCount > 0) {
//	            log.warn("Skipped {} CEs due to invalid data", skippedCount);
//	        }
//	        
//	        log.info("Retrieved {} valid alternative CEs (excluding current CE: {})", 
//	                validCEs.size(), currentCeUserId);
//	        
//	        // Log sample of processed CEs for verification
//	        validCEs.stream().limit(3).forEach(ce -> 
//	            log.info("Sample CE: {} - Employee: {}, Address: {}, ATM Count: {}", 
//	                    ce.getCeUserId(), ce.getEmployeeCode(), 
//	                    ce.getHomeAddress() != null ? ce.getHomeAddress().substring(0, Math.min(30, ce.getHomeAddress().length())) + "..." : "null",
//	                    ce.getAtmCount())
//	        );
//	        
//	        return validCEs;
//	    }
//
//	    /**
//	     * Calculate distances to all available CEs
//	     * Updated to include employee_code, home_address, and atm_count in response
//	     */
//	    private List<ATMReassignmentResponse> calculateDistancesToCEs(
//	            String atmId, double atmLat, double atmLon, 
//	            List<CEUserLocation> availableCEs, String originalCeUserId) {
//	        
//	        List<ATMReassignmentResponse> ceDistances = new ArrayList<>();
//	        
//	        for (CEUserLocation ce : availableCEs) {
//	            log.debug("Calculating distance from ATM {} to CE {}", atmId, ce.getCeUserId());
//	            
//	            try {
//	                DistanceResult result = geoapifyService.calculateDistance(
//	                    ce.getUserLat(), ce.getUserLong(), atmLat, atmLon);
//	                
//	                ATMReassignmentResponse response = new ATMReassignmentResponse(
//	                    atmId,
//	                    originalCeUserId, // Original CE (current assignment)
//	                    ce.getCeUserId(), // New CE (alternative)
//	                    ce.getEmployeeCode(), // Employee code
//	                    ce.getHomeAddress(), // Home address
//	                    ce.getAtmCount(), // ATM count
//	                    result.getDistance(), // Distance
//	                    result.getTravelTime() // Travel time
//	                );
//	                
//	                ceDistances.add(response);
//	                
//	                log.debug("Distance from ATM {} to CE {} ({}): {} km, {} min, ATM Count: {}", 
//	                        atmId, ce.getCeUserId(), ce.getEmployeeCode(),
//	                        String.format("%.2f", result.getDistance()),
//	                        String.format("%.2f", result.getTravelTime()),
//	                        ce.getAtmCount());
//	                
//	            } catch (Exception e) {
//	                log.error("Error calculating distance from ATM {} to CE {}: {}", 
//	                         atmId, ce.getCeUserId(), e.getMessage());
//	            }
//	        }
//	        
//	        return ceDistances;
//	    }
//
//	    /**
//	     * Log top results for debugging
//	     * Updated to include new fields in logging
//	     */
//	    private void logTopResults(List<ATMReassignmentResponse> results, int count) {
//	        int logCount = Math.min(count, results.size());
//	        for (int i = 0; i < logCount; i++) {
//	            ATMReassignmentResponse response = results.get(i);
//	            log.info("Rank {}: CE {} ({}) - {} km, {} min, ATM Count: {}", 
//	                    i + 1, 
//	                    response.getNewCeUserId(),
//	                    response.getEmployeeCode(),
//	                    String.format("%.2f", response.getDistance()),
//	                    String.format("%.2f", response.getTravelTime()),
//	                    response.getAtmCount());
//	        }
//	    }
//
//	    /**
//	     * Inner class to hold current ATM assignment data
//	     */
//	    private static class ATMCurrentAssignment {
//	        private final String currentCeUserId;
//	        private final String cmUserId;
//	        
//	        public ATMCurrentAssignment(String currentCeUserId, String cmUserId) {
//	            this.currentCeUserId = currentCeUserId;
//	            this.cmUserId = cmUserId;
//	        }
//	        
//	        public String getCurrentCeUserId() {
//	            return currentCeUserId;
//	        }
//	        
//	        public String getCmUserId() {
//	            return cmUserId;
//	        }
//	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    //added the code to save ----------------------------------------
	    
	    
	    /**
	     * Enhanced manual reassignment for single ATM with history storage
	     * 1. Get current CE/CM assignment for the ATM
	     * 2. Get ATM coordinates  
	     * 3. Get all available CEs excluding the current CE
	     * 4. Calculate distances and return sorted results
	     * 5. Store all proposals in history table
	     */
	    

	    @Value("${reassignment.cleanup-previous:true}")
	    private boolean cleanupPreviousProposals;

	    
	    public List<ATMReassignmentResponse> manualReassignSingleATM(String atmId) {
	        return manualReassignSingleATM(atmId, "SYSTEM");
	    }

	    /**
	     * Enhanced manual reassignment with user tracking
	     */
	   
	    public List<ATMReassignmentResponse> manualReassignSingleATM(String atmId, String createdBy) {
	        log.info("Starting manual reassignment for ATM: {} by user: {}", atmId, createdBy);
	        
	        try {
	            // Step 1: Validate input
	            validateATMId(atmId);
	            
	            // Step 2: Clean up previous proposals if enabled
	            if (cleanupPreviousProposals) {
	                cleanupPreviousProposals(atmId);
	            }
	            
	            // Step 3: Get current ATM assignment (CE and CM)
	            ATMCurrentAssignment currentAssignment = getCurrentATMAssignment(atmId);
	            log.info("ATM {} current assignment - CE: {}, CM: {}", 
	                    atmId, currentAssignment.getCurrentCeUserId(), currentAssignment.getCmUserId());
	            
	            // Step 4: Get ATM coordinates
	            double[] atmCoordinates = getATMCoordinates(atmId);
	            log.info("ATM {} coordinates: ({}, {})", atmId, atmCoordinates[0], atmCoordinates[1]);
	            
	            // Step 5: Get available CEs (excluding current CE)
	            List<CEUserLocation> availableCEs = getAvailableCEsExcludingCurrent(
	                currentAssignment.getCmUserId(), 
	                currentAssignment.getCurrentCeUserId()
	            );
	            
	            if (availableCEs.isEmpty()) {
	                throw new RuntimeException("No alternative CEs found for manual assignment");
	            }
	            
	            log.info("Found {} alternative CEs for manual assignment", availableCEs.size());
	            
	            // Step 6: Calculate distances to all alternative CEs
	            List<ATMReassignmentResponse> ceDistances = calculateDistancesToCEs(
	                atmId, 
	                atmCoordinates[0], 
	                atmCoordinates[1], 
	                availableCEs, 
	                currentAssignment.getCurrentCeUserId()
	            );
	            
	            if (ceDistances.isEmpty()) {
	                throw new RuntimeException("Could not calculate distances to any alternative CEs");
	            }
	            
	            // Step 7: Sort by shortest distance
	            ceDistances.sort(Comparator.comparing(ATMReassignmentResponse::getDistance));
	            
	            // Step 8: Store all proposals in history table
	            List<ATMReassignmentHistory_AtmWise> savedHistory = storeReassignmentHistory(ceDistances, createdBy);
	            log.info("Stored {} reassignment proposals in atm_reassignment_history_atm_wise table", savedHistory.size());
	            
	            // Step 9: Update response with saved IDs
	            updateResponseWithHistoryIds(ceDistances, savedHistory);
	            
	            log.info("Completed manual reassignment for ATM {}. Found {} alternative CE options", 
	                    atmId, ceDistances.size());
	            
	            logTopResults(ceDistances, 5);
	            return ceDistances;
	            
	        } catch (Exception e) {
	            log.error("Error during manual reassignment for ATM {}: {}", atmId, e.getMessage(), e);
	            throw e;
	        }
	    }

	    /**
	     * Clean up previous PROPOSED reassignments for the ATM
	     */
	    private void cleanupPreviousProposals(String atmId) {
	        try {
	            List<ATMReassignmentHistory_AtmWise> existingProposals = historyRepository
	                .findAll().stream()
	                .filter(h -> atmId.equals(h.getAtmCode()) && "PROPOSED".equals(h.getAssignmentStatus()))
	                .toList();
	            
	            if (!existingProposals.isEmpty()) {
	                historyRepository.deleteAll(existingProposals);
	                log.info("Cleaned up {} previous PROPOSED reassignments for ATM: {}", existingProposals.size(), atmId);
	            }
	        } catch (Exception e) {
	            log.warn("Failed to cleanup previous proposals for ATM {}: {}", atmId, e.getMessage());
	        }
	    }

	    /**
	     * Store reassignment history in database
	     */
	    private List<ATMReassignmentHistory_AtmWise> storeReassignmentHistory(List<ATMReassignmentResponse> responses, String createdBy) {
	        List<ATMReassignmentHistory_AtmWise> historyEntities = new ArrayList<>();
	        
	        for (int i = 0; i < responses.size(); i++) {
	            ATMReassignmentResponse response = responses.get(i);
	            
	            ATMReassignmentHistory_AtmWise history = ATMReassignmentHistory_AtmWise.builder()
	                .atmCode(response.getAtmCode())
	                .originalCeUserId(response.getOriginalCeUserId())
	                .newCeUserId(response.getNewCeUserId())
	                .employeeCode(response.getEmployeeCode())
	                .homeAddress(response.getHomeAddress())
	                .atmCount(response.getAtmCount())
	                .distance(response.getDistance())
	                .travelTime(response.getTravelTime())
	                .assignmentStatus("PROPOSED")
	                .rankPosition(i + 1) // 1-based ranking
	                .isSelected(false)
	                .createdBy(createdBy)
	                .createdDate(LocalDateTime.now())
	                .build();
	            
	            historyEntities.add(history);
	        }
	        
	        try {
	            List<ATMReassignmentHistory_AtmWise> saved = historyRepository.saveAll(historyEntities);
	            log.info("Successfully stored {} reassignment history records", saved.size());
	            return saved;
	        } catch (Exception e) {
	            log.error("Failed to store reassignment history: {}", e.getMessage(), e);
	            throw new RuntimeException("Failed to store reassignment history", e);
	        }
	    }

	    /**
	     * Update response objects with database IDs
	     */
	    private void updateResponseWithHistoryIds(List<ATMReassignmentResponse> responses, List<ATMReassignmentHistory_AtmWise> savedHistory) {
	        if (responses.size() != savedHistory.size()) {
	            log.warn("Mismatch between response count ({}) and saved history count ({})", 
	                    responses.size(), savedHistory.size());
	            return;
	        }
	        
	        for (int i = 0; i < responses.size(); i++) {
	            responses.get(i).setId(savedHistory.get(i).getId());
	        }
	    }

	    /**
	     * Get reassignment history for an ATM
	     */
	    public List<ATMReassignmentHistory_AtmWise> getReassignmentHistory(String atmCode) {
	        return historyRepository.findAll().stream()
	            .filter(h -> atmCode.equals(h.getAtmCode()))
	            .sorted((h1, h2) -> h2.getCreatedDate().compareTo(h1.getCreatedDate()))
	            .toList();
	    }

	    /**
	     * Get proposed reassignments for an ATM
	     */
	    public List<ATMReassignmentHistory_AtmWise> getProposedReassignments(String atmCode) {
	        return historyRepository.findAll().stream()
	            .filter(h -> atmCode.equals(h.getAtmCode()) && "PROPOSED".equals(h.getAssignmentStatus()))
	            .sorted((h1, h2) -> h1.getRankPosition().compareTo(h2.getRankPosition()))
	            .toList();
	    }

	    /**
	     * Approve a reassignment proposal
	     */
	   
	    public void approveReassignment(Long historyId, String approvedBy) {
	        ATMReassignmentHistory_AtmWise history = historyRepository.findById(historyId)
	            .orElseThrow(() -> new RuntimeException("Reassignment history not found with ID: " + historyId));
	        
	        // Update status to APPROVED
	        history.setAssignmentStatus("APPROVED");
	        history.setApprovedDate(LocalDateTime.now());
	        history.setApprovedBy(approvedBy);
	        history.setIsSelected(true);
	        
	        // Unmark other proposals for the same ATM
	        List<ATMReassignmentHistory_AtmWise> otherProposals = historyRepository.findAll().stream()
	            .filter(h -> history.getAtmCode().equals(h.getAtmCode()) && !h.getId().equals(historyId))
	            .toList();
	        
	        otherProposals.forEach(h -> h.setIsSelected(false));
	        
	        historyRepository.save(history);
	        historyRepository.saveAll(otherProposals);
	        
	        log.info("Approved reassignment for ATM {} from CE {} to CE {} by user {}", 
	                history.getAtmCode(), history.getOriginalCeUserId(), 
	                history.getNewCeUserId(), approvedBy);
	    }

	    /**
	     * Reject a reassignment proposal
	     */
	    
	    public void rejectReassignment(Long historyId, String rejectedBy, String remarks) {
	        ATMReassignmentHistory_AtmWise history = historyRepository.findById(historyId)
	            .orElseThrow(() -> new RuntimeException("Reassignment history not found with ID: " + historyId));
	        
	        history.setAssignmentStatus("REJECTED");
	        history.setApprovedDate(LocalDateTime.now());
	        history.setApprovedBy(rejectedBy);
	        
	        if (remarks != null && !remarks.trim().isEmpty()) {
	            history.setRemarks(remarks);
	        }
	        
	        historyRepository.save(history);
	        
	        log.info("Rejected reassignment with ID {} by user {}", historyId, rejectedBy);
	    }

	    /**
	     * Check if ATM has pending reassignments
	     */
	    public boolean hasPendingReassignments(String atmCode) {
	        return historyRepository.findAll().stream()
	            .anyMatch(h -> atmCode.equals(h.getAtmCode()) && "PROPOSED".equals(h.getAssignmentStatus()));
	    }

	    /**
	     * Validate ATM ID input
	     */
	    private void validateATMId(String atmId) {
	        if (atmId == null || atmId.trim().isEmpty()) {
	            throw new IllegalArgumentException("ATM ID cannot be null or empty");
	        }
	    }

	    /**
	     * Get current CE and CM assignment for the ATM
	     */
	    private ATMCurrentAssignment getCurrentATMAssignment(String atmId) {
	        List<Object[]> currentAssignment = repository.getCurrentATMAssignment(atmId);
	        
	        if (currentAssignment.isEmpty()) {
	            throw new RuntimeException("No current assignment found for ATM: " + atmId);
	        }
	        
	        Object[] assignmentData = currentAssignment.get(0);
	        String currentCeUserId = (String) assignmentData[0];
	        String cmUserId = (String) assignmentData[1];
	        
	        if (currentCeUserId == null || cmUserId == null) {
	            throw new RuntimeException("ATM " + atmId + " has incomplete assignment data");
	        }
	        
	        return new ATMCurrentAssignment(currentCeUserId, cmUserId);
	    }

	    /**
	     * Get ATM coordinates with proper error handling
	     */
	    private double[] getATMCoordinates(String atmId) {
	        List<Object[]> atmCoordinates = repository.getATMCoordinates(atmId);
	        
	        if (atmCoordinates.isEmpty()) {
	            throw new RuntimeException("ATM not found: " + atmId);
	        }
	        
	        Object[] atmData = atmCoordinates.get(0);
	        if (atmData[0] == null || atmData[1] == null) {
	            throw new RuntimeException("ATM " + atmId + " has missing coordinates");
	        }
	        
	        return new double[]{
	            ((Number) atmData[0]).doubleValue(),
	            ((Number) atmData[1]).doubleValue()
	        };
	    }

	    /**
	     * Get available CEs excluding the current CE assigned to the ATM
	     * Handles 6 columns from stored procedure: ce_user_id, USER_LAT, USER_LONG, employee_code, home_address, atm_count
	     */
	    private List<CEUserLocation> getAvailableCEsExcludingCurrent(String cmUserId, String currentCeUserId) {
	        List<Object[]> results = repository.getCEUserLocations(cmUserId, currentCeUserId);
	        
	        List<CEUserLocation> validCEs = new ArrayList<>();
	        int skippedCount = 0;
	        
	        log.info("Processing {} CE records from stored procedure", results.size());
	        
	        for (Object[] row : results) {
	            try {
	                // Log the complete row data for debugging
	                log.debug("Processing row with {} columns: {}", row.length, java.util.Arrays.toString(row));
	                
	                String ceUserId = (String) row[0];
	                
	                // Check for required coordinates
	                if (row[1] == null || row[2] == null) {
	                    log.warn("Skipping CE {} - missing coordinates", ceUserId);
	                    skippedCount++;
	                    continue;
	                }
	                
	                // Extract all fields from the 6-column result set
	                String employeeCode = row.length > 3 && row[3] != null ? row[3].toString().trim() : null;
	                String homeAddress = row.length > 4 && row[4] != null ? row[4].toString().trim() : null;
	                Integer atmCount = row.length > 5 && row[5] != null ? ((Number) row[5]).intValue() : 0;
	                
	                CEUserLocation location = new CEUserLocation(
	                    ceUserId,
	                    ((Number) row[1]).doubleValue(),
	                    ((Number) row[2]).doubleValue(),
	                    employeeCode,
	                    homeAddress,
	                    atmCount
	                );
	                
	                validCEs.add(location);
	                
	                log.debug("Successfully processed CE: {} - Employee: {}, ATM Count: {}", 
	                         ceUserId, employeeCode, atmCount);
	                
	            } catch (Exception e) {
	                log.error("Error processing CE data at row index: {} - Error: {}", 
	                         validCEs.size() + skippedCount, e.getMessage(), e);
	                skippedCount++;
	            }
	        }
	        
	        if (skippedCount > 0) {
	            log.warn("Skipped {} CEs due to invalid data", skippedCount);
	        }
	        
	        log.info("Retrieved {} valid alternative CEs (excluding current CE: {})", 
	                validCEs.size(), currentCeUserId);
	        
	        // Log sample of processed CEs for verification
	        validCEs.stream().limit(3).forEach(ce -> 
	            log.info("Sample CE: {} - Employee: {}, Address: {}, ATM Count: {}", 
	                    ce.getCeUserId(), ce.getEmployeeCode(), 
	                    ce.getHomeAddress() != null ? ce.getHomeAddress().substring(0, Math.min(30, ce.getHomeAddress().length())) + "..." : "null",
	                    ce.getAtmCount())
	        );
	        
	        return validCEs;
	    }

	    /**
	     * Calculate distances to all available CEs
	     * Updated to include employee_code, home_address, and atm_count in response
	     */
	    private List<ATMReassignmentResponse> calculateDistancesToCEs(
	            String atmId, double atmLat, double atmLon, 
	            List<CEUserLocation> availableCEs, String originalCeUserId) {
	        
	        List<ATMReassignmentResponse> ceDistances = new ArrayList<>();
	        
	        for (CEUserLocation ce : availableCEs) {
	            log.debug("Calculating distance from ATM {} to CE {}", atmId, ce.getCeUserId());
	            
	            try {
	                DistanceResult result = geoapifyService.calculateDistance(
	                    ce.getUserLat(), ce.getUserLong(), atmLat, atmLon);
	                
	                ATMReassignmentResponse response = new ATMReassignmentResponse(
	                    atmId,
	                    originalCeUserId, // Original CE (current assignment)
	                    ce.getCeUserId(), // New CE (alternative)
	                    ce.getEmployeeCode(), // Employee code
	                    ce.getHomeAddress(), // Home address
	                    ce.getAtmCount(), // ATM count
	                    result.getDistance(), // Distance
	                    result.getTravelTime() // Travel time
	                );
	                
	                ceDistances.add(response);
	                
	                log.debug("Distance from ATM {} to CE {} ({}): {} km, {} min, ATM Count: {}", 
	                        atmId, ce.getCeUserId(), ce.getEmployeeCode(),
	                        String.format("%.2f", result.getDistance()),
	                        String.format("%.2f", result.getTravelTime()),
	                        ce.getAtmCount());
	                
	            } catch (Exception e) {
	                log.error("Error calculating distance from ATM {} to CE {}: {}", 
	                         atmId, ce.getCeUserId(), e.getMessage());
	            }
	        }
	        
	        return ceDistances;
	    }

	    /**
	     * Log top results for debugging
	     * Updated to include new fields in logging
	     */
	    private void logTopResults(List<ATMReassignmentResponse> results, int count) {
	        int logCount = Math.min(count, results.size());
	        for (int i = 0; i < logCount; i++) {
	            ATMReassignmentResponse response = results.get(i);
	            log.info("Rank {}: CE {} ({}) - {} km, {} min, ATM Count: {}", 
	                    i + 1, 
	                    response.getNewCeUserId(),
	                    response.getEmployeeCode(),
	                    String.format("%.2f", response.getDistance()),
	                    String.format("%.2f", response.getTravelTime()),
	                    response.getAtmCount());
	        }
	    }

	    /**
	     * Inner class to hold current ATM assignment data
	     */
	    private static class ATMCurrentAssignment {
	        private final String currentCeUserId;
	        private final String cmUserId;
	        
	        public ATMCurrentAssignment(String currentCeUserId, String cmUserId) {
	            this.currentCeUserId = currentCeUserId;
	            this.cmUserId = cmUserId;
	        }
	        
	        public String getCurrentCeUserId() {
	            return currentCeUserId;
	        }
	        
	        public String getCmUserId() {
	            return cmUserId;
	        }
	    }
	    
	    
	    
	    
	    
	    
	    
	    
}
