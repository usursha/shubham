package com.hpy.generic;

import java.util.List;
import java.util.Optional;

import com.hpy.generic.Exception.EntityNotFoundException;

public interface IGenericMapper<T extends IGenericDto, S extends IGenericEntity> {

	T toDto(S entity);

	T toDto(Optional<S> entityOpt) throws EntityNotFoundException;

	S toEntity(T dto);

	List<T> toDto(List<S> entity);

	List<S> toEntity(List<T> dto);
}
