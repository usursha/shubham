package com.hpy.monitoringservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserSessionsEntity {
	
	@Id
	private String id;
    private String username;
    private String userId;
    private String ipAddress;
    private String sessionStart;
    private String sessionEnd;
    private String userType;
  
}
