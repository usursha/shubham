package com.hpy.ops360.ticketing.dto;

import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto extends UserLocationDto {
	private static final long serialVersionUID = 8850769524254364847L;

	private String ticketNumber; // ticketno

	private String atmid;

	private String comment;

	//@Size(max = 20, message = "createdBy must not exceed 20 characters")
	private String createdBy;

//	private LocalDateTime createdDate;

	private String diagnosis;

//	@Exclude
//	private String document;
//
//	private String documentName;

	private TaskDocumentDto taskDocumentDto;

	private String lastModifiedBy;

//	private LocalDateTime lastModifiedDate;

	private String reason;

	private String refno; // not sure what it is

	private String status;

	private String username;

}
