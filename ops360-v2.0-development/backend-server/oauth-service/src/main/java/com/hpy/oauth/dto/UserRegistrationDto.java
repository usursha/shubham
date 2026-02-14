package com.hpy.oauth.dto;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

	
	@NotEmpty(message="name must be atleast 4 characters long") 
	private String username;
	
	@Email
	@NotEmpty(message="email cannot be leave blank!!")
	private String email;

	@NotEmpty
	private String firstName;
	
	@NotEmpty
	private String lastName;
	
	@NotEmpty
	private Map<String, List<String>> attributes;

}
