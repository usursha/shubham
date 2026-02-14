package com.hpy.ops360.report_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownTicketReportDTO {
    private String atmid;
    private String businesscategory;
    private String sitetype;
    private String bank;
    private String status;
    private String ticketnumber;
    private String downtimeinhrs;
    private String downtimebucket;
    private String vendor;
    private String owner;
    private String subcalltype;
    private String customerremark;
    private String cefullname;
    private String cmfullname;
    private String createddatetime;
    private String ceetadatetime;
    private String ceactiondatetime;
}