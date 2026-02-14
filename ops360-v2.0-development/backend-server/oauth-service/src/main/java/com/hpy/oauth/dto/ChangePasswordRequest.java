package com.hpy.oauth.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChangePasswordRequest {
	@NotEmpty(message = "newPassword cannot be blank!!")
	@Length(min = 8, max = 20)
	private String newPassword;

	@NotEmpty(message = "confirmPassword cannot be blank!!")
	@Length(min = 8, max = 20)
	private String confirmPassword;
}
