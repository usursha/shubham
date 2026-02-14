package com.hpy.monitoringservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ControllerDetails {
	
    @Id
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

    // Getters and Setters
}