package com.hpy.ops360.location.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserdistLocationDto {
	
	private Long srno;
	
	private String username;
	
	private Double latitude;
	
	private Double longitude;
	
    private LocalDateTime createdOn;

	

    
}
