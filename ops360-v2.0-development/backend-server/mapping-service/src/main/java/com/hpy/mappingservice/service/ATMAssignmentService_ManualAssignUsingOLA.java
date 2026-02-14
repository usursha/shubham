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
import com.hpy.mappingservice.service.OlaMapsService.DistanceResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ATMAssignmentService_ManualAssignUsingOLA {

	
	private final ATMAssignmentRepository repository;
    private final OlaMapsService olaMapsService; // Changed from GeoapifyService to OlaMapsService
    private final ATMReassignmentHistory_AtmWiseRepository historyRepository;

    @Value("${reassignment.cleanup-previous:true}")
    private boolean cleanupPreviousProposals;

    /**
     * Manual reassignment with system user
     */
    public List<ATMReassignmentResponse> manualReassignSingleATM(String atmId) {
        return manualReassignSingleATM(atmId, "SYSTEM");
    }

    /**
     * Enhanced manual reassignment with user tracking using OLA Maps
     */
    public List<ATMReassignmentResponse> manualReassignSingleATM(String atmId, String createdBy) {
        log.info("Starting manual reassignment for ATM: {} by user: {} using OLA Maps", atmId, createdBy);
        
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
            log.info("ATM {} coordinates for OLA Maps calculation: ({}, {})", 
                    atmId, atmCoordinates[0], atmCoordinates[1]);
            
            // Step 5: Get available CEs (excluding current CE)
            List<CEUserLocation> availableCEs = getAvailableCEsExcludingCurrent(
                currentAssignment.getCmUserId(), 
                currentAssignment.getCurrentCeUserId()
            );
            
            if (availableCEs.isEmpty()) {
                throw new RuntimeException("No alternative CEs found for manual assignment");
            }
            
            log.info("Found {} alternative CEs for manual assignment using OLA Maps", availableCEs.size());
            
            // Step 6: Calculate distances to all alternative CEs using OLA Maps
            List<ATMReassignmentResponse> ceDistances = calculateDistancesToCEs(
                atmId, 
                atmCoordinates[0], 
                atmCoordinates[1], 
                availableCEs, 
                currentAssignment.getCurrentCeUserId()
            );
            
            if (ceDistances.isEmpty()) {
                throw new RuntimeException("Could not calculate distances to any alternative CEs using OLA Maps");
            }
            
            // Step 7: Sort by shortest distance
            ceDistances.sort(Comparator.comparing(ATMReassignmentResponse::getDistance));
            
            // Step 8: Store all proposals in history table
            List<ATMReassignmentHistory_AtmWise> savedHistory = storeReassignmentHistory(ceDistances, createdBy);
            log.info("Stored {} reassignment proposals calculated with OLA Maps in atm_reassignment_history_atm_wise table", 
                    savedHistory.size());
            
            // Step 9: Update response with saved IDs
            updateResponseWithHistoryIds(ceDistances, savedHistory);
            
            log.info("Completed manual reassignment for ATM {} using OLA Maps. Found {} alternative CE options", 
                    atmId, ceDistances.size());
            
            logTopResults(ceDistances, 5);
            return ceDistances;
            
        } catch (Exception e) {
            log.error("Error during manual reassignment for ATM {} using OLA Maps: {}", atmId, e.getMessage(), e);
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
     * Store reassignment history in database with OLA Maps calculation data
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
                .remarks("Calculated using OLA Maps") // Add source reference
                .build();
            
            historyEntities.add(history);
        }
        
        try {
            List<ATMReassignmentHistory_AtmWise> saved = historyRepository.saveAll(historyEntities);
            log.info("Successfully stored {} reassignment history records with OLA Maps data", saved.size());
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
        
        log.info("Approved OLA Maps calculated reassignment for ATM {} from CE {} to CE {} by user {}", 
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
        
        log.info("Rejected OLA Maps calculated reassignment with ID {} by user {}", historyId, rejectedBy);
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
        
        log.info("Processing {} CE records from stored procedure for OLA Maps calculation", results.size());
        
        for (Object[] row : results) {
            try {
                log.debug("Processing row with {} columns: {}", row.length, java.util.Arrays.toString(row));
                
                String ceUserId = (String) row[0];
                
                // Check for required coordinates
                if (row[1] == null || row[2] == null) {
                    log.warn("Skipping CE {} - missing coordinates for OLA Maps calculation", ceUserId);
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
                
                log.debug("Successfully processed CE for OLA Maps: {} - Employee: {}, ATM Count: {}", 
                         ceUserId, employeeCode, atmCount);
                
            } catch (Exception e) {
                log.error("Error processing CE data for OLA Maps at row index: {} - Error: {}", 
                         validCEs.size() + skippedCount, e.getMessage(), e);
                skippedCount++;
            }
        }
        
        if (skippedCount > 0) {
            log.warn("Skipped {} CEs due to invalid data for OLA Maps calculation", skippedCount);
        }
        
        log.info("Retrieved {} valid alternative CEs for OLA Maps calculation (excluding current CE: {})", 
                validCEs.size(), currentCeUserId);
        
        // Log sample of processed CEs for verification
        validCEs.stream().limit(3).forEach(ce -> 
            log.info("Sample CE for OLA Maps: {} - Employee: {}, Address: {}, ATM Count: {}", 
                    ce.getCeUserId(), ce.getEmployeeCode(), 
                    ce.getHomeAddress() != null ? ce.getHomeAddress().substring(0, Math.min(30, ce.getHomeAddress().length())) + "..." : "null",
                    ce.getAtmCount())
        );
        
        return validCEs;
    }

    /**
     * Calculate distances to all available CEs using OLA Maps
     * Updated to include employee_code, home_address, and atm_count in response
     */
    private List<ATMReassignmentResponse> calculateDistancesToCEs(
            String atmId, double atmLat, double atmLon, 
            List<CEUserLocation> availableCEs, String originalCeUserId) {
        
        List<ATMReassignmentResponse> ceDistances = new ArrayList<>();
        
        log.info("Starting OLA Maps distance calculations for {} CEs", availableCEs.size());
        
        for (CEUserLocation ce : availableCEs) {
            log.debug("Calculating distance from ATM {} to CE {} using OLA Maps", atmId, ce.getCeUserId());
            
            try {
                DistanceResult result = olaMapsService.calculateDistance(
                    ce.getUserLat(), ce.getUserLong(), atmLat, atmLon);
                
                ATMReassignmentResponse response = new ATMReassignmentResponse(
                    atmId,
                    originalCeUserId, // Original CE (current assignment)
                    ce.getCeUserId(), // New CE (alternative)
                    ce.getEmployeeCode(), // Employee code
                    ce.getHomeAddress(), // Home address
                    ce.getAtmCount(), // ATM count
                    result.getDistance(), // Distance from OLA Maps
                    result.getTravelTime() // Travel time from OLA Maps
                );
                
                ceDistances.add(response);
                
                log.debug("OLA Maps distance from ATM {} to CE {} ({}): {} km, {} min, ATM Count: {}", 
                        atmId, ce.getCeUserId(), ce.getEmployeeCode(),
                        String.format("%.2f", result.getDistance()),
                        String.format("%.2f", result.getTravelTime()),
                        ce.getAtmCount());
                
            } catch (Exception e) {
                log.error("Error calculating OLA Maps distance from ATM {} to CE {}: {}", 
                         atmId, ce.getCeUserId(), e.getMessage());
            }
        }
        
        log.info("Completed OLA Maps distance calculations for {} CE options", ceDistances.size());
        
        return ceDistances;
    }

    /**
     * Log top results for debugging with OLA Maps data
     */
    private void logTopResults(List<ATMReassignmentResponse> results, int count) {
        int logCount = Math.min(count, results.size());
        log.info("Top {} OLA Maps calculated reassignment options:", logCount);
        
        for (int i = 0; i < logCount; i++) {
            ATMReassignmentResponse response = results.get(i);
            log.info("Rank {}: CE {} ({}) - {} km, {} min, ATM Count: {} (OLA Maps)", 
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
