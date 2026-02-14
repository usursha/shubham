package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

public class ResponseCmTasksWithDocumentsWrapper extends GenericDto {
	private static final long serialVersionUID = -4475939279984752480L;
	@JsonIgnore
	private Long id;
	private List<CmTasksWithDocumentsDto> responseCmdtolist;

	public List<CmTasksWithDocumentsDto> getResponseCmdtolist() {
		return responseCmdtolist;
	}

	public void setResponseCmdtolist(List<CmTasksWithDocumentsDto> responseCmdtolist) {
		this.responseCmdtolist = responseCmdtolist;
	}


}
