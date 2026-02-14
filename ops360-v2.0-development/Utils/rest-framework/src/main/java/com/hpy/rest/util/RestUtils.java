package com.hpy.rest.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.hpy.generic.IGenericDto;
import com.hpy.generic.impl.GenericDto;
import com.hpy.rest.dto.FileTypeResponseDto;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.dto.ResponseDto;
//import com.hpy.rest.dto.ResponseErrorDto;
import com.hpy.rest.dto.ResponseListDto;
import com.hpy.rest.dto.ResponseListStringDto;
//import com.hpy.rest.exception.ErrorCode;
//import com.hpy.rest.response.ApiErrorResponse;
import com.hpy.rest.dto.ResponseStringDto;
import com.hpy.rest.dto.ResponseStringDto2;

import reactor.core.publisher.Mono;

@Component
public class RestUtils {

	 // Method to wrap a single object response	
	public <T extends GenericDto> IResponseDto wrapResponse(T obj, String message) {

		ResponseDto<T> response = new ResponseDto<>();

		response.setResponseCode(HttpStatus.OK.value());
		response.setMessage(message);
		response.setData(obj);
		response.setError(null);
		response.setTimeStamp(LocalDateTime.now());
		return response;

	}
	
	// Method to wrap a mono-objects response
	public <T extends GenericDto> IResponseDto wrapResponse(Mono<T> obj, String message) {

		ResponseDto<T> response = new ResponseDto<>();

		response.setResponseCode(HttpStatus.OK.value());
		response.setMessage(message);
		response.setData(obj.block());
		response.setError(null);
		response.setTimeStamp(LocalDateTime.now());
		return response;

	}

	// Method to wrap a list of objects response
	public <T extends GenericDto> IResponseDto wrapResponse(List<T> obj, String message) {

		ResponseListDto<T> response = new ResponseListDto<>();

		response.setResponseCode(HttpStatus.OK.value());
		response.setMessage(message);
		response.setData(obj);
		response.setError(null);
		response.setTimeStamp(LocalDateTime.now());
		return response;

	}
	
	 // method to wrap specifically for null object cases
	public <T extends GenericDto> IResponseDto wrapNullResponse(String message, HttpStatus status) {
	    ResponseDto<T> response = new ResponseDto<>();
	    response.setResponseCode(status.value());
	    response.setMessage(message);
	    response.setData(null);
	    response.setError(null);
		response.setTimeStamp(LocalDateTime.now());
	    return response;
	}
	
	public <T extends GenericDto>IResponseDto wrapResponse(String obj, String message) {
	    ResponseStringDto<T> response = new ResponseStringDto<>();
	    response.setResponseCode(HttpStatus.OK.value());
	    response.setMessage(message);
	    response.setData(obj);
	    response.setError(null);
	    response.setTimeStamp(LocalDateTime.now());
	    return response;
	}
	
	public <T extends GenericDto> IResponseDto wrapFileTypeResponse(File file, String message) {
		FileTypeResponseDto<T> response = new FileTypeResponseDto<>();
	    response.setResponseCode(HttpStatus.OK.value());
	    response.setMessage(message);
	    response.setData(file);
	    response.setError(null);
		response.setTimeStamp(LocalDateTime.now());
	    return response;
	}
	
    
	public IResponseDto wrapByteArrayResourceResponse(ByteArrayResource resource, String message) {
	    ResponseStringDto2 response = new ResponseStringDto2();
	    response.setResponseCode(HttpStatus.OK.value());
	    response.setMessage(message);
	    
	    // Convert ByteArrayResource to String
	    String data = new String(resource.getByteArray(), StandardCharsets.UTF_8);
	    response.setData(data);
	    
	    response.setError(null);
	    response.setTimeStamp(LocalDateTime.now());
	    
	    return response;
	}
	
	public IResponseDto wrapByteArrayResourceResponseAsBase64(ByteArrayResource resource, String message) {
	    ResponseStringDto2 response = new ResponseStringDto2();
	    response.setResponseCode(HttpStatus.OK.value());
	    response.setMessage(message);
	    
	    // Convert ByteArrayResource to Base64 String
	    String base64Data = Base64.getEncoder().encodeToString(resource.getByteArray());
	    response.setData(base64Data);
	    
	    response.setError(null);
	    response.setTimeStamp(LocalDateTime.now());
	    
	    return response;
	}

	
	public IResponseDto wrapResponseListOfString(List<String> obj, String message) {
	    ResponseListStringDto<GenericDto> response = new ResponseListStringDto<>();

	    response.setResponseCode(HttpStatus.OK.value());
	    response.setMessage(message);
	    response.setData(obj);
	    response.setError(null);
	    response.setTimeStamp(LocalDateTime.now());
	    return response;
	}

}
