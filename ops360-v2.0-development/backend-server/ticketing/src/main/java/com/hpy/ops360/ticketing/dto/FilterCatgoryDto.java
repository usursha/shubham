package com.hpy.ops360.ticketing.dto;

import java.util.List;

import com.hpy.generic.impl.GenericDto;

import lombok.Data;

@Data
public class FilterCatgoryDto extends GenericDto {
	private String screen;
	private String filterCode;
	private String filterLabel;
	private String seqOrder;
	private List<FilterCategoryValueDto> catgoryValueDtos;
}
