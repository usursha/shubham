package com.hpy.ops360.report_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtmUptimeRequestDownload {
    private String startDate;
    private String endDate;
    
}
