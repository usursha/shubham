package com.hpy.mappingservice.entity.bulkentity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "leave_requests")
@Getter
@Setter
public class LeaveRequestAlt {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "request_date")
    private java.time.LocalDateTime requestDate;

    @Column(name = "leave_type_id", nullable = false)
    private Integer leaveTypeId;

    @Column(name = "reason_id", nullable = false)
    private Integer reasonId;

    @Column(name = "absence_slot_id")
    private Integer absenceSlotId;

    @Column(name = "custom_start_time")
    private java.time.LocalDateTime customStartTime;

    @Column(name = "custom_end_time")
    private java.time.LocalDateTime customEndTime;

    @Column(name = "remarks", columnDefinition = "nvarchar(max)")
    private String remarks;

    @Column(name = "status", nullable = false, length = 100)
    private String status;

    @Column(name = "created_at", nullable = false)
    private java.time.LocalDateTime createdAt;
}
