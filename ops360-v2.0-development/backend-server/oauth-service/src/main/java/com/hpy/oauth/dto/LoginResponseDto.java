package com.hpy.oauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;

//import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginResponseDto extends GenericDto {

	
	@JsonIgnore
    private Long id;
	private String accessToken;
	private String refreshToken;
	private AggregateUserRepresentationDto representation;

}
