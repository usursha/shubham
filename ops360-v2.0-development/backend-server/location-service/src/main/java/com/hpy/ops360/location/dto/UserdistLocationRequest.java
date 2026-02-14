package com.hpy.ops360.location.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserdistLocationRequest {
	private String username;
	private String created_on;
}