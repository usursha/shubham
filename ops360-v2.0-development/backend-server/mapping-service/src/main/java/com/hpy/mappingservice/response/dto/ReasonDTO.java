package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import lombok.Data;

@Data
public class ReasonDTO extends GenericDto {
	
	@JsonIgnore
	private Long id;

    private Long srno;

    private String name;

    private boolean isActive;
    
    public ReasonDTO(Long srno, String name, boolean isActive) {
		super();
		this.srno = srno;
		this.name = name;
		this.isActive = isActive;
	}
}