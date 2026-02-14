package com.hpy.ops360.ticketing.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.Data;

public class HpyMatrixRespDto extends GenericDto {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Long id;
	private String location;
	private List<HpyMatrixLevelDto> data;

	public HpyMatrixRespDto(String location, List<HpyMatrixLevelDto> data) {
		super();
		this.location = location;
		this.data = data;
	}

	public HpyMatrixRespDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<HpyMatrixLevelDto> getData() {
		return data;
	}

	public void setData(List<HpyMatrixLevelDto> data) {
		this.data = data;
	}

}
