package com.hpy.ops360.ticketing.ticket.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

public class SelectedCategoryDto extends GenericDto {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Long id;

	private String selectedCategory;
	private List<BroadCategoryDto> broadCategoryDtos;

	public SelectedCategoryDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SelectedCategoryDto(String selectedCategory, List<BroadCategoryDto> broadCategoryDtos) {
		super();
		this.selectedCategory = selectedCategory;
		this.broadCategoryDtos = broadCategoryDtos;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public List<BroadCategoryDto> getBroadCategoryDtos() {
		return broadCategoryDtos;
	}

	public void setBroadCategoryDtos(List<BroadCategoryDto> broadCategoryDtos) {
		this.broadCategoryDtos = broadCategoryDtos;
	}

}
