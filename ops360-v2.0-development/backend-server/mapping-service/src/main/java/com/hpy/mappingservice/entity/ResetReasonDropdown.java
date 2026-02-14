package com.hpy.mappingservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reset_reason_dropdown")
@Getter
@Setter
public class ResetReasonDropdown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "srno")
    private Long srno;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
