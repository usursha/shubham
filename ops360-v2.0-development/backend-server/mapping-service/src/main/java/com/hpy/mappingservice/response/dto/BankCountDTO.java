package com.hpy.mappingservice.response.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankCountDTO {
    private String bankName;
    private int count;
}
