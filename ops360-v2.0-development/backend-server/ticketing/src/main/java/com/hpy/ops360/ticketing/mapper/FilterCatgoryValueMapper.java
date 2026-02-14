package com.hpy.ops360.ticketing.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.ticketing.dto.FilterCategoryValueDto;
import com.hpy.ops360.ticketing.entity.FilterCategoryValue;

@Component
public class FilterCatgoryValueMapper extends GenericMapper<FilterCategoryValueDto, FilterCategoryValue> {

	public FilterCatgoryValueMapper() {
		super(FilterCategoryValueDto.class, FilterCategoryValue.class);

	}

}
