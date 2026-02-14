package com.hpy.rest.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hpy.rest.response.ApiErrorResponse;
import com.hpy.rest.response.ApiValidationErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .responseCode(HttpStatus.NOT_FOUND.value())
                .error("Resource not found")
                .message(ex.getMessage())
                .errorNumber(1001)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
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
                .errorNumber(1002)
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

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
               .responseCode(HttpStatus.BAD_REQUEST.value())
                .error("Bad request")
                .message(ex.getMessage())
                .errorNumber(1003)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflictException(ConflictException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .responseCode(HttpStatus.CONFLICT.value())
                .error("Resource Conflict")
                .message(ex.getMessage())
                .errorNumber(1004)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiErrorResponse> handleForbiddenException(ForbiddenException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .responseCode(HttpStatus.FORBIDDEN.value())
                .error("Forbidden")
                .message(ex.getMessage())
                .errorNumber(1005)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ApiErrorResponse> handleInternalServerErrorException(InternalServerErrorException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .errorNumber(1006)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiErrorResponse);
//        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleInternalServerError(Exception ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .errorNumber(1006)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        		.responseCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                .error("Method Not Allowed")
                .message(ex.getMessage())
                .errorNumber(1007)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ApiErrorResponse> handleUnprocessableEntityException(UnprocessableEntityException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        		.responseCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error("Unprocessable Entity")
                .message(ex.getMessage())
                .errorNumber(1007)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }
    
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleDataNotFoundException(DataNotFoundException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .responseCode(HttpStatus.NOT_FOUND.value())
                .error("Data Not Found")
                .message(ex.getMessage())
                .errorNumber(1008)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ApiErrorResponse> handleDatabaseException(DatabaseException ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        		.responseCode(HttpStatus.SERVICE_UNAVAILABLE.value())
                .error("Database Error")
                .message(ex.getMessage())
                .errorNumber(1009)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }
    
    @ExceptionHandler(RequestMismatchExceptionDto.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(RequestMismatchExceptionDto ex) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
               .responseCode(HttpStatus.BAD_REQUEST.value())
                .error("sorting order is not correct!!")
                .message(ex.getMessage())
                .errorNumber(1010)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiErrorResponse);
    }

}