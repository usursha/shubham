package com.hpy.mappingservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.entity.ATMReassignmentHistory;
import com.hpy.mappingservice.repository.ATMAssignmentRepository;
import com.hpy.mappingservice.repository.ATMReassignmentHistoryRepository;
import com.hpy.mappingservice.request.dto.ATMReassignmentRequest;
import com.hpy.mappingservice.response.dto.ATMCEMapping;
import com.hpy.mappingservice.response.dto.ATMReassignmentResponse;
import com.hpy.mappingservice.response.dto.CEUserLocation;
import com.hpy.mappingservice.service.OlaMapsService.DistanceMatrix;
import com.hpy.mappingservice.service.OlaMapsService.NearestCEResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ATMAssignmentService_UsingOLA {
	 private final ATMAssignmentRepository repository;
	    private final OlaMapsService olaMapsService; // Changed from GeoapifyService to OlaMapsService
	    private final ATMReassignmentHistoryRepository reassignmentHistoryRepository;

	    @Autowired
	    private LoginService loginService;

	    /**
	     * Reassign ATMs from leaving CE to other CEs using OLA Maps service
	     */
	    public List<ATMReassignmentResponse> reassignATMs(ATMReassignmentRequest request) {
	        
	        String cmUserId = loginService.getLoggedInUser();
	        // Step 1: Get all ATMs assigned to the leaving CE
	        List<ATMCEMapping> leavingCeATMs = getATMsForCE(cmUserId, request.getLeavingCeUserId());
	        log.info("Found {} ATMs to reassign using OLA Maps", leavingCeATMs.size());

	        if (leavingCeATMs.isEmpty()) {
	            log.warn("No ATMs found for CE: {}", request.getLeavingCeUserId());
	            return new ArrayList<>();
	        }

	        // Step 2: Get all other CE locations (excluding the leaving CE)
	        List<CEUserLocation> availableCEs = getAvailableCEs(cmUserId, request.getLeavingCeUserId());
	        log.info("Found {} available CEs for OLA Maps calculation", availableCEs.size());

	        if (availableCEs.isEmpty()) {
	            throw new RuntimeException("No available CEs to reassign ATMs");
	        }

	        // Step 3: Calculate distance matrix using OLA Maps routing API
	        log.info("Calculating distances using OLA Maps API...");
	        DistanceMatrix distanceMatrix = olaMapsService.calculateDistanceMatrix(leavingCeATMs, availableCEs);

	        if (distanceMatrix == null) {
	            log.error("Failed to calculate distance matrix using OLA Maps");
	            return new ArrayList<>();
	        }

	        // Step 4: Find nearest CE for each ATM and create response + simplified history record
	        List<ATMReassignmentResponse> reassignments = new ArrayList<>();
	        List<ATMReassignmentHistory> historyRecords = new ArrayList<>();

	        for (int atmIndex = 0; atmIndex < leavingCeATMs.size(); atmIndex++) {
	            NearestCEResult nearest = olaMapsService.findNearestCE(atmIndex, distanceMatrix);

	            if (nearest != null) {
	                // Get additional CE details for the new assigned CE
	                CEUserLocation newCE = findCEById(availableCEs, nearest.getNearestCeUserId());
	                
	                // Create ATMReassignmentResponse with all required fields (for API response)
	                ATMReassignmentResponse response = new ATMReassignmentResponse();
	                response.setAtmCode(nearest.getAtmCode());
	                response.setOriginalCeUserId(request.getLeavingCeUserId());
	                response.setNewCeUserId(nearest.getNearestCeUserId());
	                response.setDistance(nearest.getDistance());
	                response.setTravelTime(nearest.getTravelTime());
	                
	                // Set additional fields for response
	                if (newCE != null) {
	                    response.setEmployeeCode(newCE.getEmployeeCode() != null ? newCE.getEmployeeCode() : "N/A");
	                    response.setHomeAddress(newCE.getHomeAddress() != null ? newCE.getHomeAddress() : "Address Not Available");
	                } else {
	                    response.setEmployeeCode("N/A");
	                    response.setHomeAddress("Address Not Available");
	                }
	                response.setAtmCount(getATMCountForCE(cmUserId, nearest.getNearestCeUserId()));

	                reassignments.add(response);

	                // Create SIMPLIFIED history record for database - ONLY required fields
	                ATMReassignmentHistory historyRecord = new ATMReassignmentHistory();
	                historyRecord.setAtmCode(nearest.getAtmCode());
	                historyRecord.setOriginalCeUserId(request.getLeavingCeUserId());
	                historyRecord.setNewCeUserId(nearest.getNearestCeUserId());
	                historyRecord.setEmployeeCode(response.getEmployeeCode());
	                historyRecord.setHomeAddress(response.getHomeAddress());
	                historyRecord.setDistance(BigDecimal.valueOf(nearest.getDistance()));
	                historyRecord.setTravelTime(BigDecimal.valueOf(nearest.getTravelTime()));
	                
	                // Set minimal additional fields for audit purposes
	                historyRecord.setReassignmentDate(LocalDateTime.now());
	                historyRecord.setCreatedBy(cmUserId);
	                historyRecord.setStatus("COMPLETED"); // Direct completion without approval workflow
	                
	                historyRecords.add(historyRecord);

	                log.info("ATM {} reassigned from {} to {} using OLA Maps (distance: {} km, time: {} min)", 
	                        nearest.getAtmCode(), request.getLeavingCeUserId(), nearest.getNearestCeUserId(),
	                        String.format("%.2f", nearest.getDistance()), String.format("%.2f", nearest.getTravelTime()));
	            }
	        }

	        // Step 5: Save only the essential data to database
	        try {
	            List<ATMReassignmentHistory> savedRecords = reassignmentHistoryRepository.saveAll(historyRecords);
	            log.info("Successfully saved {} ATM reassignment records calculated with OLA Maps to database", savedRecords.size());
	        } catch (Exception e) {
	            log.error("Failed to save reassignment history to database: {}", e.getMessage());
	            // Continue execution even if database save fails
	        }

	        log.info("Successfully reassigned {} ATMs using OLA Maps routing API", reassignments.size());
	        return reassignments;
	    }

	    // ... (keep all existing helper methods unchanged)
	    private CEUserLocation findCEById(List<CEUserLocation> availableCEs, String ceUserId) {
	        return availableCEs.stream()
	                .filter(ce -> ce.getCeUserId().equals(ceUserId))
	                .findFirst()
	                .orElse(null);
	    }

	    private Integer getATMCountForCE(String cmUserId, String ceUserId) {
	        try {
	            List<ATMCEMapping> atmList = getATMsForCE(cmUserId, ceUserId);
	            return atmList.size();
	        } catch (Exception e) {
	            log.warn("Failed to get ATM count for CE {}: {}", ceUserId, e.getMessage());
	            return 0;
	        }
	    }

	    private List<ATMCEMapping> getATMsForCE(String cmUserId, String ceUserId) {
	        List<Object[]> results = repository.getATMCEMappingDetails(cmUserId, ceUserId);
	        List<ATMCEMapping> validATMs = new ArrayList<>();
	        int skippedCount = 0;

	        for (Object[] row : results) {
	            String atmCode = (String) row[1];
	            if (row[2] == null || row[3] == null) {
	                log.warn("Skipping ATM {} - missing coordinates", atmCode);
	                skippedCount++;
	                continue;
	            }

	            try {
	                ATMCEMapping mapping = new ATMCEMapping((String) row[0], atmCode,
	                        ((Number) row[2]).doubleValue(), ((Number) row[3]).doubleValue());
	                validATMs.add(mapping);
	            } catch (ClassCastException | NullPointerException e) {
	                log.warn("Skipping ATM {} - invalid coordinate data", atmCode);
	                skippedCount++;
	            }
	        }

	        if (skippedCount > 0) {
	            log.warn("Skipped {} ATMs due to missing/invalid data", skippedCount);
	        }
	        return validATMs;
	    }

	    private List<CEUserLocation> getAvailableCEs(String cmUserId, String excludedCeUserId) {
	        List<Object[]> results = repository.getCEUserLocations(cmUserId, excludedCeUserId);
	        List<CEUserLocation> validCEs = new ArrayList<>();
	        int skippedCount = 0;

	        for (Object[] row : results) {
	            String ceUserId = (String) row[0];
	            if (row[1] == null || row[2] == null) {
	                log.warn("Skipping CE {} - missing coordinates", ceUserId);
	                skippedCount++;
	                continue;
	            }

	            try {
	                CEUserLocation location = new CEUserLocation();
	                location.setCeUserId(ceUserId);
	                location.setUserLat(((Number) row[1]).doubleValue());
	                location.setUserLong(((Number) row[2]).doubleValue());
	                
	                if (row.length > 3) {
	                    location.setEmployeeCode(row[3] != null ? (String) row[3] : null);
	                }
	                if (row.length > 4) {
	                    location.setHomeAddress(row[4] != null ? (String) row[4] : null);
	                }
	                
	                validCEs.add(location);
	            } catch (ClassCastException | NullPointerException e) {
	                log.warn("Skipping CE {} - invalid coordinate data", ceUserId);
	                skippedCount++;
	            }
	        }

	        if (skippedCount > 0) {
	            log.warn("Skipped {} CEs due to missing/invalid data", skippedCount);
	        }
	        return validCEs;
	    }
}
