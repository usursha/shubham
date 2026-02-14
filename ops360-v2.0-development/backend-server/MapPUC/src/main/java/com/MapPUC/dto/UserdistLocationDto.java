package com.MapPUC.dto;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserdistLocationDto {
	
	private Long srno;
	
	private String username;
	
	private Double latitude;
	
	private Double longitude;
	
    private LocalDateTime createdOn;
    
    

	

    
}
