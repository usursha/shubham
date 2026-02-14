package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApprovalResponseDto extends GenericDto{
	
	public LeaveApprovalResponseDto(String message, Long requestId, String status) {
		super();
		this.message = message;
		this.requestId = requestId;
		this.status = status;
	}
	@JsonIgnore
	private Long id;
    private String message;
    private Long requestId;
    private String status;


    
   
}
