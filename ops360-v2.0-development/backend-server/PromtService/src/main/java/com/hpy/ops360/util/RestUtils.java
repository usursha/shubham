package com.hpy.ops360.util;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.hpy.generic.IGenericDto;
import com.hpy.generic.impl.GenericDto;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.dto.ResponseDto;
import com.hpy.rest.dto.ResponseErrorDto;
import com.hpy.rest.dto.ResponseListDto;
import com.hpy.rest.exception.ErrorCode;
import com.hpy.rest.response.ApiErrorResponse;

@Component
public class RestUtils {

	 // Method to wrap a single object response	
	public <T extends GenericDto> IResponseDto wrapResponse(T obj, String message) {

		ResponseDto<T> response = new ResponseDto<>();

		response.setResponseCode(HttpStatus.OK.value());
		response.setMessage(message);
		response.setData(obj);
		return response;

	}

	// Method to wrap a list of objects response
	public <T extends IGenericDto> IResponseDto wrapResponse(List<T> obj, String message) {

		ResponseListDto<T> response = new ResponseListDto<>();

		response.setResponseCode(HttpStatus.OK.value());
		response.setMessage(message);
		response.setData(obj);
		return response;

	}
	
	 // method to wrap specifically for null object cases
	public <T extends GenericDto> IResponseDto wrapNullResponse(String message, HttpStatus status) {
	    ResponseDto<T> response = new ResponseDto<>();
	    response.setResponseCode(status.value());
	    response.setMessage(message);
	    response.setData(null);
	    return response;
	}
	
	// Method to wrap error responses
    public IResponseDto wrapErrorResponse(ErrorCode errorCode) {
        return new ResponseErrorDto(errorCode);
    }
}

