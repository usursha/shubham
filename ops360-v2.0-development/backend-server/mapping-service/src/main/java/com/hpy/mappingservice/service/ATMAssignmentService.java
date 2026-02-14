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
import com.hpy.mappingservice.service.GeoapifyService.DistanceMatrix;
import com.hpy.mappingservice.service.GeoapifyService.NearestCEResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ATMAssignmentService {

	private final ATMAssignmentRepository repository;
	private final GeoapifyService geoapifyService;
	
    private final ATMReassignmentHistoryRepository reassignmentHistoryRepository;

	
	@Autowired
	private LoginService loginService;
	

//	/**
//	 * Reassign ATMs from leaving CE to other CEs using individual Routing API calls
//	 */
//	public List<ATMReassignmentResponse> reassignATMs(ATMReassignmentRequest request) {
//		
//		String cmUserId = loginService.getLoggedInUser();
//		// Step 1: Get all ATMs assigned to the leaving CE
//		List<ATMCEMapping> leavingCeATMs = getATMsForCE(cmUserId, request.getLeavingCeUserId());
//		log.info("Found {} ATMs to reassign", leavingCeATMs.size());
//
//		if (leavingCeATMs.isEmpty()) {
//			log.warn("No ATMs found for CE: {}", request.getLeavingCeUserId());
//			return new ArrayList<>();
//		}
//
//		// Step 2: Get all other CE locations (excluding the leaving CE)
//		List<CEUserLocation> availableCEs = getAvailableCEs(cmUserId, request.getLeavingCeUserId());
//		log.info("Found {} available CEs", availableCEs.size());
//
//		if (availableCEs.isEmpty()) {
//			throw new RuntimeException("No available CEs to reassign ATMs");
//		}
//
//		// Log the number of API calls that will be made
//		int totalApiCalls = leavingCeATMs.size() * availableCEs.size();
//		log.info("Will make {} individual routing API calls", totalApiCalls);
//
//		// Step 3: Calculate distance matrix using individual routing calls
//		log.info("Calculating distances using individual routing API calls...");
//		DistanceMatrix distanceMatrix = geoapifyService.calculateDistanceMatrix(leavingCeATMs, availableCEs);
//
//		if (distanceMatrix == null) {
//			log.error("Failed to calculate distance matrix");
//			return new ArrayList<>();
//		}
//
//		// Step 4: Find nearest CE for each ATM using the matrix
//		List<ATMReassignmentResponse> reassignments = new ArrayList<>();
//
//		for (int atmIndex = 0; atmIndex < leavingCeATMs.size(); atmIndex++) {
//			NearestCEResult nearest = geoapifyService.findNearestCE(atmIndex, distanceMatrix);
//
//			if (nearest != null) {
//				ATMReassignmentResponse response = new ATMReassignmentResponse(nearest.getAtmCode(),
//						request.getLeavingCeUserId(), nearest.getNearestCeUserId(), nearest.getDistance(),
//						nearest.getTravelTime());
//
//				reassignments.add(response);
//
//				// Here you would typically update the database with the new assignment
//				// updateATMAssignment(atm.getAtmCode(), bestAssignment.getNewCeUserId());
//
//				log.info("ATM {} reassigned from {} to {} (distance: {} km, time: {} min)", nearest.getAtmCode(),
//						request.getLeavingCeUserId(), nearest.getNearestCeUserId(),
//						String.format("%.2f", nearest.getDistance()), String.format("%.2f", nearest.getTravelTime()));
//			}
//		}
//
//		log.info("Successfully reassigned {} ATMs using individual routing API calls", reassignments.size());
//		return reassignments;
//	}
//
//	/**
//	 * Get ATMs assigned to a specific CE with null coordinate filtering
//	 */
//	private List<ATMCEMapping> getATMsForCE(String cmUserId, String ceUserId) {
//		List<Object[]> results = repository.getATMCEMappingDetails(cmUserId, ceUserId);
//
//		List<ATMCEMapping> validATMs = new ArrayList<>();
//		int skippedCount = 0;
//
//		for (Object[] row : results) {
//			String atmCode = (String) row[1];
//
//			// Check for null coordinates
//			if (row[2] == null || row[3] == null) {
//				log.warn("Skipping ATM {} - missing coordinates (lat: {}, long: {})", atmCode, row[2], row[3]);
//				skippedCount++;
//				continue;
//			}
//
//			try {
//				ATMCEMapping mapping = new ATMCEMapping((String) row[0], // ce_user_id
//						atmCode, // atm_code
//						((Number) row[2]).doubleValue(), // atm_lat
//						((Number) row[3]).doubleValue() // atm_long
//				);
//				validATMs.add(mapping);
//			} catch (ClassCastException | NullPointerException e) {
//				log.warn("Skipping ATM {} - invalid coordinate data: {}", atmCode, e.getMessage());
//				skippedCount++;
//			}
//		}
//
//		if (skippedCount > 0) {
//			log.warn("Skipped {} ATMs due to missing or invalid coordinate data", skippedCount);
//		}
//
//		return validATMs;
//	}
//
//	/**
//	 * Get available CEs with null coordinate filtering
//	 */
//	private List<CEUserLocation> getAvailableCEs(String cmUserId, String excludedCeUserId) {
//		List<Object[]> results = repository.getCEUserLocations(cmUserId, excludedCeUserId);
//
//		List<CEUserLocation> validCEs = new ArrayList<>();
//		int skippedCount = 0;
//
//		for (Object[] row : results) {
//			String ceUserId = (String) row[0];
//
//			// Check for null coordinates
//			if (row[1] == null || row[2] == null) {
//				log.warn("Skipping CE {} - missing coordinates (lat: {}, long: {})", ceUserId, row[1], row[2]);
//				skippedCount++;
//				continue;
//			}
//
//			try {
//				CEUserLocation location = new CEUserLocation(ceUserId, // ce_user_id
//						((Number) row[1]).doubleValue(), // user_lat
//						((Number) row[2]).doubleValue() // user_long
//				);
//				validCEs.add(location);
//			} catch (ClassCastException | NullPointerException e) {
//				log.warn("Skipping CE {} - invalid coordinate data: {}", ceUserId, e.getMessage());
//				skippedCount++;
//			}
//		}
//
//		if (skippedCount > 0) {
//			log.warn("Skipped {} CEs due to missing or invalid coordinate data", skippedCount);
//		}
//
//		return validCEs;
//	}
	
	/**
	 * Reassign ATMs from leaving CE to other CEs using individual Routing API calls
	 */
	
	
//	public List<ATMReassignmentResponse> reassignATMs(ATMReassignmentRequest request) {
//	    
//	    String cmUserId = loginService.getLoggedInUser();
//	    // Step 1: Get all ATMs assigned to the leaving CE
//	    List<ATMCEMapping> leavingCeATMs = getATMsForCE(cmUserId, request.getLeavingCeUserId());
//	    log.info("Found {} ATMs to reassign", leavingCeATMs.size());
//
//	    if (leavingCeATMs.isEmpty()) {
//	        log.warn("No ATMs found for CE: {}", request.getLeavingCeUserId());
//	        return new ArrayList<>();
//	    }
//
//	    // Step 2: Get all other CE locations (excluding the leaving CE)
//	    List<CEUserLocation> availableCEs = getAvailableCEs(cmUserId, request.getLeavingCeUserId());
//	    log.info("Found {} available CEs", availableCEs.size());
//
//	    if (availableCEs.isEmpty()) {
//	        throw new RuntimeException("No available CEs to reassign ATMs");
//	    }
//
//	    // Log the number of API calls that will be made
//	    int totalApiCalls = leavingCeATMs.size() * availableCEs.size();
//	    log.info("Will make {} individual routing API calls", totalApiCalls);
//
//	    // Step 3: Calculate distance matrix using individual routing calls
//	    log.info("Calculating distances using individual routing API calls...");
//	    DistanceMatrix distanceMatrix = geoapifyService.calculateDistanceMatrix(leavingCeATMs, availableCEs);
//
//	    if (distanceMatrix == null) {
//	        log.error("Failed to calculate distance matrix");
//	        return new ArrayList<>();
//	    }
//
//	    // Step 4: Find nearest CE for each ATM using the matrix
//	    List<ATMReassignmentResponse> reassignments = new ArrayList<>();
//
//	    for (int atmIndex = 0; atmIndex < leavingCeATMs.size(); atmIndex++) {
//	        NearestCEResult nearest = geoapifyService.findNearestCE(atmIndex, distanceMatrix);
//
//	        if (nearest != null) {
//	            // Get additional CE details for the new assigned CE
//	            CEUserLocation newCE = findCEById(availableCEs, nearest.getNearestCeUserId());
//	            
//	            // Create ATMReassignmentResponse with all required fields
//	            ATMReassignmentResponse response = new ATMReassignmentResponse();
//	            response.setAtmCode(nearest.getAtmCode());
//	            response.setOriginalCeUserId(request.getLeavingCeUserId());
//	            response.setNewCeUserId(nearest.getNearestCeUserId());
//	            response.setDistance(nearest.getDistance());
//	            response.setTravelTime(nearest.getTravelTime());
//	            
//	            // Set additional fields - get from CE details or set defaults
//	            if (newCE != null) {
//	                response.setEmployeeCode(newCE.getEmployeeCode() != null ? newCE.getEmployeeCode() : "N/A");
//	                response.setHomeAddress(newCE.getHomeAddress() != null ? newCE.getHomeAddress() : "Address Not Available");
//	            } else {
//	                response.setEmployeeCode("N/A");
//	                response.setHomeAddress("Address Not Available");
//	            }
//	            
//	            // Set ATM count - you might need to get this from repository or calculate
//	            response.setAtmCount(getATMCountForCE(cmUserId, nearest.getNearestCeUserId()));
//
//	            reassignments.add(response);
//
//	            // Here you would typically update the database with the new assignment
//	            // updateATMAssignment(atm.getAtmCode(), bestAssignment.getNewCeUserId());
//
//	            log.info("ATM {} reassigned from {} to {} (distance: {} km, time: {} min)", nearest.getAtmCode(),
//	                    request.getLeavingCeUserId(), nearest.getNearestCeUserId(),
//	                    String.format("%.2f", nearest.getDistance()), String.format("%.2f", nearest.getTravelTime()));
//	        }
//	    }
//
//	    log.info("Successfully reassigned {} ATMs using individual routing API calls", reassignments.size());
//	    return reassignments;
//	}
//
//	/**
//	 * Helper method to find CE by ID from the available CEs list
//	 */
//	private CEUserLocation findCEById(List<CEUserLocation> availableCEs, String ceUserId) {
//	    return availableCEs.stream()
//	            .filter(ce -> ce.getCeUserId().equals(ceUserId))
//	            .findFirst()
//	            .orElse(null);
//	}
//
//	/**
//	 * Get ATM count for a specific CE
//	 */
//	private Integer getATMCountForCE(String cmUserId, String ceUserId) {
//	    try {
//	        List<ATMCEMapping> atmList = getATMsForCE(cmUserId, ceUserId);
//	        return atmList.size();
//	    } catch (Exception e) {
//	        log.warn("Failed to get ATM count for CE {}: {}", ceUserId, e.getMessage());
//	        return 0; // Return 0 if unable to fetch count
//	    }
//	}
//
//	/**
//	 * Get ATMs assigned to a specific CE with null coordinate filtering
//	 */
//	private List<ATMCEMapping> getATMsForCE(String cmUserId, String ceUserId) {
//	    List<Object[]> results = repository.getATMCEMappingDetails(cmUserId, ceUserId);
//
//	    List<ATMCEMapping> validATMs = new ArrayList<>();
//	    int skippedCount = 0;
//
//	    for (Object[] row : results) {
//	        String atmCode = (String) row[1];
//
//	        // Check for null coordinates
//	        if (row[2] == null || row[3] == null) {
//	            log.warn("Skipping ATM {} - missing coordinates (lat: {}, long: {})", atmCode, row[2], row[3]);
//	            skippedCount++;
//	            continue;
//	        }
//
//	        try {
//	            ATMCEMapping mapping = new ATMCEMapping((String) row[0], // ce_user_id
//	                    atmCode, // atm_code
//	                    ((Number) row[2]).doubleValue(), // atm_lat
//	                    ((Number) row[3]).doubleValue() // atm_long
//	            );
//	            validATMs.add(mapping);
//	        } catch (ClassCastException | NullPointerException e) {
//	            log.warn("Skipping ATM {} - invalid coordinate data: {}", atmCode, e.getMessage());
//	            skippedCount++;
//	        }
//	    }
//
//	    if (skippedCount > 0) {
//	        log.warn("Skipped {} ATMs due to missing or invalid coordinate data", skippedCount);
//	    }
//
//	    return validATMs;
//	}
//
//	/**
//	 * Get available CEs with null coordinate filtering - Enhanced to fetch additional details
//	 */
//	private List<CEUserLocation> getAvailableCEs(String cmUserId, String excludedCeUserId) {
//	    // You might need to modify this query to fetch additional fields like employee_code, home_address
//	    List<Object[]> results = repository.getCEUserLocations(cmUserId, excludedCeUserId);
//
//	    List<CEUserLocation> validCEs = new ArrayList<>();
//	    int skippedCount = 0;
//
//	    for (Object[] row : results) {
//	        String ceUserId = (String) row[0];
//
//	        // Check for null coordinates
//	        if (row[1] == null || row[2] == null) {
//	            log.warn("Skipping CE {} - missing coordinates (lat: {}, long: {})", ceUserId, row[1], row[2]);
//	            skippedCount++;
//	            continue;
//	        }
//
//	        try {
//	            // Create CEUserLocation with additional details
//	            CEUserLocation location = new CEUserLocation();
//	            location.setCeUserId(ceUserId);
//	            location.setUserLat(((Number) row[1]).doubleValue());
//	            location.setUserLong(((Number) row[2]).doubleValue());
//	            
//	            // Set additional fields if available in query results
//	            if (row.length > 3) {
//	                location.setEmployeeCode(row[3] != null ? (String) row[3] : null);
//	            }
//	            if (row.length > 4) {
//	                location.setHomeAddress(row[4] != null ? (String) row[4] : null);
//	            }
//	            
//	            validCEs.add(location);
//	        } catch (ClassCastException | NullPointerException e) {
//	            log.warn("Skipping CE {} - invalid coordinate data: {}", ceUserId, e.getMessage());
//	            skippedCount++;
//	        }
//	    }
//
//	    if (skippedCount > 0) {
//	        log.warn("Skipped {} CEs due to missing or invalid coordinate data", skippedCount);
//	    }
//
//	    return validCEs;
//	}
	
	
	
	
	
	
	
	
	//
	
	/**
     * Reassign ATMs from leaving CE to other CEs and save only required fields to database
     */
    
    public List<ATMReassignmentResponse> reassignATMs(ATMReassignmentRequest request) {
        
        String cmUserId = loginService.getLoggedInUser();
        // Step 1: Get all ATMs assigned to the leaving CE
        List<ATMCEMapping> leavingCeATMs = getATMsForCE(cmUserId, request.getLeavingCeUserId());
        log.info("Found {} ATMs to reassign", leavingCeATMs.size());

        if (leavingCeATMs.isEmpty()) {
            log.warn("No ATMs found for CE: {}", request.getLeavingCeUserId());
            return new ArrayList<>();
        }

        // Step 2: Get all other CE locations (excluding the leaving CE)
        List<CEUserLocation> availableCEs = getAvailableCEs(cmUserId, request.getLeavingCeUserId());
        log.info("Found {} available CEs", availableCEs.size());

        if (availableCEs.isEmpty()) {
            throw new RuntimeException("No available CEs to reassign ATMs");
        }

        // Step 3: Calculate distance matrix using individual routing calls
        log.info("Calculating distances using individual routing API calls...");
        DistanceMatrix distanceMatrix = geoapifyService.calculateDistanceMatrix(leavingCeATMs, availableCEs);

        if (distanceMatrix == null) {
            log.error("Failed to calculate distance matrix");
            return new ArrayList<>();
        }

        // Step 4: Find nearest CE for each ATM and create response + simplified history record
        List<ATMReassignmentResponse> reassignments = new ArrayList<>();
        List<ATMReassignmentHistory> historyRecords = new ArrayList<>();

        for (int atmIndex = 0; atmIndex < leavingCeATMs.size(); atmIndex++) {
            NearestCEResult nearest = geoapifyService.findNearestCE(atmIndex, distanceMatrix);

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

                log.info("ATM {} reassigned from {} to {} (distance: {} km, time: {} min)", nearest.getAtmCode(),
                        request.getLeavingCeUserId(), nearest.getNearestCeUserId(),
                        String.format("%.2f", nearest.getDistance()), String.format("%.2f", nearest.getTravelTime()));
            }
        }

        // Step 5: Save only the essential data to database
        try {
            List<ATMReassignmentHistory> savedRecords = reassignmentHistoryRepository.saveAll(historyRecords);
            log.info("Successfully saved {} ATM reassignment records with essential data to database", savedRecords.size());
        } catch (Exception e) {
            log.error("Failed to save reassignment history to database: {}", e.getMessage());
            // Continue execution even if database save fails
        }

        log.info("Successfully reassigned {} ATMs using individual routing API calls", reassignments.size());
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