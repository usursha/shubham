package com.hpy.uam.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Roles {

	@NotNull
	@NotEmpty
	private String roleName;

	@NotNull
	@NotEmpty
	private String description;
}
