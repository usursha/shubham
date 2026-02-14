package com.hpy.rest.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.hpy.generic.impl.GenericDto;
import com.hpy.rest.response.ApiErrorResponse;

import lombok.Data;

@Data
public class MapResponseMapperDto<T extends GenericDto> implements IResponseDto {
	
	private static final long serialVersionUID = 1L;
	private Integer responseCode;
	private String message;
	private Map<Integer, List<T>> data;
	private ApiErrorResponse error;
	private LocalDateTime timeStamp;

}
