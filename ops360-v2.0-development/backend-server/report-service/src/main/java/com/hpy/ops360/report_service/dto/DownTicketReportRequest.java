package com.hpy.ops360.report_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownTicketReportRequest {
    private String startDate;
    private String endDate;
    private String ceFullName;
    private String searchkey;
    private String atmId;
    private String bank;
    private String status;
    private String ticketNumber;
    private String owner;
    private String subCallType;
    private String businessModel;
    private String siteType;
    private String etaDateTime;
    private Integer pageIndex;
    private Integer pageSize;
    private String sortby;
    
}