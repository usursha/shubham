package com.hpy.mappingservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class LeaveRequestentity{
    @Id
    @Column(name = "id")	
    private Long srno;

    @Column(name = "user_id")
    private Long userid;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Column(name = "leave_type_id")
    private Long leaveTypeid;

    @Column(name = "reason_id")
    private Long reason;

    @Column(name = "absence_slot_id")
    private Long absenceSlot;

    @Column(name = "custom_start_time")
    private LocalDateTime customStartTime;
    
    @Column(name = "custom_end_time")
    private LocalDateTime customEndTime;
    
    @Column(name = "remarks")
    private String remarks;
    
    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}