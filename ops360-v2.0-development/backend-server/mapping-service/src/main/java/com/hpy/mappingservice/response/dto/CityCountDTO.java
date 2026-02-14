package com.hpy.mappingservice.response.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityCountDTO {
    private String cityName;
    private int count;
}

