package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnmappedATMResponseDTO extends GenericDto{

	@JsonIgnore
	private Long id;
	
    private Integer srNo;
    private String atmCode;
    private String address;
    private String bankName;
    private String city;
    private String distanceFromBase;
}
