package com.hpy.mappingservice.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "leave_requests")
@Data
public class LeaveRequest{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserMapping user;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @ManyToOne
    @JoinColumn(name = "leave_type_id")
    private LeaveType leaveType;

    @ManyToOne
    @JoinColumn(name = "reason_id")
    private Reason reason;

    @ManyToOne
    @JoinColumn(name = "absence_slot_id")
    private AbsenceSlot absenceSlot;

    @Column(name = "custom_start_time")
    private LocalDateTime customStartTime;
    
    @Column(name = "custom_end_time")
    private LocalDateTime customEndTime;
    
    
    private String remarks;
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}