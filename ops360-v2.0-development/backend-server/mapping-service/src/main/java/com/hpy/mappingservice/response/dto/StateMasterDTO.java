package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StateMasterDTO extends GenericDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7965561071897121352L;
	@JsonIgnore
	private Long id;
	private Long stateId;  
    private String stateName;
    private Long zoneId;
    
	public StateMasterDTO(Long stateId, String stateName, Long zoneId) {
		super();
		this.stateId = stateId;
		this.stateName = stateName;
		this.zoneId = zoneId;
	}
    
    
}
