package com.hpy.ops360.report_service.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class DownTicketReportEntityDownload {

    @Id
    private String ticketnumber;

    private String atmid;
    private String businesscategory;
    private String sitetype;
    private String bank;
    private String status;
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