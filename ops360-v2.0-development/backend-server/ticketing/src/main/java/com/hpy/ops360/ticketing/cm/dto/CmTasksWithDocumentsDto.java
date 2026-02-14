package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.IGenericDto;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CmTasksWithDocumentsDto extends GenericDto implements IGenericDto {

	@JsonIgnore
	private Long id;
	private static final long serialVersionUID = -1127930846425424213L;

	private Long srNo;

	private String atmId;

	private String comment;

	private String createdBy;

	private String diagnosis;

	private String reason;

	private String ticketNumber;

	private String status;

	private String refNo;

	private String createdDate;

	private String username;

	private List<String> documents;

}
