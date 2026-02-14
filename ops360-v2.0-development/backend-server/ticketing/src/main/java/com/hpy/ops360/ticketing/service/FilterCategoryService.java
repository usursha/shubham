package com.hpy.ops360.ticketing.service;

import org.springframework.stereotype.Service;

import com.hpy.generic.Exception.EntityNotFoundException;
import com.hpy.generic.impl.GenericService;
import com.hpy.ops360.ticketing.dto.FilterCatgoryDto;
import com.hpy.ops360.ticketing.entity.FilterCategory;
import com.hpy.ops360.ticketing.repository.FilterCategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class FilterCategoryService extends GenericService<FilterCatgoryDto, FilterCategory> {

	private FilterCategoryRepository filterCategoryRepository;

	@Transactional
	public FilterCatgoryDto getFilterCategoryById(Long filterCategoryId) throws EntityNotFoundException {
//		findById(filterCategoryId);
//		return findById(filterCategoryId);
		System.out.println(filterCategoryRepository.findById(filterCategoryId));
		return this.getMapper().toDto(filterCategoryRepository.findById(filterCategoryId));

	}

}
