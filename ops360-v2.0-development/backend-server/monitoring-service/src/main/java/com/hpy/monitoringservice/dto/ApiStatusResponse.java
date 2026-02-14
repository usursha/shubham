package com.hpy.monitoringservice.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiStatusResponse {
    private String apiUrl;
    private int status;
}
