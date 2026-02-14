package com.hpy.uam.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {

	@NotEmpty(message="username cannot be blank!!")
	private String username;
	
	@Email
	@NotEmpty(message="email cannot be blank!!")
	private String email;

}
