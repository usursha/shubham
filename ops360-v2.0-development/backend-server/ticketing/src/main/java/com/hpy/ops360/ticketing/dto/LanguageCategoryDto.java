package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.Data;

@Data
public class LanguageCategoryDto extends GenericDto {

	private static final long serialVersionUID = -4459685453309949157L;
	@JsonIgnore
	private Long id;

	private String languageLevel;

	private String name;

	private String languageCode;

//	private boolean activated;

}
