package com.hpy.mappingservice.response.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoapifyMatrixResponse {
    private MatrixElement[][] matrix;
    private String mode;
    private String units;
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MatrixElement {
        private Double distance; // in meters
        private Double time;     // in seconds
        private Integer status;  // 0 = success, other values = error
    }
}