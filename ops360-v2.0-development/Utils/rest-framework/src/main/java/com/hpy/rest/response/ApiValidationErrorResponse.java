package com.hpy.rest.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiValidationErrorResponse {
    private int responseCode;
    private String message;
    private String error;
    private Map<String, List<String>> errors;
    private int errorNumber;
    private LocalDateTime timestamp;
}
