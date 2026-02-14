package com.hpy.uam.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwoFactorAuthSubmissionDto {
	
	@NotEmpty(message="deviceName cannot be blank!!")
	private String deviceName;
	@NotEmpty(message="encodedTotpSecret cannot be blank!!")
    private String encodedTotpSecret;
	@NotEmpty(message="totpInitialCode cannot be blank!!")
    private String totpInitialCode;
	@NotEmpty(message="overwrite cannot be blank!!")
    private boolean overwrite;

}
