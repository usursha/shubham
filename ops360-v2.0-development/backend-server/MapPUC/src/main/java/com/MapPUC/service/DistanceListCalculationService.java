package com.MapPUC.service;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.MapPUC.dto.UserdistLocationDto;
import com.MapPUC.dto.UsertotaldistLocationDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DistanceListCalculationService {

    private final RestTemplate restTemplate = new RestTemplate();

    public UsertotaldistLocationDto getTotalDistance(List<UserdistLocationDto> locationList) {
        log.info("Starting real-world total distance and time calculation for location updates. Number of points received: {}", 
                 locationList != null ? locationList.size() : 0);

        UsertotaldistLocationDto response = new UsertotaldistLocationDto();

        if (locationList == null || locationList.size() < 2) {
            log.warn("Insufficient data points to calculate distance and time. At least 2 points are required.");
            response.setTotalDistance("0.000 KM");
            response.setTotalTime("0 hrs 0 mins");
            return response;
        }

        try {
            // Build the OSRM API URL with multiple waypoints
            String waypoints = locationList.stream()
                    .map(point -> String.format("%f,%f", point.getLongitude(), point.getLatitude()))
                    .collect(Collectors.joining(";"));

            String url = String.format("http://localhost:5000/route/v1/driving/%s", waypoints);
//            String url = String.format("http://localhost:5001/route/v1/driving/%s", waypoints);
            log.info("Requesting OSRM API with URL: {}", url);

            // Call OSRM API
            String apiResponse = restTemplate.getForObject(url, String.class);
            JSONObject json = new JSONObject(apiResponse);

            // Extract distance (in meters) and duration (in seconds) from the response
            double distanceInMeters = json.getJSONArray("routes")
                                          .getJSONObject(0)
                                          .getDouble("distance");

            double durationInSeconds = json.getJSONArray("routes")
                                           .getJSONObject(0)
                                           .getDouble("duration");

            double distanceInKm = distanceInMeters / 1000.0;
            String formattedDistance = String.format("%.3f KM", distanceInKm);

            int hours = (int) (durationInSeconds / 3600);
            int minutes = (int) ((durationInSeconds % 3600) / 60);
            String formattedTime = String.format("%d hrs %d mins", hours, minutes);

            log.info("Real-world total distance calculated: {}, total time: {}", formattedDistance, formattedTime);

            response.setTotalDistance(formattedDistance);
            response.setTotalTime(formattedTime);
            return response;
        } catch (Exception e) {
            log.error("Error calling OSRM API: {}", e.getMessage());
            response.setTotalDistance("0.000 KM");
            response.setTotalTime("0 hrs 0 mins");
            return response;
        }
    }
}