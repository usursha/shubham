package com.hpy.ops360.ticketing.cm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TicketWithDocumentsEntity {

    @Id
    private Integer srno;
    
    @Column(name = "ticket_number")
    private String ticketNumber;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "ticket_type")
    private String ticketType;
    
    @Column(name = "is_flagged")
    private Integer isFlagged;
    
    @Column(name = "hours_passed")
    private String hoursPassed;
    
    @Column(name = "no_of_attachments")
    private Integer noOfAttachments;
    
    @Column(name = "thumbnail_image")
    private String thumbnailImage;
}
