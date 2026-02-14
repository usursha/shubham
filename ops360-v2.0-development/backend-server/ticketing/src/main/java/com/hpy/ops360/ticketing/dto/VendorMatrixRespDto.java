package com.hpy.ops360.ticketing.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

public class VendorMatrixRespDto extends GenericDto {

	@JsonIgnore
	private Long id;
	private String location;
	private List<VendorMatrixLevelDto> data;

	public VendorMatrixRespDto(String location, List<VendorMatrixLevelDto> data) {
		super();
		this.location = location;
		this.data = data;
	}

	public VendorMatrixRespDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<VendorMatrixLevelDto> getData() {
		return data;
	}

	public void setData(List<VendorMatrixLevelDto> data) {
		this.data = data;
	}

}
