package com.hpy.uam.dto;

import lombok.AllArgsConstructor;

//import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

	
	private String accessToken;

	private AggUserRepresentationRequestDto representation;

}
