package com.hpy.oauth.dto;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoutResponseDto  extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
	private int code;
	private String message;

}
