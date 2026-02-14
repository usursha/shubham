package com.hpy.ops360.dashboard.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

public class AtmDetailsDtowrapper extends GenericDto {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Long id;

	private List<CMUnderCEAtmDetailsDTO> atmdetailsdtolist;

	public List<CMUnderCEAtmDetailsDTO> getAtmdetailsdtolist() {
		return atmdetailsdtolist;
	}

	public void setAtmdetailsdtolist(List<CMUnderCEAtmDetailsDTO> atmdetailsdtolist) {
		this.atmdetailsdtolist = atmdetailsdtolist;
	}

}
