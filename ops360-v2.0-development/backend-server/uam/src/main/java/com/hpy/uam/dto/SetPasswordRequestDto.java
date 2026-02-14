package com.hpy.uam.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetPasswordRequestDto {
	
	@NotEmpty(message="Username cannot be blank!!")
	private String username;


}
