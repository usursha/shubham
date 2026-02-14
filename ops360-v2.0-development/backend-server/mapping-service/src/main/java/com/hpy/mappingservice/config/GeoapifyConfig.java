package com.hpy.mappingservice.config;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "geoapify")
@Data
public class GeoapifyConfig {
    private String apiKey;
    private String baseUrl;
    private String routingUrl;
}