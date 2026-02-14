package com.hpy.ops360.AssetService.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsernameRequestDto {
	
	@NotEmpty(message="username cannot be blank!!")
	private String username;

}