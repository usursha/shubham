package com.hpy.ops360.ticketing.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

	
	private String username;

	@NotEmpty
	private String password;
	
	//authenticator otp
//	private String token;

}