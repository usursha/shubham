package com.hpy.ops360.ticketing.cm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocTicketDetailsListEntity {

    @Id
    @Column(name = "srno")
    private String srno;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "owner")
    private String owner;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "subcall_type")
    private String subcallType;

    @Column(name = "uploaded_by")
    private String uploadedBy;

    @Column(name = "is_closed")
    private int isClosed;

    @Column(name = "is_down")
    private int isDown;

    @Column(name = "is_open")
    private int isOpen;

    @Column(name = "hours_passed")
    private String hoursPassed;
}
