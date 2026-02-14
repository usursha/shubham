package com.hpy.ops360.location.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserLocationDto extends GenericDto {

	private static final long serialVersionUID = -7873915346105070205L;

	private String username;

	private double latitude;

	private double longitude;
}
