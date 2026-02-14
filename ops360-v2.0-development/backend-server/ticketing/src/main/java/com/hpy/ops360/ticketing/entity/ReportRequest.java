package com.hpy.ops360.ticketing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
    private String username;
    private String reportUser;
    private String startDate;
    private String endDate;

}