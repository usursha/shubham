package com.hpy.mappingservice.response.dto;

import lombok.Data;

@Data
public class AtmDto {
	
	private String psId;
	private String atmId;
	private int status;
	private CeDto ceDto;
	

}
