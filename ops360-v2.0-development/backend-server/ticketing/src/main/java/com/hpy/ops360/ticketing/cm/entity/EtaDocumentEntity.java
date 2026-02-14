package com.hpy.ops360.ticketing.cm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
public class EtaDocumentEntity {

    @Id
    private Long srno; // Required for JPA, even if not meaningful

    @Column(name = "source")
    private String source;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "ticket_type")
    private String ticketType;
    
    @Column(name = "is_flagged")
    private String isFlagged;
    
    @Column(name = "hours_passed")
    private String hoursPassed;
    
    @Column(name = "ticket_number")
    private String ticketNumber;
    
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
