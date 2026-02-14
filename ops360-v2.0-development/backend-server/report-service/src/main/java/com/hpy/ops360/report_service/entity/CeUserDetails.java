package com.hpy.ops360.report_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CeUserDetails {

    @Id
    @Column(name = "ce_userid")
    private String ceUserId;

    @Column(name = "ce_name")
    private String ceName;

    @Column(name = "ce_email")
    private String ceEmail;

    @Column(name = "ce_mobile")
    private String ceMobile;

}
