package com.hpy.generic.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.hpy.generic.IGenericDto;
import com.hpy.generic.IGenericEntity;
import com.hpy.generic.IGenericMapper;
import com.hpy.generic.Exception.EntityNotFoundException;

public abstract class GenericMapper<T extends IGenericDto, S extends IGenericEntity> implements IGenericMapper<T, S> {

	private static final Logger log = LoggerFactory.getLogger(GenericMapper.class);

	private Class<T> dtoClazz;
	private Class<S> entityClazz;

	public GenericMapper(Class<T> dtoClazz, Class<S> entityClazz) {
		this.dtoClazz = dtoClazz;
		this.entityClazz = entityClazz;
	}

	@Override
	public T toDto(S entity) {
		if(entity==null) {
			return null;
		}
		try {
			T dto = dtoClazz.getDeclaredConstructor().newInstance();
			BeanUtils.copyProperties(entity, dto);
			return dto;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			log.error("Error Creating Dto", e);
			return null;
		}
	}

	@Override
	public S toEntity(T dto) {
		try {
			S entity = entityClazz.getDeclaredConstructor().newInstance();
			BeanUtils.copyProperties(dto, entity);
			return entity;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			log.error("Error Creating Entity", e);
			return null;
		}
	}

	@Override
	public T toDto(Optional<S> entityOpt) throws EntityNotFoundException {
		if (entityOpt.isPresent()) {
			return toDto(entityOpt.get());
		}
		throw new EntityNotFoundException("Entity Not found");
	}

	@Override
	public List<T> toDto(List<S> entity) {
		List<T> result = new ArrayList<>();
		if (entity == null)
			return null;
		for (S s : entity) {
			result.add(toDto(s));
		}

		return result;
	}

	@Override
	public List<S> toEntity(List<T> dto) {
		List<S> result = new ArrayList<>();
		for (T t : dto) {
			result.add(toEntity(t));
		}
		return result;
	}

}
