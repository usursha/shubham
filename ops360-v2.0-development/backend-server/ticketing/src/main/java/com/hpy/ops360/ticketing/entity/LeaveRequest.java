package com.hpy.ops360.ticketing.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequest {

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "request_date")
	private LocalDateTime requestDate;

	@Column(name = "leave_type_id")
	private Long leaveTypeId;

	@Column(name = "reason_id")
	private Long reasonId;

	@Column(name = "absence_slot_id")
	private Long absenceSlotId;

	@Column(name = "custom_start_time")
	private LocalDateTime customStartTime;

	@Column(name = "custom_end_time")
	private LocalDateTime customEndTime;

	private String remarks;

	private String status;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "total_leave_requests")
	private long totalLeaveRequests;
//	
}