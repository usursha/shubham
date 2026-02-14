package com.hpy.ops360.report_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UptimeReportdownloadRequest {

    private String startdate;
    private String enddate;
    private String searchkey;
    private String uptimeAchievedRange;
    private String uptimeTargetRange;
    private String txnAchievedRange;
    private String txnTargetRange;

}
