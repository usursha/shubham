package com.hpy.uam.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkUserRequestDto {
	
	@NotEmpty(message="base64String cannot be blank")
	private String base64String;
	
	@NotEmpty(message="filename cannot be blank")
	private String filename;

}
