package com.hpy.rest.dto;

import java.time.LocalDateTime;

import com.hpy.generic.impl.GenericDto;
import com.hpy.rest.response.ApiErrorResponse;

import lombok.Data;

@Data
public class ResponseDto<T extends GenericDto> implements IResponseDto {
	private static final long serialVersionUID = -7472399969592945936L;
	
	private Integer responseCode;
	private String message;
	private T data;
	private ApiErrorResponse error;
	private LocalDateTime timeStamp;
	
}
