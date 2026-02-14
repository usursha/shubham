package com.hpy.uam.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePictureRequestDto {

	private String username;

	@NotEmpty(message = "mediaType cannot be blank")
	private String mediaType;

	@JsonIgnore
	private String docPath;

	@NotEmpty(message = "fileName cannot be blank")
	private String fileName;

	@Transient
	@NotEmpty(message = "base64String cannot be blank")
	private String base64String;

}
