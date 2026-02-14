package com.hpy.oauth.exceptions;

import java.util.InputMismatchException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordPatternMismatchException extends InputMismatchException{
	
	private String message;

}
