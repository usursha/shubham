package com.hpy.oauth.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

	@NotEmpty(message="username cannot be blank!!")
	private String username;

	@NotEmpty(message="password cannot be blank!!")
	private String password;
	
	@NotEmpty(message="token cannot be blank!!")
	private String token;

}