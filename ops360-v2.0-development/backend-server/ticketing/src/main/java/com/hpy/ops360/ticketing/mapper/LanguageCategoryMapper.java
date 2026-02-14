package com.hpy.ops360.ticketing.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.ticketing.dto.LanguageCategoryDto;
import com.hpy.ops360.ticketing.entity.LanguageCategory;

@Component
public class LanguageCategoryMapper extends GenericMapper<LanguageCategoryDto, LanguageCategory> {

	public LanguageCategoryMapper() {
		super(LanguageCategoryDto.class, LanguageCategory.class);
		// TODO Auto-generated constructor stub
	}

}
