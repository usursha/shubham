package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.ticketing.dto.GetManuallyTicketResponseDTO;

public class ManualTicketListWrapper extends GenericDto{
	
	@JsonIgnore
    private Long id;
	
    private List<GetManuallyTicketResponseDTO> manualticketList;

	public List<GetManuallyTicketResponseDTO> getManualticketList() {
		return manualticketList;
	}

	public void setManualticketList(List<GetManuallyTicketResponseDTO> manualticketList) {
		this.manualticketList = manualticketList;
	}

}
