package com.hpy.mappingservice.response.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ATMCEMapping {
    private String ceUserId;
    private String atmCode;
    private Double atmLat;
    private Double atmLong;
}