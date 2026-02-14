package com.hpy.ops360.ticketing.cm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class DocTicketListEntity {

    @Id
    @Column(name = "srno")
    private Integer srNo;  // From ROW_NUMBER() in SP

    @Column(name = "atm_id")
    private String atmId;

    @Column(name = "ticket_number")
    private String ticketNumber;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "owner")
    private String owner;

    @Column(name = "vendor")
    private String vendor = " ";  // As set in SP

    @Column(name = "subcall_type")
    private String subcallType;

    @Column(name = "internal_remark")
    private String internalRemark;

    @Column(name = "status")
    private String status;

    @Column(name = "ticket_type")
    private String ticketType;  

    @Column(name = "is_flagged")
    private Integer isFlagged;  // 0 or 1 from CASE in SP

    @Column(name = "hours_passed")
    private String hoursPassed;  // VARCHAR from SP

    @Column(name = "document")
    private String document;
    
    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document1")
    private String document1;
    
    @Column(name = "document1_name")
    private String document1Name;

    @Column(name = "document2")
    private String document2;
    
    @Column(name = "document2_name")
    private String document2Name;

    @Column(name = "document3")
    private String document3;
    
    @Column(name = "document3_name")
    private String document3Name;

    @Column(name = "document4")
    private String document4;
    
    @Column(name = "document4_name")
    private String document4Name;
   
}