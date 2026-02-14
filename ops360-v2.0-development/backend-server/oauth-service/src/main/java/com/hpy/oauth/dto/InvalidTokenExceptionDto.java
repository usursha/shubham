package com.hpy.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvalidTokenExceptionDto extends RuntimeException{

	
	private static final long serialVersionUID = 1L;
	private String message;
	private int code;
}
