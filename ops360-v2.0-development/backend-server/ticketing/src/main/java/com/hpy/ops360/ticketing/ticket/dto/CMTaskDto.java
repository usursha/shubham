package com.hpy.ops360.ticketing.ticket.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CMTaskDto extends GenericDto{
	
	@JsonIgnore
	private Long id;

	private Long srNo;

	private String atmId;

	private String comment;

	private String createdBy;

	private String diagnosis;

//	private String document;

	private String documentName;

	private String reason;

	private String ticketNumber;

	private String refNo;

	@Builder
	public CMTaskDto(Long srNo, String atmId, String comment, String createdBy, String diagnosis, String documentName,
			String reason, String ticketNumber, String refNo) {
		this.srNo = srNo;
		this.atmId = atmId;
		this.comment = comment;
		this.createdBy = createdBy;
		this.diagnosis = diagnosis;
		this.documentName = documentName;
		this.reason = reason;
		this.ticketNumber = ticketNumber;
		this.refNo = refNo;
	}
	
	

}
