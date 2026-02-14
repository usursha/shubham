package com.hpy.mappingservice.response.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CEUserLocation {
    private String ceUserId;
    private Double userLat;
    private Double userLong;
    
    private String employeeCode;
    private String homeAddress;
    private Integer atmCount;
}