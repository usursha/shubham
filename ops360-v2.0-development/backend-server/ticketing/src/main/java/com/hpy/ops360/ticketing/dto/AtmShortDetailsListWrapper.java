package com.hpy.ops360.ticketing.dto;

import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;

import java.util.List;

public class AtmShortDetailsListWrapper extends GenericDto {
  
	private static final long serialVersionUID = 1L;
	
	private List<AtmShortDetailsDto> atmShortDetailsList;

	
	
	
    public AtmShortDetailsListWrapper() {
        // Default constructor
    }

    public AtmShortDetailsListWrapper(List<AtmShortDetailsDto> atmShortDetailsList) {
        this.atmShortDetailsList = atmShortDetailsList;
    }

    public List<AtmShortDetailsDto> getAtmShortDetailsList() {
        return atmShortDetailsList;
    }

    public void setAtmShortDetailsList(List<AtmShortDetailsDto> atmShortDetailsList) {
        this.atmShortDetailsList = atmShortDetailsList;
    }
}