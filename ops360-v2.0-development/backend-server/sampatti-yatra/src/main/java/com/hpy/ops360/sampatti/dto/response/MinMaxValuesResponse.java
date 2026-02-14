package com.hpy.ops360.sampatti.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinMaxValuesResponse {
    private Integer minAchieved;
    private Integer maxAchieved;
}