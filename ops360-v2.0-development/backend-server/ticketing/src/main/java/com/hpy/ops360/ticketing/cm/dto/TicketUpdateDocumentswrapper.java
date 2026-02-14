package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.ticketing.dto.DocumentDto;

import lombok.Builder;

public class TicketUpdateDocumentswrapper extends GenericDto {
	
	private static final long serialVersionUID = 1L;


	@JsonIgnore
    private Long id;
	

	private List<DocumentDto> documentDtolist;


	public List<DocumentDto> getDocumentDtolist() {
		return documentDtolist;
	}


	public void setDocumentDtolist(List<DocumentDto> documentDtolist) {
		this.documentDtolist = documentDtolist;
	}

	

    
}
