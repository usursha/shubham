package com.hpy.ops360.ticketing.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hpy.generic.impl.GenericService;
import com.hpy.ops360.ticketing.dto.LanguageCategoryDto;
import com.hpy.ops360.ticketing.entity.LanguageCategory;
import com.hpy.ops360.ticketing.repository.LanguageCategoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LanguageCategoryService extends GenericService<LanguageCategoryDto, LanguageCategory> {

	private LanguageCategoryRepository languageCategoryRepository;

	public List<LanguageCategoryDto> getActivatedLanguageCategories() {

		return getMapper().toDto(languageCategoryRepository.findByActivatedTrue());

	}

}
