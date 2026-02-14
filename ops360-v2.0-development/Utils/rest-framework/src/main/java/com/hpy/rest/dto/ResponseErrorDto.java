package com.hpy.rest.dto;

import com.hpy.generic.impl.GenericDto;
import com.hpy.rest.exception.ErrorCode;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResponseErrorDto implements IResponseDto {
    private static final long serialVersionUID = -1234567890123456789L;

    private Integer responseCode;
    private String message;
    private ErrorData data;

    @Data
    public static class ErrorData extends GenericDto {

		private static final long serialVersionUID = 1L;
		private boolean status;
        private String error;
        private String errorMessage;
        private int errorNumber;
        private LocalDateTime timestamp;

        public ErrorData(ErrorCode errorCode) {
            this.status = false;
            this.error = errorCode.name();
            this.errorMessage = errorCode.getDefaultMessage();
            this.errorNumber = errorCode.getCode();
            this.timestamp = LocalDateTime.now();
        }
    }

    public ResponseErrorDto(ErrorCode errorCode) {
        this.responseCode = errorCode.getHttpStatus().value();
        this.message = "Error";
        this.data = new ErrorData(errorCode);
    }
}