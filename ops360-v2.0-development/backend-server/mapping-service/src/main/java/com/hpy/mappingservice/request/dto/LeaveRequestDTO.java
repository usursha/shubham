package com.hpy.mappingservice.request.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeaveRequestDTO {

	@NotNull(message = "Leave type cannot be null")
	private Integer leaveTypeId;

	@NotNull(message = "Reason cannot be null")
	private Integer reasonId;

	private Integer absenceSlotId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime customStartTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime customEndTime;
	private String remarks;
}