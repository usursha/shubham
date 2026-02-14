package com.hpy.ops360.ticketing.entity;

import com.hpy.generic.impl.GenericEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "language_category")
public class LanguageCategory extends GenericEntity {

	@Column(name = "language_level", length = 255)
	private String languageLevel;

	@Column(name = "name", length = 20)
	private String name;

	@Column(name = "language_code", length = 2)
	private String languageCode;

	@Column(name = "activated")
	private boolean activated;
}
