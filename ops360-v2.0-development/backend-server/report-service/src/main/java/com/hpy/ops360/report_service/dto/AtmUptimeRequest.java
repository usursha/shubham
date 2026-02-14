package com.hpy.ops360.report_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtmUptimeRequest {
    private String startDate;
    private String endDate;
    private String searchkey;
    private String uptimeRange;
    private String liveDate;
    private String atmId;
    private String bankName;
    private String city;
    private String site;
    private String siteId;
    private String status;
    private String sortBy;
    private Integer pageNo;
    private Integer pageSize;
}
