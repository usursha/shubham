package com.hpy.uam.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwoFactorAuthSecretDataDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
	@NotEmpty(message="encodedTotpSecret cannot be blank!!")
	private String encodedTotpSecret;
	@NotEmpty(message="totpSecretQRCode cannot be blank!!")
    private String totpSecretQRCode;

}
