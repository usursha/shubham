package com.hpy.mappingservice.request.dto;

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
@Table(name = "extend_history")
@Getter
@Setter
public class ExtendCEmapping {

    @Id
	@Column(name = "sr_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long srNo;

    @Column(name = "atm_id", nullable = false)
    private String atmId;

    @Column(name = "leave_request_id", nullable = false)
    private Long leaveRequestId;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
