package com.hpy.ops360.framework.dto;

import com.hpy.generic.impl.GenericDto;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class UserLocationDto extends GenericDto {

	private static final long serialVersionUID = 1L;
	protected PointDto point;
	protected String username;

}
