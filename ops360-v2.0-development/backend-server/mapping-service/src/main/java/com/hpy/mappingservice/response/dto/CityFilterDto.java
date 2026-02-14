package com.hpy.mappingservice.response.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CityFilterDto {
    private String name;
    private Integer count;
}
