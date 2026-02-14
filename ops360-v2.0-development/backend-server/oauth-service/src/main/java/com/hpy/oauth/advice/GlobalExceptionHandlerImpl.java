package com.hpy.oauth.advice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

import org.apache.hc.client5.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hpy.oauth.exceptions.CustomException;
import com.hpy.oauth.exceptions.PasswordPatternMismatchException;
import com.hpy.oauth.exceptions.TimeOutException;
import com.hpy.rest.exception.ForbiddenException;
import com.hpy.rest.exception.ResourceNotFoundException;
import com.hpy.rest.response.ApiErrorResponse;
import com.hpy.rest.response.ApiValidationErrorResponse;

import jakarta.ws.rs.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandlerImpl {//extends GlobalExceptionHandler{
	
	@ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        		.responseCode(HttpStatus.UNAUTHORIZED.value())
        		.error("UNAUTHORISED ACCESS")
                .message("Your session is already active. Please logout from the other device.")
                .errorNumber(4001)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }
	
	@ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ApiErrorResponse> handleWebClientResponseException(WebClientResponseException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        		.responseCode(HttpStatus.UNAUTHORIZED.value())
        		.error("UNAUTHORISED ACCESS")
                .message("invalid credentials!!")
                .errorNumber(4002)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }
	
	@ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<ApiErrorResponse> handleJWTDecodeException(JWTDecodeException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Token doesn't have a valid JSON format.")
                .errorNumber(4003)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }
	
	@ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiErrorResponse> handleForbiddenException(ForbiddenException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .responseCode(HttpStatus.FORBIDDEN.value())
                .error(ex.getMessage())
                .message("Invalid Access!!")
                .errorNumber(4004)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }
	
	@ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .responseCode(HttpStatus.NOT_FOUND.value())
                .error(ex.getMessage())
                .message("Resource not found!!")
                .errorNumber(4005)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handlerHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .responseCode(HttpStatus.BAD_REQUEST.value())
                .error(ex.getMessage())
                .message("Given input is invalid. Please provide valid input")
                .errorNumber(4006)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }

	@ExceptionHandler(InputMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleInputMismatchException(InputMismatchException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        		.responseCode(HttpStatus.BAD_REQUEST.value())
        		.error("UNAUTHORISED ACCESS")
                .message("confirm password and new password did not match!!!")
                .errorNumber(4007)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }
	
	@ExceptionHandler(PasswordPatternMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handlePasswordPatternMismatchException(PasswordPatternMismatchException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        		.responseCode(HttpStatus.BAD_REQUEST.value())
        		.error(ex.getMessage())
                .message("New Password doesn't meet criteria!!!")
                .errorNumber(4008)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }
    
	@ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseEntity<ApiErrorResponse> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        		.responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        		.error(ex.getMessage())
                .message("Username is not associated with any group")
                .errorNumber(4009)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }

	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        Map<String, List<String>> errors = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String fieldName = fieldError.getField();
            String errorMessage = getBriefErrorMessage(fieldError); // Call a separate method to get a brief error message
            errors.computeIfAbsent(fieldName, key -> new ArrayList<>()).add(errorMessage);
        }
        ApiValidationErrorResponse apiValidationErrorResponse = ApiValidationErrorResponse.builder()
                .responseCode(HttpStatus.BAD_REQUEST.value())
                .error("Validation failed")
                .message("Validation error(s) occurred")
                .errorNumber(4010)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiValidationErrorResponse);
    }
	
	private String getBriefErrorMessage(FieldError fieldError) {
        String defaultMessage = fieldError.getDefaultMessage();
        String[] parts = defaultMessage.split("\\{");
        return parts[0].trim();
    }
	
	@ExceptionHandler(TimeOutException.class)
    public ResponseEntity<ApiErrorResponse> handleTimeOutException(TimeOutException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        		.responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        		.error(ex.getMessage())
                .message("link has been expired, kindly request for new link..")
                .errorNumber(4011)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }
    
	@ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        		.responseCode(HttpStatus.BAD_REQUEST.value())
        		.error(ex.getMessage())
                .message("Invalid Credentials! Please Give Correct Credentials")
                .errorNumber(4012)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }

	@ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiErrorResponse> handleTokenExpiredException(TokenExpiredException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        		.responseCode(HttpStatus.UNAUTHORIZED.value())
        		.error(ex.getMessage())
                .message("Token Expired!! Please Login Again.")
                .errorNumber(4013)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        		.responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        		.error(ex.getMessage())
                .message("User is not associated with any group")
                .errorNumber(4014)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
	}
	
	@ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<ApiErrorResponse> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        		.responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        		.error(ex.getMessage())
                .message("User is not registered!!")
                .errorNumber(4015)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
	}
	
	@ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(CustomException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        		.responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        		.error(ex.getMessage())
                .message("email-id is not registered!!")
                .errorNumber(4016)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
	}
		
//	@ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<ApiErrorResponse> handleRuntimeException(ResponseStatusException ex) {
//        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
//        		.responseCode(HttpStatus.UNAUTHORIZED.value())
//        		.error("UNAUTHORISED ACCESS")
//                .message("Your session is already active. Please logout from the other device.")
//                .errorNumber(1102)
//                .timestamp(LocalDateTime.now())
//                .build();
//        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
//    }
//	
//	@ExceptionHandler(JWTDecodeException.class)
//    public ResponseEntity<ApiErrorResponse> handleInternalServerError(JWTDecodeException ex) {
//        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
//                .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .error("Internal Server Error")
//                .message("Token doesn't have a valid JSON format.")
//                .errorNumber(1006)
//                .timestamp(LocalDateTime.now())
//                .build();
//        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
//    }
//	
//	
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ResponseDto<GenericDto>> methodArgumentNotValidExceptionHandlerEntity(MethodArgumentNotValidException exception) {
//        Map<String, String> response = new HashMap<>();
//        exception.getBindingResult().getAllErrors().forEach(error -> {
//            String fieldName = ((FieldError) error).getField();
//            String message = error.getDefaultMessage();
//            response.put(fieldName, message);
//        });
//        ResponseDto<GenericDto> responseDto = new ResponseDto<>();
//        responseDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
//        responseDto.setMessage("Validation errors");
//        responseDto.setData(null);
//        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
//    }
//	
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
//
//    @ExceptionHandler(ForbiddenException.class)
//    public ResponseEntity<ResponseDto<GenericDto>> handleForbiddenException(ForbiddenException ex) {
//    	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
//        responseDto.setResponseCode(HttpStatus.FORBIDDEN.value());
//        responseDto.setMessage("Invalid Access!!");
//        responseDto.setData(null);
//        return new ResponseEntity<>( HttpStatus.OK);
//    }
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
//    @ExceptionHandler(InputMismatchException.class)
//    public ResponseEntity<ResponseDto<GenericDto>> handleInputMismatchException(InputMismatchException ex) {
//    	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
//        responseDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
//        responseDto.setMessage("New Password doesn't meet criteria!!!");
//        responseDto.setData(null);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
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
//    @ExceptionHandler(TokenExpiredException.class)
//    public ResponseEntity<ResponseDto<GenericDto>> handleTokenExpiredException(TokenExpiredException ex) {
//    	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
//        responseDto.setResponseCode(HttpStatus.UNAUTHORIZED.value());
//        responseDto.setMessage("Token Expired!! Please Login Again.");
//        responseDto.setData(null);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//
////    @ExceptionHandler(Exception.class)
////    public ResponseEntity<ResponseDto<GenericDto>> handleGenericException(Exception ex) {
////    	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
////        responseDto.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
////        responseDto.setMessage(ResponseMessageUtil.getMessageForStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
////        responseDto.setData(null);
////        return new ResponseEntity<>(responseDto, HttpStatus.OK);
////    }
//    
//
//    
//    @ExceptionHandler(IndexOutOfBoundsException.class)
//    public ResponseEntity<ResponseDto<GenericDto>> handleGenericException(IndexOutOfBoundsException ex) {
//    	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
//        responseDto.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
////        responseDto.setMessage(ResponseMessageUtil.getMessageForStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
//        responseDto.setMessage("User is not associated with any group");
//        responseDto.setData(null);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//    
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ResponseDto<GenericDto>> handleGenericException(Exception ex) {
//    	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
//        responseDto.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        responseDto.setMessage(ex.getMessage());
//        responseDto.setData(null);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }
//    
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ResponseDto<GenericDto>> handleGenericException(ResourceNotFoundException ex) {
//  	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
//      responseDto.setResponseCode(1010);
//      responseDto.setMessage("User does not have permission to login");
//      responseDto.setData(null);
//      return new ResponseEntity<>(responseDto, HttpStatus.OK);
//  }
//    
//    
//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<ResponseDto<GenericDto>> handleResponseStatusException(ResponseStatusException ex) {
//  	ResponseDto<GenericDto> responseDto = new ResponseDto<>();
//      responseDto.setResponseCode(HttpStatus.CONFLICT.value());
//      responseDto.setMessage("User already has an active session. Please logout from other devices first.");
//      responseDto.setData(null);
//      return new ResponseEntity<>(responseDto, HttpStatus.OK);
//  }
    
    

}
