package com.hpy.oauth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ForgotPasswordDto {

	
	@NotNull
	private String userName;
	
	
	@NotNull
	private String userMobileNumber;
	
	
	@NotNull
	private String employeeCode;

}
