package com.hpy.rest.dto;

import java.io.File;
import java.time.LocalDateTime;

import com.hpy.generic.impl.GenericDto;
import com.hpy.rest.response.ApiErrorResponse;

import lombok.Data;

@Data
public class FileTypeResponseDto<T extends GenericDto> implements IResponseDto  {
private static final long serialVersionUID = -7472399969592945936L;
	
	private Integer responseCode;
	private String message;
	private  File data;
	private ApiErrorResponse error;
	private LocalDateTime timeStamp;

}
