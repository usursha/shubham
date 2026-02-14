package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryCeManualAtmResponseDto{
	@JsonIgnore
	private Long id;
	
    private String atm_id;
    private String bank_name;
    private String address;
    private String assigned_ce;
	private String city;
    private String dist_from_base;
    private int status; // 0 or 1
}

