package com.hpy.monitoringservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionsDto {
	
	private String id;
    private String username;
    private String userId;
    private String ipAddress;
    private String sessionStart;
    private String sessionEnd;
    private String userType;
    
 

}
