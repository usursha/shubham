package com.hpy.ops360.location.dto;

import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.location.Point;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = false)
public abstract class UserLocationDto extends GenericDto {

	private static final long serialVersionUID = 1L;
	protected Point point;
	protected String username;

}
