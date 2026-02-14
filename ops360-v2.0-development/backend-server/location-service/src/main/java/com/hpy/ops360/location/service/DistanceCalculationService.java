package com.hpy.ops360.location.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hpy.ops360.location.dto.UserdistLocationDto;
import com.hpy.ops360.location.dto.UsertotaldistLocationDto;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

@Service
@Slf4j
public class DistanceCalculationService {

    private final RestTemplate restTemplate = new RestTemplate();

    public UsertotaldistLocationDto getTotalDistance(List<UserdistLocationDto> locationList) {
        log.info("Starting real-world total distance calculation for location updates. Number of points received: {}", 
                 locationList != null ? locationList.size() : 0);

        UsertotaldistLocationDto response = new UsertotaldistLocationDto();

        if (locationList == null || locationList.size() < 2) {
            log.warn("Insufficient data points to calculate distance. At least 2 points are required.");
            response.setTotalDistance("0.000 KM");
            return response;
        }

        try {
            // Build the OSRM API URL with multiple waypoints
            String waypoints = locationList.stream()
                    .map(point -> String.format("%f,%f", point.getLongitude(), point.getLatitude()))
                    .collect(Collectors.joining(";"));

            String url = String.format("http://router.project-osrm.org/route/v1/driving/%s", waypoints);

            log.info("Requesting OSRM API with URL: {}", url);

            // Call OSRM API
            String apiResponse = restTemplate.getForObject(url, String.class);
            JSONObject json = new JSONObject(apiResponse);

            // Extract distance (in meters) from the response
            double distanceInMeters = json.getJSONArray("routes")
                                          .getJSONObject(0)
                                          .getDouble("distance");

            double distanceInKm = distanceInMeters / 1000.0;
            String formattedDistance = String.format("%.3f KM", distanceInKm);

            log.info("Real-world total distance calculated: {}", formattedDistance);

            response.setTotalDistance(formattedDistance);
            return response;
        } catch (Exception e) {
            log.error("Error calling OSRM API: {}", e.getMessage());
            response.setTotalDistance("0.000 KM");
            return response;
        }
    }
}


