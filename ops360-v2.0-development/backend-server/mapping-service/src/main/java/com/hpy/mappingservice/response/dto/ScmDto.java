package com.hpy.mappingservice.response.dto;

import lombok.Data;

@Data
public class ScmDto {
	private String username;
	private int status;
	private ZonalHeadDto zonalHeadDto;
}
