package com.hpy.mappingservice.response.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class ExcelReponseWithStatus extends GenericDto {
	private static final long serialVersionUID = -6455188954538283853L;
	@JsonIgnore
	private Long id;
	private List<AtmDto> atmList;
	
	public ExcelReponseWithStatus(List<AtmDto> atmList) {
		super();
		this.atmList = atmList;
	} 
}
