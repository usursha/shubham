package com.hpy.mappingservice.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.hpy.mappingservice.config.GeoapifyConfig;
import com.hpy.mappingservice.response.dto.ATMCEMapping;
import com.hpy.mappingservice.response.dto.CEUserLocation;
import com.hpy.mappingservice.response.dto.GeoapifyRouteResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeoapifyService {

	@Autowired
	private GeoapifyConfig geoapifyConfig;
	//@Autowired
	 private final WebClient webClient;

	 /**
	     * Calculate distance between two points using individual Routing API
	     */
	    public DistanceResult calculateDistance(Double fromLat, Double fromLon, Double toLat, Double toLon) {
	        log.debug("Calculating distance from ({}, {}) to ({}, {})", fromLat, fromLon, toLat, toLon);
	        
	        try {
	            // Build waypoints for individual routing API
	            String waypoints = String.format("%f,%f|%f,%f", fromLat, fromLon, toLat, toLon);
	            
	            // Individual Routing API endpoint
	            String url = String.format("https://api.geoapify.com/v1/routing?waypoints=%s&mode=drive&apiKey=%s",
	                    waypoints, geoapifyConfig.getApiKey());
	            
	            log.debug("Routing API URL: {}", url.replaceAll("apiKey=[^&]*", "apiKey=***"));
	            
	            GeoapifyRouteResponse response = webClient.get()
	                    .uri(url)
	                    .retrieve()
	                    .bodyToMono(GeoapifyRouteResponse.class)
	                    .timeout(Duration.ofSeconds(10))
	                    .block();

	            if (response == null || response.getFeatures() == null || response.getFeatures().isEmpty()) {
	                log.warn("Invalid response from Routing API, falling back to calculation");
	                return calculateFallbackDistance(fromLat, fromLon, toLat, toLon);
	            }
	            
	            GeoapifyRouteResponse.Properties props = response.getFeatures().get(0).getProperties();
	            
	            if (props == null || props.getDistance() == null || props.getTime() == null) {
	                log.warn("Invalid properties in response, falling back to calculation");
	                return calculateFallbackDistance(fromLat, fromLon, toLat, toLon);
	            }
	            
	            double distanceKm = props.getDistance() / 1000.0; // Convert to km
	            double timeMinutes = props.getTime() / 60.0;     // Convert to minutes
	            
	            log.debug("API result - Distance: {} km, Time: {} minutes", distanceKm, timeMinutes);
	            
	            return new DistanceResult(distanceKm, timeMinutes);
	            
	        } catch (Exception e) {
	            log.warn("Routing API failed: {}, using fallback calculation", e.getMessage());
	            return calculateFallbackDistance(fromLat, fromLon, toLat, toLon);
	        }
	    }
	    
	    /**
	     * Calculate distances for all ATM-CE combinations
	     * Uses parallel processing to speed up multiple API calls
	     */
	    public DistanceMatrix calculateDistanceMatrix(List<ATMCEMapping> atms, List<CEUserLocation> ces) {
	        log.info("Calculating distance matrix for {} ATMs and {} CEs using individual routing calls", 
	                atms.size(), ces.size());
	        
	        int totalCalls = atms.size() * ces.size();
	        log.info("This will require {} individual API calls", totalCalls);
	        
	        double[][] distances = new double[ces.size()][atms.size()];
	        double[][] times = new double[ces.size()][atms.size()];
	        
	        // Use parallel processing to speed up API calls
	        ExecutorService executor = Executors.newFixedThreadPool(5); // Limit concurrent calls
	        List<CompletableFuture<Void>> futures = new ArrayList<>();
	        
	        for (int ceIndex = 0; ceIndex < ces.size(); ceIndex++) {
	            final int finalCeIndex = ceIndex;
	            CEUserLocation ce = ces.get(ceIndex);
	            
	            for (int atmIndex = 0; atmIndex < atms.size(); atmIndex++) {
	                final int finalAtmIndex = atmIndex;
	                ATMCEMapping atm = atms.get(atmIndex);
	                
	                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
	                    try {
	                        // Add small delay to avoid rate limiting
	                        Thread.sleep(25); // 25ms delay - faster processing
	                        
	                        DistanceResult result = calculateDistance(
	                            ce.getUserLat(), ce.getUserLong(),
	                            atm.getAtmLat(), atm.getAtmLong()
	                        );
	                        
	                        synchronized (distances) {
	                            distances[finalCeIndex][finalAtmIndex] = result.getDistance() * 1000; // Convert to meters
	                            times[finalCeIndex][finalAtmIndex] = result.getTravelTime() * 60;     // Convert to seconds
	                        }
	                        
	                    } catch (Exception e) {
	                        log.error("Error calculating distance for CE {} to ATM {}: {}", 
	                                ce.getCeUserId(), atm.getAtmCode(), e.getMessage());
	                        
	                        // Use fallback calculation
	                        DistanceResult fallback = calculateFallbackDistance(
	                            ce.getUserLat(), ce.getUserLong(),
	                            atm.getAtmLat(), atm.getAtmLong()
	                        );
	                        
	                        synchronized (distances) {
	                            distances[finalCeIndex][finalAtmIndex] = fallback.getDistance() * 1000;
	                            times[finalCeIndex][finalAtmIndex] = fallback.getTravelTime() * 60;
	                        }
	                    }
	                }, executor);
	                
	                futures.add(future);
	            }
	        }
	        
	        // Wait for all API calls to complete
	        try {
	            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
	            log.info("*** SUCCESS: Completed {} individual routing API calls ***", totalCalls);
	        } catch (Exception e) {
	            log.error("Error waiting for API calls to complete: {}", e.getMessage());
	        } finally {
	            executor.shutdown();
	        }
	        
	        return new DistanceMatrix(distances, times, ces, atms);
	    }
	    
	    /**
	     * Find the nearest CE for a specific ATM using the distance matrix
	     */
	    public NearestCEResult findNearestCE(int atmIndex, DistanceMatrix matrix) {
	        if (matrix == null || matrix.getDistances() == null) {
	            return null;
	        }
	        
	        double[][] distances = matrix.getDistances();
	        int nearestCEIndex = 0;
	        double minDistance = Double.MAX_VALUE;
	        double minTime = 0;
	        
	        // Check all CEs (sources) for this ATM (target)
	        for (int ceIndex = 0; ceIndex < distances.length; ceIndex++) {
	            if (distances[ceIndex][atmIndex] < minDistance) {
	                minDistance = distances[ceIndex][atmIndex];
	                minTime = matrix.getTimes()[ceIndex][atmIndex];
	                nearestCEIndex = ceIndex;
	            }
	        }
	        
	        CEUserLocation nearestCE = matrix.getCes().get(nearestCEIndex);
	        ATMCEMapping atm = matrix.getAtms().get(atmIndex);
	        
	        return new NearestCEResult(
	            atm.getAtmCode(),
	            nearestCE.getCeUserId(),
	            minDistance / 1000.0,  // Convert to km
	            minTime / 60.0         // Convert to minutes
	        );
	    }
	    
	    /**
	     * Fallback calculation when API fails
	     */
	    private DistanceResult calculateFallbackDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
	        // Standard Haversine calculation
	        final int R = 6371; // Radius of Earth in kilometers
	        
	        double latDistance = Math.toRadians(lat2 - lat1);
	        double lonDistance = Math.toRadians(lon2 - lon1);
	        
	        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) 
	                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) 
	                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	        
	        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	        double straightLineDistance = R * c;
	        
	        // Apply Mumbai road multiplier (based on your 22.95/16.47 test result)
	        double roadMultiplier = 1.39;
	        double actualRoadDistance = straightLineDistance * roadMultiplier;
	        
	        // Mumbai traffic speed (average)
	        double averageSpeed = 25; // km/h
	        double travelTime = (actualRoadDistance / averageSpeed) * 60; // minutes
	        
	        return new DistanceResult(actualRoadDistance, travelTime);
	    }

	    @Data
	    @AllArgsConstructor
	    public static class DistanceResult {
	        private Double distance; // in kilometers
	        private Double travelTime; // in minutes
	    }

	    @Data
	    @AllArgsConstructor
	    public static class DistanceMatrix {
	        private double[][] distances;  // distances[ceIndex][atmIndex] in meters
	        private double[][] times;      // times[ceIndex][atmIndex] in seconds
	        private List<CEUserLocation> ces;
	        private List<ATMCEMapping> atms;
	        
	        // Constructor for individual API calls
	       
	    }
	    
	    @Data
	    @AllArgsConstructor
	    public static class NearestCEResult {
	        private String atmCode;
	        private String nearestCeUserId;
	        private Double distance;   // in km
	        private Double travelTime; // in minutes
	    }
	 
	 
	 
	 
	 
	 
	 
	 /**
	     * Calculate distance between two specific points using individual Routing API
	     * SIMPLIFIED - No parallel processing, no matrix calculations
	     */
//	    public DistanceResult calculateDistance(Double fromLat, Double fromLon, Double toLat, Double toLon) {
//	        log.debug("Calculating distance from ({}, {}) to ({}, {})", fromLat, fromLon, toLat, toLon);
//	        
//	        try {
//	            // Build waypoints for individual routing API
//	            String waypoints = String.format("%f,%f|%f,%f", fromLat, fromLon, toLat, toLon);
//	            
//	            // Individual Routing API endpoint
//	            String url = String.format("https://api.geoapify.com/v1/routing?waypoints=%s&mode=drive&apiKey=%s",
//	                    waypoints, geoapifyConfig.getApiKey());
//	            
//	            log.debug("Routing API URL: {}", url.replaceAll("apiKey=[^&]*", "apiKey=***"));
//	            
//	            GeoapifyRouteResponse response = webClient.get()
//	                    .uri(url)
//	                    .retrieve()
//	                    .bodyToMono(GeoapifyRouteResponse.class)
//	                    .timeout(Duration.ofSeconds(5))  // Quick timeout for fast fallback
//	                    .block();
//
//	            if (response == null || response.getFeatures() == null || response.getFeatures().isEmpty()) {
//	                log.debug("Invalid API response, using fallback calculation");
//	                return calculateFallbackDistance(fromLat, fromLon, toLat, toLon);
//	            }
//	            
//	            GeoapifyRouteResponse.Properties props = response.getFeatures().get(0).getProperties();
//	            
//	            if (props == null || props.getDistance() == null || props.getTime() == null) {
//	                log.debug("Invalid properties in response, using fallback calculation");
//	                return calculateFallbackDistance(fromLat, fromLon, toLat, toLon);
//	            }
//	            
//	            double distanceKm = props.getDistance() / 1000.0; // Convert to km
//	            double timeMinutes = props.getTime() / 60.0;     // Convert to minutes
//	            
//	            log.debug("API SUCCESS - Distance: {} km, Time: {} minutes", 
//	                     String.format("%.2f", distanceKm), String.format("%.2f", timeMinutes));
//	            
//	            return new DistanceResult(distanceKm, timeMinutes);
//	            
//	        } catch (Exception e) {
//	            log.debug("API failed ({}), using fallback calculation", e.getMessage());
//	            return calculateFallbackDistance(fromLat, fromLon, toLat, toLon);
//	        }
//	    }
//	    
//	    /**
//	     * Enhanced fallback calculation based on Mumbai road patterns
//	     */
//	    private DistanceResult calculateFallbackDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
//	        // Standard Haversine calculation
//	        final int R = 6371; // Radius of Earth in kilometers
//	        
//	        double latDistance = Math.toRadians(lat2 - lat1);
//	        double lonDistance = Math.toRadians(lon2 - lon1);
//	        
//	        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) 
//	                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) 
//	                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//	        
//	        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//	        double straightLineDistance = R * c;
//	        
//	        // Apply Mumbai road multiplier (based on your 22.95/16.47 test result)
//	        double roadMultiplier = calculateMumbaiRoadMultiplier(straightLineDistance);
//	        double actualRoadDistance = straightLineDistance * roadMultiplier;
//	        
//	        // Mumbai traffic speed
//	        double averageSpeed = calculateMumbaiSpeed(straightLineDistance);
//	        double travelTime = (actualRoadDistance / averageSpeed) * 60; // minutes
//	        
//	        log.debug("FALLBACK - Distance: {} km, Time: {} minutes", 
//	                 String.format("%.2f", actualRoadDistance), String.format("%.2f", travelTime));
//	        
//	        return new DistanceResult(actualRoadDistance, travelTime);
//	    }
//	    
//	    /**
//	     * Mumbai road distance multiplier
//	     */
//	    private double calculateMumbaiRoadMultiplier(double straightLineDistance) {
//	        if (straightLineDistance < 3) {
//	            return 1.25; // Short city routes: 25% longer
//	        } else if (straightLineDistance < 10) {
//	            return 1.35; // Medium routes: 35% longer
//	        } else if (straightLineDistance < 25) {
//	            return 1.40; // Your test case range: 40% longer (matches API)
//	        } else {
//	            return 1.50; // Long routes with highway: 50% longer
//	        }
//	    }
//	    
//	    /**
//	     * Mumbai traffic-adjusted speeds
//	     */
//	    private double calculateMumbaiSpeed(double distance) {
//	        if (distance < 3) {
//	            return 18; // Dense city center: 18 km/h
//	        } else if (distance < 10) {
//	            return 22; // City routes: 22 km/h  
//	        } else if (distance < 25) {
//	            return 28; // Mixed city/highway: 28 km/h
//	        } else {
//	            return 35; // Mostly highway routes: 35 km/h
//	        }
//	    }
//
//	    @Data
//	    @AllArgsConstructor
//	    public static class DistanceResult {
//	        private Double distance; // in kilometers
//	        private Double travelTime; // in minutes
//	    }
}