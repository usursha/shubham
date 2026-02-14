package com.hpy.ops360.atmservice.utils;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.hpy.ops360.atmservice.dto.ResponsePagedWithMetaDto;
import com.hpy.ops360.atmservice.response.PagedResponse;
import com.hpy.ops360.atmservice.response.ResponsePagedDto;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

@Component
public class RestUtilsImpl extends RestUtils {

//	 // Method to wrap a single object response	
//	public <T extends GenericDto> IResponseDto wrapResponse(T obj, String message) {
//
//		ResponseDto<T> response = new ResponseDto<>();
//
//		response.setResponseCode(HttpStatus.OK.value());
//		response.setMessage(message);
//		response.setData(obj);
//		return response;
//
//	}
//
//	// Method to wrap a list of objects response
//	public <T extends IGenericDto> IResponseDto wrapResponse(List<T> obj, String message) {
//
//		ResponseListDto<T> response = new ResponseListDto<>();
//
//		response.setResponseCode(HttpStatus.OK.value());
//		response.setMessage(message);
//		response.setData(obj);
//		return response;
//
//	}
//	
//	 // method to wrap specifically for null object cases
//	public <T extends GenericDto> IResponseDto wrapNullResponse(String message, HttpStatus status) {
//	    ResponseDto<T> response = new ResponseDto<>();
//	    response.setResponseCode(status.value());
//	    response.setMessage(message);
//	    response.setData(null);
//	    return response;
//	}
//	
//	// Method to wrap error responses
//    public IResponseDto wrapErrorResponse(ErrorCode errorCode) {
//        return new ResponseErrorDto(errorCode);
//    }
	
	
	// CORRECT METHOD 1: Generic method for any PagedResponse
    public <T> IResponseDto wrapPagedResponse(PagedResponse<T> pagedResponse, String message) {
        ResponsePagedDto<T> response = new ResponsePagedDto<>();
        
        response.setResponseCode(HttpStatus.OK.value());
        response.setMessage(message);
        response.setData(pagedResponse);
        response.setError(null);
        response.setTimeStamp(LocalDateTime.now());
        
        return response;
    }
//    
    // CORRECT METHOD 2: Alternative with flattened structure
    public <T> IResponseDto wrapPagedResponseWithMeta(PagedResponse<T> pagedResponse, String message) {
        ResponsePagedWithMetaDto<T> response = new ResponsePagedWithMetaDto<>();
        
        response.setResponseCode(HttpStatus.OK.value());
        response.setMessage(message);
        response.setContent(pagedResponse.getContent());
        response.setPage(pagedResponse.getPage());
        response.setSize(pagedResponse.getSize());
        response.setTotalElements(pagedResponse.getTotalElements());
        response.setTotalPages(pagedResponse.getTotalPages());
        response.setFirst(pagedResponse.isFirst());
        response.setLast(pagedResponse.isLast());
        response.setEmpty(pagedResponse.isEmpty());
        response.setError(null);
        response.setTimeStamp(LocalDateTime.now());
        
        return response;
    }
}
