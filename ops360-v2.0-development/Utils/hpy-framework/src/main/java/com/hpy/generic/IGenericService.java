package com.hpy.generic;

import java.util.List;

import com.hpy.generic.Exception.EntityNotFoundException;

public interface IGenericService<T extends IGenericDto, S extends IGenericEntity> {

	public T findById(Long id) throws EntityNotFoundException;

	public List<T> list();

	public T save(T dto);

	public T update(T dto) throws EntityNotFoundException;

	public void delete(Long id);

	public void delete(T dto);


	


}
