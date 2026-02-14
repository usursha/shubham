package com.hpy.mappingservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reset_reason")
@Getter
@Setter
public class ResetReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "reason_comment", columnDefinition = "nvarchar(max)")
    private String reasonComment;

    @Column(name = "atm_id", nullable = false)
    private Long atmId;

    @Column(name = "leave_request_id", nullable = false)
    private Long leaveRequestId;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
