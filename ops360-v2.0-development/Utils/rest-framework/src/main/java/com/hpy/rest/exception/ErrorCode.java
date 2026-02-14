package com.hpy.rest.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	// System-wide error codes (1-999)
	UNEXPECTED_ERROR(1, "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
	VALIDATION_ERROR(2, "Validation failed", HttpStatus.BAD_REQUEST),

	// Authentication and Authorization errors (1000-1999)
	UNAUTHORIZED(1000, "Unauthorized access", HttpStatus.UNAUTHORIZED),
	TOKEN_EXPIRED(1001, "Token has expired", HttpStatus.UNAUTHORIZED),
	INVALID_TOKEN(1002, "Invalid token", HttpStatus.UNAUTHORIZED),

	// Resource errors (2000-2999)
	RESOURCE_NOT_FOUND(2000, "Resource not found", HttpStatus.NOT_FOUND),
	RESOURCE_ALREADY_EXISTS(2001, "Resource already exists", HttpStatus.CONFLICT),

	// Task-specific errors (10000-10999)
	TASK_NOT_FOUND(10000, "Task not found", HttpStatus.NOT_FOUND),
	INVALID_TASK_STATUS(10001, "Invalid task status", HttpStatus.BAD_REQUEST);

	// Add more error codes for other microservices as needed

	private final int code;
	private final String defaultMessage;
	private final HttpStatus httpStatus;

	ErrorCode(int code, String defaultMessage, HttpStatus httpStatus) {
		this.code = code;
		this.defaultMessage = defaultMessage;
		this.httpStatus = httpStatus;
	}

	public int getCode() {
		return code;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}