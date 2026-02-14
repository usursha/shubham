package com.hpy.ops360.ticketing.cm.dto;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.ticketing.dto.RemarksDto;

public class RemarksrequestDtoWrapper extends GenericDto {
	
	@JsonIgnore
    private Long id;
	
    private List<RemarksDto> remarksdtolist;

	public List<RemarksDto> getRemarksdtolist() {
		return remarksdtolist;
	}

	public void setRemarksdtolist(List<RemarksDto> remarksdtolist) {
		this.remarksdtolist = remarksdtolist;
	}

    
}

