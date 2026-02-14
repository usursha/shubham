package com.hpy.oauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
	private String firstName; 
	private String lastName;
	private String email;
	private String LoggedInUsername;
}
