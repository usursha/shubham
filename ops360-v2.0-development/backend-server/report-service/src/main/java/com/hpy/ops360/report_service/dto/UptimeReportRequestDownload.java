package com.hpy.ops360.report_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UptimeReportRequestDownload {

	private String startdate;
    private String enddate;
    
}
