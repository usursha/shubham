package com.hpy.monitoringservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ApiLogSummary {
	
    @Id
    private Long srno;
    private String startTime;
    private String endTime;
    private String callCount;
    private String avgTimeTaken;
    private String maxTimeTaken;
    private String controllerDetails;

}