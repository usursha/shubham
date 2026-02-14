package com.MapPUC.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.MapPUC.dto.UserdistLocationDto2;
import com.MapPUC.dto.UsertotaldistLocationDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class DistanceCalculationService {
	private static final String OSRM_API_URL = "http://localhost:5000/route/v1/driving/";
//    private static final String OSRM_API_URL = "http://localhost:5001/route/v1/cycling/";
    private final RestTemplate restTemplate = new RestTemplate();

    public UsertotaldistLocationDto getTotalDistanceAndTime() {
        log.info("Starting real-world distance and time calculation for hardcoded locations.");

        // Hardcoded locations: In Mumbai Only
        List<UserdistLocationDto2> locations = Arrays.asList(
            new UserdistLocationDto2(1, 19.197423521468444, 72.94619379049836),
            new UserdistLocationDto2(2, 19.2116080147379, 72.95988378542887) 
        );

        String coordinates = buildCoordinatesString(locations);
        String url = OSRM_API_URL + coordinates;

        log.info("Requesting OSRM API with URL: {}", url);

        UsertotaldistLocationDto response = new UsertotaldistLocationDto();

        try {
            ResponseEntity<String> apiResponse = restTemplate.getForEntity(url, String.class);
            String responseBody = apiResponse.getBody();

            double distanceInKm = parseDistance(responseBody);
            String totalTime = parseTotalTime(responseBody);

            response.setTotalDistance(String.format("%.3f KM", distanceInKm));
            response.setTotalTime(totalTime);

            log.info("Real-world total distance: {} KM, Total time: {}", 
                      String.format("%.3f", distanceInKm), totalTime);

        } catch (Exception e) {
            log.error("Error calling OSRM API: {}", e.getMessage());
            response.setTotalDistance("0.000 KM");
            response.setTotalTime("0 hrs 0 mins");
        }

        return response;
    }

    private String buildCoordinatesString(List<UserdistLocationDto2> locations) {
        StringBuilder coordinates = new StringBuilder();
        for (UserdistLocationDto2 loc : locations) {
            coordinates.append(loc.getLongitude()).append(",").append(loc.getLatitude()).append(";");
        }
        // Remove the trailing semicolon
        coordinates.deleteCharAt(coordinates.length() - 1);
        return coordinates.toString();
    }

    private double parseDistance(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Extract distance (in meters) from the first route
            double distanceInMeters = rootNode.path("routes").get(0).path("distance").asDouble();

            // Convert meters to kilometers
            return distanceInMeters / 1000.0;
        } catch (Exception e) {
            log.error("Error parsing distance from OSRM response: {}", e.getMessage());
            return 0.0;
        }
    }

    private String parseTotalTime(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Extract duration (in seconds) from the first route
            double durationInSeconds = rootNode.path("routes").get(0).path("duration").asDouble();

            // Convert seconds into hours and minutes
            int hours = (int) (durationInSeconds / 3600);
            int minutes = (int) ((durationInSeconds % 3600) / 60);

            return String.format("%d hrs %d mins", hours, minutes);
        } catch (Exception e) {
            log.error("Error parsing time from OSRM response: {}", e.getMessage());
            return "0 hrs 0 mins";
        }
    }
}






//package com.MapPUC.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import com.MapPUC.dto.UserdistLocationDto2;
//import com.MapPUC.dto.UsertotaldistLocationDto;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Service
//@Slf4j
//public class DistanceCalculationService {
//	
////    private static final String OSRM_API_URL = "http://router.project-osrm.org/route/v1/driving/";
//	private static final String OSRM_API_URL = "http://localhost:5000/route/v1/cycling/";
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public UsertotaldistLocationDto getTotalDistance() {
//        log.info("Starting real-world distance calculation for hardcoded locations.");
//
//        // Hardcoded locations: Bangalore and Mysore
//        List<UserdistLocationDto2> locations = Arrays.asList(
//            new UserdistLocationDto2(1, 19.1974829, 72.9475901),  
//            new UserdistLocationDto2(2, 19.064097, 72.866725)  
//        );
//        
//                      
//
//        String coordinates = buildCoordinatesString(locations);
//        String url = OSRM_API_URL + coordinates;
//
//        log.info("Requesting OSRM API with URL: {}", url);
//
//        UsertotaldistLocationDto response = new UsertotaldistLocationDto();
//
//        try {
//            ResponseEntity<String> apiResponse = restTemplate.getForEntity(url, String.class);
//            String responseBody = apiResponse.getBody();
//
//            double distanceInKm = parseDistance(responseBody);
//            response.setTotalDistance(String.format("%.3f KM", distanceInKm));
//
//            log.info("Real-world total distance calculated: {} KM", String.format("%.3f", distanceInKm));
//
//        } catch (Exception e) {
//            log.error("Error calling OSRM API: {}", e.getMessage());
//            response.setTotalDistance("0.000 KM");
//        }
//
//        return response;
//    }
//
//    private String buildCoordinatesString(List<UserdistLocationDto2> locations) {
//        StringBuilder coordinates = new StringBuilder();
//        for (UserdistLocationDto2 loc : locations) {
//            coordinates.append(loc.getLongitude()).append(",").append(loc.getLatitude()).append(";");
//        }
//        // Remove the trailing semicolon
//        coordinates.deleteCharAt(coordinates.length() - 1);
//        return coordinates.toString();
//    }
//
//    private double parseDistance(String responseBody) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode rootNode = objectMapper.readTree(responseBody);
//
//            // Extract distance (in meters) from the first route
//            double distanceInMeters = rootNode.path("routes").get(0).path("distance").asDouble();
//
//            // Convert meters to kilometers
//            return distanceInMeters / 1000.0;
//        } catch (Exception e) {
//            log.error("Error parsing distance from OSRM response: {}", e.getMessage());
//            return 0.0;
//        }
//    }
//}
//
//
