package com.hpy.ops360.AssetService.dto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
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
	@NotEmpty(message = "filename cannot be null!!")
    private String base64;

}
