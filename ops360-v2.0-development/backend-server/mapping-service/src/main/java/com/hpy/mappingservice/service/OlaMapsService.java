// 2. OlaMapsService.java - Service for OLA Maps integration
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

import com.hpy.mappingservice.config.OlaMapsConfig;
import com.hpy.mappingservice.response.dto.ATMCEMapping;
import com.hpy.mappingservice.response.dto.CEUserLocation;
import com.hpy.mappingservice.response.dto.OlaMapsRouteResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OlaMapsService {

    @Autowired
    private OlaMapsConfig olaMapsConfig;
    
    private final WebClient webClient;

    /**
     * Calculate distance between two points using OLA Maps Directions API
     */
    public DistanceResult calculateDistance(Double fromLat, Double fromLon, Double toLat, Double toLon) {
        log.debug("Calculating distance from ({}, {}) to ({}, {}) using OLA Maps", fromLat, fromLon, toLat, toLon);
        
        try {
            // Build OLA Maps Directions API URL
            String url = String.format("%s%s?origin=%f,%f&destination=%f,%f&alternatives=false&steps=false&api_key=%s",
                    olaMapsConfig.getBaseUrl(),
                    olaMapsConfig.getRoutingUrl(),
                    fromLat, fromLon, 
                    toLat, toLon,
                    olaMapsConfig.getApiKey());
            
            log.debug("OLA Maps Directions API URL: {}", url.replaceAll("api_key=[^&]*", "api_key=***"));
            
            OlaMapsRouteResponse response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(OlaMapsRouteResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            if (response == null || response.getRoutes() == null || response.getRoutes().isEmpty()) {
                log.warn("Invalid response from OLA Maps Directions API, falling back to calculation");
                return calculateFallbackDistance(fromLat, fromLon, toLat, toLon);
            }
            
            OlaMapsRouteResponse.Route route = response.getRoutes().get(0);
            
            if (route == null || route.getOverview() == null) {
                log.warn("Invalid route in response, falling back to calculation");
                return calculateFallbackDistance(fromLat, fromLon, toLat, toLon);
            }
            
            double distanceKm = route.getOverview().getDistance() / 1000.0; // Convert to km
            double timeMinutes = route.getOverview().getDuration() / 60.0;     // Convert to minutes
            
            log.debug("OLA Maps API result - Distance: {} km, Time: {} minutes", distanceKm, timeMinutes);
            
            return new DistanceResult(distanceKm, timeMinutes);
            
        } catch (Exception e) {
            log.warn("OLA Maps Directions API failed: {}, using fallback calculation", e.getMessage());
            return calculateFallbackDistance(fromLat, fromLon, toLat, toLon);
        }
    }
    
    /**
     * Calculate distances for all ATM-CE combinations using OLA Maps Distance Matrix API
     * Falls back to individual calls if matrix API is not available or fails
     */
    public DistanceMatrix calculateDistanceMatrix(List<ATMCEMapping> atms, List<CEUserLocation> ces) {
        log.info("Calculating distance matrix for {} ATMs and {} CEs using OLA Maps", 
                atms.size(), ces.size());
        
        // First try to use Distance Matrix API for batch processing
        try {
            return calculateDistanceMatrixBatch(atms, ces);
        } catch (Exception e) {
            log.warn("Distance Matrix API failed: {}, falling back to individual calls", e.getMessage());
            return calculateDistanceMatrixIndividual(atms, ces);
        }
    }
    
    /**
     * Calculate distance matrix using OLA Maps Distance Matrix API (batch processing)
     */
    private DistanceMatrix calculateDistanceMatrixBatch(List<ATMCEMapping> atms, List<CEUserLocation> ces) {
        log.info("Attempting batch distance matrix calculation using OLA Maps Distance Matrix API");
        
        // Prepare origins (CEs) and destinations (ATMs)
        String origins = ces.stream()
                .map(ce -> String.format("%f,%f", ce.getUserLat(), ce.getUserLong()))
                .reduce((a, b) -> a + "|" + b)
                .orElse("");
                
        String destinations = atms.stream()
                .map(atm -> String.format("%f,%f", atm.getAtmLat(), atm.getAtmLong()))
                .reduce((a, b) -> a + "|" + b)
                .orElse("");
        
        String url = String.format("%s%s?origins=%s&destinations=%s&api_key=%s",
                olaMapsConfig.getBaseUrl(),
                olaMapsConfig.getDistanceMatrixUrl(),
                origins,
                destinations,
                olaMapsConfig.getApiKey());
        
        log.debug("OLA Maps Distance Matrix API URL: {}", url.replaceAll("api_key=[^&]*", "api_key=***"));
        
        // Note: Implement OlaMapsDistanceMatrixResponse class based on OLA Maps API documentation
        // This is a placeholder - you'll need to create the response class based on actual API response structure
        throw new RuntimeException("Distance Matrix API implementation pending - falling back to individual calls");
    }
    
    /**
     * Calculate distance matrix using individual OLA Maps API calls
     * Uses parallel processing to speed up multiple API calls
     */
    private DistanceMatrix calculateDistanceMatrixIndividual(List<ATMCEMapping> atms, List<CEUserLocation> ces) {
        log.info("Calculating distance matrix using individual OLA Maps routing calls for {} ATMs and {} CEs", 
                atms.size(), ces.size());
        
        int totalCalls = atms.size() * ces.size();
        log.info("This will require {} individual OLA Maps API calls", totalCalls);
        
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
                        log.error("Error calculating distance for CE {} to ATM {} using OLA Maps: {}", 
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
            log.info("*** SUCCESS: Completed {} individual OLA Maps routing API calls ***", totalCalls);
        } catch (Exception e) {
            log.error("Error waiting for OLA Maps API calls to complete: {}", e.getMessage());
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
     * Fallback calculation when OLA Maps API fails
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
    }
    
    @Data
    @AllArgsConstructor
    public static class NearestCEResult {
        private String atmCode;
        private String nearestCeUserId;
        private Double distance;   // in km
        private Double travelTime; // in minutes
    }
}