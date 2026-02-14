package com.hpy.ops360.report_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownTicketReportRequestSearch {
    private String startDate;
    private String endDate;
    private String searchkey;
    private String ceFullName;
    private String atmId;
    private String bank;
    private String status;
    private String ticketNumber;
    private String owner;
    private String subCallType;
    private String businessModel;
    private String siteType;
    private String etaDateTime;
    private String sortby;
    
}