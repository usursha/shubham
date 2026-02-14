package com.hpy.ops360.ticketing.cm.dto;

import jakarta.validation.constraints.NotEmpty;


import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaFilesDto {
	
	@NotEmpty(message = "filename cannot be null!!")
	private String filename;
	
	@Transient
    private String base64;

}
