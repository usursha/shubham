package com.hpy.monitoringservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControllerDetailsDTO {
    private Long srno;
    private String userName;
    private String controllerName;
    private String className;
    private String methodName;
    private String dateOfInsertion;
    private String request;
    private String response;
    private Integer statusCode;
    private String timeTaken;

}