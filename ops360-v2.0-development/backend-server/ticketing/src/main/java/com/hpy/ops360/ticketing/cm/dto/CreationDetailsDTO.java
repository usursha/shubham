package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreationDetailsDTO extends GenericDto{
	
	@JsonIgnore
	private Long id;
	private String srno;
	private String ticketNumber;
	private String createdBy;
	private String createdTime;

}
