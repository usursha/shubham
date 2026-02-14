package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveRejectStatusresponseDto extends GenericDto{

	
	
	@JsonIgnore
    private Long id;
	
	private boolean success;
	private String message;
	
	
	public ApproveRejectStatusresponseDto(boolean b, String result) {
		this.success=b;
		this.message=result;
	}
}
