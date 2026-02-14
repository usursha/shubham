package com.hpy.ops360.dashboard.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.dashboard.dto.CmSynopsisDTO;
import com.hpy.ops360.dashboard.entity.CmSynopsis;

@Component
public class CmSynopsisMapper extends GenericMapper<CmSynopsisDTO, CmSynopsis> {

	public CmSynopsisMapper() {
		super(CmSynopsisDTO.class, CmSynopsis.class);
		// TODO Auto-generated constructor stub
	}

}
