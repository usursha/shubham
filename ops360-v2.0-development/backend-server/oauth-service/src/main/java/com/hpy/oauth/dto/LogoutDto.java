package com.hpy.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoutDto {
	
	private String clientId;
	private String clientSecret;
	private String refreshToke;

}
