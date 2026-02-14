package com.hpy.ops360.dashboard.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiStatusResponse {
    private String apiUrl;
    private int status;
    
    private String data;
}
