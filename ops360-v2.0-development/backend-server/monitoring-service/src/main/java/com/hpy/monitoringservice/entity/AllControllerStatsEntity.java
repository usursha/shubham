package com.hpy.monitoringservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AllControllerStatsEntity {
	
	@Id
	private Long srno;

    private String controllerName;
    private String callCount;
    private String avgTimeTaken;
    private String maxTimeTaken;

    
}