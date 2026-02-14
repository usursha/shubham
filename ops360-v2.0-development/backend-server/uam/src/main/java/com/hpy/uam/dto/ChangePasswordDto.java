package com.hpy.uam.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ChangePasswordDto {

	@NotNull
	@NotEmpty(message="userName cannot be blank")
	private String userName;

	@NotEmpty(message="currentPassword cannot be blank")
	@Length(min=8, max=20)
	private String currentPassword;
	
	@NotEmpty(message="newPassword cannot be blank!!")
	@Length(min=8, max=20)
	private String newPassword;
	
	@NotEmpty(message="confirmPassword cannot be blank!!")
	@Length(min=8, max=20)
	private String confirmPassword;

}
