package com.hpy.mappingservice.response.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoapifyRouteResponse {
    private List<Feature> features;
    
    @Data
    public static class Feature {
        private Properties properties;
    }
    
    @Data
    public static class Properties {
        private Double distance;
        private Double time;
    }
}