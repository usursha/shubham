package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseDTO extends GenericDto{
	
	@JsonIgnore
	private Long id;
	private String status;
	private String ticketType;
	private String isFlagged;
	private String hoursPassed;
	
    private List<Document> documentList;
}
