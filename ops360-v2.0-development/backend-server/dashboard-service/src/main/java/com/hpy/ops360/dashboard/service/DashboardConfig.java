package com.hpy.ops360.dashboard.service;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "dashboard")
public class DashboardConfig {
    private List<String> apiUrls;
}
