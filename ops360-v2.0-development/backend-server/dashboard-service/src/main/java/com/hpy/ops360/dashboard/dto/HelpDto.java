package com.hpy.ops360.dashboard.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HelpDto extends GenericDto {

	private static final long serialVersionUID = 2168207750462706949L;

	private String name;

	private String contactNo;

}
