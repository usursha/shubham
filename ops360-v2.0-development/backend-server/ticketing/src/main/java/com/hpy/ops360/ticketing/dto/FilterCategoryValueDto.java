package com.hpy.ops360.ticketing.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.Data;

@Data
public class FilterCategoryValueDto extends GenericDto {
	private String filterValueCode;
	private String filterValue;
	private String seqOrder;

}
