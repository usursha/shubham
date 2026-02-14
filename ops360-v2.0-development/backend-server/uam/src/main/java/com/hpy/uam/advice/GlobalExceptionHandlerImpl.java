package com.hpy.uam.advice;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hpy.rest.exception.GlobalExceptionHandler;
import com.hpy.rest.response.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandlerImpl extends GlobalExceptionHandler {

//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandlerEntity(MethodArgumentNotValidException exception){
//		Map<String, String> response=new HashMap<>();
//		exception.getBindingResult().getAllErrors().forEach(error->{
//			String fieldname=((FieldError)error).getField();
//			String message=error.getDefaultMessage();
//			response.put(fieldname,message);
//		});
//		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//	}
//
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<ResponseDto<GenericResponseDto>> methodArgumentNotValidExceptionHandlerEntity(MethodArgumentNotValidException exception){
//		GenericResponseDto data=new GenericResponseDto();
//		exception.getBindingResult().getAllErrors().forEach(error->{
//			String fieldname=((FieldError)error).getField();
//			String message=error.getDefaultMessage();
//			data.getMap().put(fieldname,message);
//		});
//		ResponseDto<GenericResponseDto> response=new ResponseDto() {};
//		response.setResponseCode(HttpStatus.BAD_REQUEST.value());
//		response.setMessage("Invalid Data!!");
//		response.setData(data);
//		return ResponseEntity.ok(response);
//	}
//	
//	
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ResponseDto<GenericDto>> handleNotFoundException(NotFoundException ex) {
//    	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
//        responseDto.setResponseCode(HttpStatus.NOT_FOUND.value());
//        responseDto.setMessage("Resource not found!!");
//        responseDto.setData(null);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<ResponseDto<GenericDto>> handlerHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
//    	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
//        responseDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
//        responseDto.setMessage("Given input is invalid. Please provide valid input");
//        responseDto.setData(null);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//
//    @ExceptionHandler(WebClientResponseException.class)
//    public ResponseEntity<ResponseDto<GenericDto>> handleWebClientResponseException(WebClientResponseException ex) {
//    	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
//        responseDto.setResponseCode(HttpStatus.UNAUTHORIZED.value());
//        responseDto.setMessage("Username or Password is not valid!!");
//        responseDto.setData(null);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//
//    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
//    public ResponseEntity<ResponseDto<GenericDto>> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException ex) {
//    	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
//        responseDto.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        responseDto.setMessage("Username is not associated with any group");
//        responseDto.setData(null);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//
////    @ExceptionHandler(InputMismatchException.class)
////    public ResponseEntity<ResponseDto<GenericDto>> handleInputMismatchException(InputMismatchException ex) {
////    	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
////        responseDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
////        responseDto.setMessage("New Password doesn't meet criteria!!!");
////        responseDto.setData(null);
////        return new ResponseEntity<>(responseDto, HttpStatus.OK);
////    }
//
//    @ExceptionHandler(InvalidCredentialsException.class)
//    public ResponseEntity<ResponseDto<GenericDto>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
//    	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
//        responseDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
//        responseDto.setMessage("Invalid Credentials! Please Give Correct Credentials");
//        responseDto.setData(null);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ResponseDto<GenericDto>> handleGenericException(RuntimeException ex) {
//    	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
//        responseDto.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        responseDto.setMessage(ex.getMessage());
//        responseDto.setData(null);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
	
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleForbiddenException(AccessDeniedException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .responseCode(HttpStatus.FORBIDDEN.value())
                .error("Access Denied")
                .message(ex.getMessage())
                .errorNumber(1005)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }
}
