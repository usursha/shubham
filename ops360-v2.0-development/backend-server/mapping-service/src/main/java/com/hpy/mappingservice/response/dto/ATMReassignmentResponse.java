package com.hpy.mappingservice.response.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ATMReassignmentResponse extends GenericDto{
//    private String atmCode;
//    private String originalCeUserId;
//    private String newCeUserId;
//    private Double distance;
//    private Double travelTime;
	
	private String atmCode;
    private String originalCeUserId;
    private String newCeUserId;
    private String employeeCode;
    private String homeAddress;
    private Integer atmCount;
    private Double distance;
    private Double travelTime;
    
}