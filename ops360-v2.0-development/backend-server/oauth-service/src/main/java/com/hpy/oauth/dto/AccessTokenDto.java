package com.hpy.oauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
	private String accessToken;
//	private String expiresIn;
//	private String refreshExpiresIn;
	private String refreshToken;
//	private String tokenType;
//	private String notBeforePolicy;
//	private String sessionState;
//	private String scope;
	

}
