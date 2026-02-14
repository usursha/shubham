package com.hpy.oauth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordDto {
	@NotEmpty
	private String newPassword;
	@NotEmpty
	private String confirmPassword;
}
