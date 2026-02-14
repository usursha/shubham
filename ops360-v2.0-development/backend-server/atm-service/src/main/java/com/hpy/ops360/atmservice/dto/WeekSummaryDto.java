package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekSummaryDto {
    private int srno;
    private String section;
    private int weekNum;
    private String weekdayName;
    private double achieved;
    private double target;
}
