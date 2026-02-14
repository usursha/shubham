package com.hpy.uam.dto;


import org.hibernate.validator.constraints.Length;

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
	
	@NotEmpty(message="username cannot be blank!!")
	@Length(min=8, max=20)
	private String password;

}