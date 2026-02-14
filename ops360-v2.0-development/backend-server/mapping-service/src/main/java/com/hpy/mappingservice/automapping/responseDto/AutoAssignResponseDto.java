package com.hpy.mappingservice.automapping.responseDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AutoAssignResponseDto extends GenericDto {
    
	@JsonIgnore
	private Long id;
    private String atmCode;
    private String address;
    private String newCeUserId;
    private String fullName;
    private Long newCeId;
    private Double distance;
    private String bankName;
    private String status;
    private String city;
}