package com.hpy.ops360.ticketing.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.ticketing.dto.FilterCatgoryDto;
import com.hpy.ops360.ticketing.entity.FilterCategory;

@Component
public class FilterCatgoryMapper extends GenericMapper<FilterCatgoryDto, FilterCategory> {

	public FilterCatgoryMapper() {
		super(FilterCatgoryDto.class, FilterCategory.class);

	}

}
