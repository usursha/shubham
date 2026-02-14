package com.hpy.mappingservice.response.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.Data;

@Data
public class AbsenceSlotDTO extends GenericDto{
	public AbsenceSlotDTO(Long srno, String name, LocalTime startTime, LocalTime endTime, boolean isActive) {
		super();
		this.srno = srno;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isActive = isActive;
	}

	@JsonIgnore
	private Long id;

    private Long srno;

    private String name;

    private LocalTime startTime;

    private LocalTime endTime;

    private boolean isActive;
    
    
}