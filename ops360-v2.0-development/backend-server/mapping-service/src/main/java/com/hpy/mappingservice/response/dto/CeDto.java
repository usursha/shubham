package com.hpy.mappingservice.response.dto;

import lombok.Data;

@Data
public class CeDto {
	
	private String username;
	private int status;
	private CmDto cmDto;

}
