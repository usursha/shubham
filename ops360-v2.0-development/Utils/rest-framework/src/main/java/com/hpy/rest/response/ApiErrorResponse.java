package com.hpy.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.hpy.generic.impl.GenericDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse<T extends GenericDto> {
	private Integer responseCode;
	private String message;
	private T data;
    private String error;
    private int errorNumber;
    private LocalDateTime timestamp;
}
