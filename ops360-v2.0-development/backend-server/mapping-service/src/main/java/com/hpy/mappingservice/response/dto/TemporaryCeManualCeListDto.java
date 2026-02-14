package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryCeManualCeListDto {
	
    private Long ceId;
    private String ceUserId;
    private String ceName;
    private String ceProfile;
    private String employeeId;
    private String address;
    private String area;
    private Double latitude;
    private Double longitude;
    private Integer assignedAtm;
    private Integer mappedAtm;
    private Integer remainingAtm;
    private Double atmDistance;
}

