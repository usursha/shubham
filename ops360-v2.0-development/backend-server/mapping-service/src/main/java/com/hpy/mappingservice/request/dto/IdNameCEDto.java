package com.hpy.mappingservice.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdNameCEDto {
    private String id;
    private String name;
    private String count;
}
