package com.hpy.oauth.exceptions;

import java.util.concurrent.TimeoutException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeOutException extends TimeoutException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;

}
