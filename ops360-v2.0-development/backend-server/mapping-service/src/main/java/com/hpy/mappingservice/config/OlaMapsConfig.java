// 1. OlaMapsConfig.java - Configuration for OLA Maps
package com.hpy.mappingservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "olamaps")
@Data
public class OlaMapsConfig {
    private String apiKey;
    private String baseUrl = "https://api.olamaps.io"; // Default OLA Maps base URL
    private String routingUrl = "/routing/v1/directions";
    private String distanceMatrixUrl = "/routing/v1/distanceMatrix";
}