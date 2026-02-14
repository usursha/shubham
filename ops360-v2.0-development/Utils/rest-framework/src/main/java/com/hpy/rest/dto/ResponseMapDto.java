package com.hpy.rest.dto;


import java.util.Map;

import com.hpy.generic.IGenericDto;

import lombok.Data;

@Data
public class ResponseMapDto<String, Object> implements IResponseDto {
	private static final long serialVersionUID = 1L;
	private Integer responseCode;
	private String message;
	private Map<String, Object> data;
}