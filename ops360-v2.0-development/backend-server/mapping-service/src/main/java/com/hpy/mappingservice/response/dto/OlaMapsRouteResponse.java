// 3. OlaMapsRouteResponse.java - Response DTO for OLA Maps
package com.hpy.mappingservice.response.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OlaMapsRouteResponse {
    
    private List<Route> routes;
    private String status;
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Route {
        private Overview overview;
        private List<Leg> legs;
        
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Overview {
            private Double distance; // in meters
            private Double duration; // in seconds
            
            @JsonProperty("duration_in_traffic")
            private Double durationInTraffic; // in seconds
        }
        
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Leg {
            private Double distance; // in meters
            private Double duration; // in seconds
            
            @JsonProperty("start_location")
            private Location startLocation;
            
            @JsonProperty("end_location")
            private Location endLocation;
        }
        
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Location {
            private Double lat;
            private Double lng;
        }
    }
}