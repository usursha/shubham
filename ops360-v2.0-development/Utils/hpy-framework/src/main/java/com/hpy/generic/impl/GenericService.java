package com.hpy.generic.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hpy.generic.IGenericDto;
import com.hpy.generic.IGenericEntity;
import com.hpy.generic.IGenericMapper;
import com.hpy.generic.IGenericRepo;
import com.hpy.generic.IGenericService;
import com.hpy.generic.Exception.EntityNotFoundException;

public abstract class GenericService<T extends IGenericDto, S extends IGenericEntity> implements IGenericService<T, S> {

	private static final Logger log = LoggerFactory.getLogger(GenericService.class);

	@Autowired
	private IGenericRepo<S> repo;

	@Autowired
	private IGenericMapper<T, S> mapper;

	public IGenericRepo<S> getRepo() {
		return repo;
	}

	@Override
	public T findById(Long id) throws EntityNotFoundException {
		Optional<S> entityOpt = repo.findById(id);
		log.debug("retrieving object by id {}", id);
		T dto = getMapper().toDto(entityOpt);
		return dto;
	}

	@Override
	public List<T> list() {
		List<S> listEntity = repo.findAll();
		List<T> list = getMapper().toDto(listEntity);
		return list;
	}

	@Override
	public T save(T dto) {
		S entity = getMapper().toEntity(dto);
		S saved = repo.save(entity);
		return getMapper().toDto(saved);
	}

	@Override
	public T update(T dto) throws EntityNotFoundException {
		return save(dto);
	}

	@Override
	public void delete(Long id) {
		repo.deleteById(id);
	}

	@Override
	public void delete(T dto) {
		S entity = getMapper().toEntity(dto);
		repo.delete(entity);
	}

	public IGenericMapper<T, S> getMapper() {
		return mapper;
	}

	public void setMapper(IGenericMapper<T, S> mapper) {
		this.mapper = mapper;
	}

}
