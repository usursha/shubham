package com.hpy.ops360.ticketing.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.ticketing.dto.RemarkAssetsDto;
import com.hpy.ops360.ticketing.entity.RemarkAssets;

@Component
public class RemarkAssetsMapper extends GenericMapper<RemarkAssetsDto, RemarkAssets> {

	public RemarkAssetsMapper() {
		super(RemarkAssetsDto.class, RemarkAssets.class);

	}

}
