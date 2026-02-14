package com.hpy.ops360.AssetService.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetMediaTypeRequestDto {

	@NotEmpty(message = "atmId cannot be null!!")
	private String atmId;
	@NotEmpty(message = "parentId cannot be null!!")
	private String parentId;
	@NotEmpty(message = "parentType cannot be null!!")
	private String parentType;
	@NotEmpty(message = "mediaType cannot be null!!")
	private String mediaType;
	@JsonIgnore
	private String docPath;
	@NotEmpty(message = "fileName cannot be null!!")
	private String fileName;
	@Transient
	private String base64String;

}
