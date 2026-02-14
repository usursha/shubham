package com.hpy.ops360.ticketing.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestDTO {

	private Long userId;

	private LocalDateTime requestDate;

	private Long leaveTypeId;

	private Long reasonId;

	private Long absenceSlotId;

	private LocalDateTime customStartTime;

	private LocalDateTime customEndTime;

	private String remarks;

	private String status;

	private LocalDateTime createdAt;

	private String type = "LEAVE";
}
