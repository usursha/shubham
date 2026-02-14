package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ZoneMasterDTO extends GenericDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private Long id;
	private Long zoneId;  // Renamed from id to zoneId
    private String zoneName;
    
	public ZoneMasterDTO(Long zoneId, String zoneName) {
		super();
		this.zoneId = zoneId;
		this.zoneName = zoneName;
	}
    
    
}
