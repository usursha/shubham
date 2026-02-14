package com.hpy.monitoringservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ControllerStats {
	
    @Id
    private Long srno;
    
    private String userName;
    private String callCount;
    private String avgTimeTaken;
    private String maxTimeTaken;

}