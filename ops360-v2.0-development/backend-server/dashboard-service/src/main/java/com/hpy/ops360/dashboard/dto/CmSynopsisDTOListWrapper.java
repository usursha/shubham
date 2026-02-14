package com.hpy.ops360.dashboard.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

public class CmSynopsisDTOListWrapper extends GenericDto {
	
	@JsonIgnore
    private Long id;
	
    private List<CmSynopsisDTO> cmSynopsisDTOList;

    // Getters and Setters
    public List<CmSynopsisDTO> getCmSynopsisDTOList() {
        return cmSynopsisDTOList;
    }

    public void setCmSynopsisDTOList(List<CmSynopsisDTO> cmSynopsisDTOList) {
        this.cmSynopsisDTOList = cmSynopsisDTOList;
    }
}
