package com.hpy.ops360.location.service;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hpy.ops360.location.dto.UserdistLocationDto;
import com.hpy.ops360.location.dto.UsertotaldistLocationDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DistanceListCalculationService {
	
	@Value("${osrm.api.url}") 
    private String osrmApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final int MAX_POINTS = 450; // OSRM API limit

    public UsertotaldistLocationDto getTotalDistance(List<UserdistLocationDto> locationList) {
        log.info("Starting distance calculation for {} points", locationList != null ? locationList.size() : 0);

        UsertotaldistLocationDto response = new UsertotaldistLocationDto();

        if (locationList == null || locationList.size() < 2) {
            log.warn("Insufficient data points. At least 2 points are required.");
            response.setTotalDistance("0.000 KM");
            response.setTotalTime("0 hrs 0 mins");
            return response;
        }

        try {
            double totalDistance = 0.0;
            double totalTime = 0.0;

            // Split the location list into batches of size <= 500
            for (int i = 0; i < locationList.size(); i += MAX_POINTS) {
                int endIndex = Math.min(i + MAX_POINTS, locationList.size());
                List<UserdistLocationDto> batch = locationList.subList(i, endIndex);

                log.info("Processing batch {} - {} (total: {})", i + 1, endIndex, locationList.size());

                // Calculate distance for each batch
                UsertotaldistLocationDto batchResult = calculateBatchDistance(batch);
                totalDistance += parseDistance(batchResult.getTotalDistance());
                totalTime += parseTime(batchResult.getTotalTime());
            }

            // Prepare the final response
            response.setTotalDistance(String.format("%.3f KM", totalDistance));
            int hours = (int) (totalTime / 60);
            int minutes = (int) (totalTime % 60);
            response.setTotalTime(String.format("%d hrs %d mins", hours, minutes));

            log.info("Total calculated distance: {} KM, Total time: {}", totalDistance, response.getTotalTime());
            return response;

        } catch (Exception e) {
            log.error("Error calculating total distance: {}", e.getMessage());
            response.setTotalDistance("0.000 KM");
            response.setTotalTime("0 hrs 0 mins");
            return response;
        }
    }

    private UsertotaldistLocationDto calculateBatchDistance(List<UserdistLocationDto> batch) {
        UsertotaldistLocationDto response = new UsertotaldistLocationDto();
        try {
            String waypoints = batch.stream()
                    .map(point -> String.format("%f,%f", point.getLongitude(), point.getLatitude()))
                    .collect(Collectors.joining(";"));

         // Construct URL from properties
            String url = String.format("%s%s", osrmApiUrl, waypoints);
            log.info("Requesting OSRM API with URL: {}", url);

            // OSRM API Call
            String apiResponse = restTemplate.getForObject(url, String.class);
            JSONObject json = new JSONObject(apiResponse);

            // Extract data
            double distanceInMeters = json.getJSONArray("routes")
                                          .getJSONObject(0)
                                          .getDouble("distance");

            double durationInSeconds = json.getJSONArray("routes")
                                           .getJSONObject(0)
                                           .getDouble("duration");

            double distanceInKm = distanceInMeters / 1000.0;
            String formattedDistance = String.format("%.3f KM", distanceInKm);

            int minutes = (int) (durationInSeconds / 60);
            String formattedTime = String.format("%d mins", minutes);

            log.info("Batch distance: {}, time: {}", formattedDistance, formattedTime);
            response.setTotalDistance(formattedDistance);
            response.setTotalTime(formattedTime);
            return response;

        } catch (Exception e) {
            log.error("Error processing batch: {}", e.getMessage());
            response.setTotalDistance("0.000 KM");
            response.setTotalTime("0 mins");
            return response;
        }
    }

    private double parseDistance(String distance) {
        try {
            return Double.parseDouble(distance.split(" ")[0]);
        } catch (Exception e) {
            log.error("Error parsing distance: {}", e.getMessage());
            return 0.0;
        }
    }

    private double parseTime(String time) {
        try {
            String[] parts = time.split(" ");

            int hours = 0;
            int minutes = 0;

            // Check if hours are present
            if (parts.length > 2 && parts[1].equals("hrs")) {
                hours = Integer.parseInt(parts[0]); // Extract hours
                minutes = Integer.parseInt(parts[2]); // Extract minutes
            } else if (parts[1].equals("mins")) { // Only minutes present
                minutes = Integer.parseInt(parts[0]);
            }

            // Convert total time to minutes
            return hours * 60 + minutes;

        } catch (Exception e) {
            log.error("Error parsing time '{}': {}", time, e.getMessage());
            return 0.0; // Default to 0 if parsing fails
        }
    }

}

