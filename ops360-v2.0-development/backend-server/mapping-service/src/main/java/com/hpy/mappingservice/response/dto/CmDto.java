package com.hpy.mappingservice.response.dto;

import lombok.Data;

@Data
public class CmDto {
	
	private String username;
	private int status;
	private ScmDto scmDto;

}
