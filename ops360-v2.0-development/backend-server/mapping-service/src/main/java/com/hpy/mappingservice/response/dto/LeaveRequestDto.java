package com.hpy.mappingservice.response.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.Data;

@Data
public class LeaveRequestDto extends GenericDto {

	@JsonIgnore
	private Long id;

	private Long srno;
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

	public LeaveRequestDto(Long srno, Long userId, LocalDateTime requestDate, Long leaveTypeId, Long reasonId,
			Long absenceSlotId, LocalDateTime customStartTime, LocalDateTime customEndTime, String remarks,
			String status, LocalDateTime createdAt) {
		super();
		this.srno = srno;
		this.userId = userId;
		this.requestDate = requestDate;
		this.leaveTypeId = leaveTypeId;
		this.reasonId = reasonId;
		this.absenceSlotId = absenceSlotId;
		this.customStartTime = customStartTime;
		this.customEndTime = customEndTime;
		this.remarks = remarks;
		this.status = status;
		this.createdAt = createdAt;
	}

}
