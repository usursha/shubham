package com.hpy.mappingservice.automapping.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankInfo {
    private String bankName;
    private Long bankCount;
}