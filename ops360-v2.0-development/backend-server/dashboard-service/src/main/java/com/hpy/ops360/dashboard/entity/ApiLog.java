package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "api_log")
public class ApiLog {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long srno;
    
    private String userName;
    
    private String controllerName;
    
    private String className;

    private String methodName;
    
    private LocalDateTime dateOfInsertion;
    
    private String request;
    
    private String response;
    
    private int statusCode;
    
    private double timeTaken;

}