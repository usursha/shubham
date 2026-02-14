package com.hpy.monitoringservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControllerStatsDTO {
    private String userName;
    private String callCount;
    private String avgTimeTaken;
    private String maxTimeTaken;

    // Getters and Setters
}