package com.hpy.ops360.ticketing.ticket.dto;

import java.time.LocalDateTime;

import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskDTO extends UserLocationDto {
	private static final long serialVersionUID = 8850769524254364847L;

	private Long taskId;
	private String atmId;
	private String comment;
	private String createdBy;
	private LocalDateTime createdDate;
	private String diagnosis;
	private String document;
	private String documentName;
	private String document1;
	private String document1Name;
	private String document2;
	private String document2Name;
	private String document3;
	private String document3Name;
	private String document4;
	private String document4Name;
	private String lastModifiedBy;
	private LocalDateTime lastModifiedDate;
	private String reason;
	private String refNo;
	private String status;
	private String ticketNumber;
	private String username;
	private String checkerName;
	private LocalDateTime checkerTime;
	private String checkerRejectReason;
	private String checkerComment;
	private String crmStatus;
	private LocalDateTime crmTime;
	private Long atmMasterId;
	private String atmCode;
	private String address;
	private String bankName;
	private String city;
	private String grade;
	private String state;
	private String zone;
	private String source;
	private Double latitude;
	private Double longitude;

	private String errorMessage; // Add this field

}
