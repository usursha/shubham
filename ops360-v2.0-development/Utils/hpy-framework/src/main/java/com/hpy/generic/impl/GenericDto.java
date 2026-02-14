package com.hpy.generic.impl;

import com.hpy.generic.IGenericDto;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class GenericDto implements IGenericDto {

	private static final long serialVersionUID = 2996696769474882385L;

	private Long id;

}
