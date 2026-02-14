package com.hpy.ops360.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ProfilePictureDto extends GenericDto{
	

	@JsonIgnore
	private Long id;
	
	@JsonIgnore
	private String parentId;
	
	@NotEmpty(message="parentType cannot be blank")
	private String parentType;
	
	@NotEmpty(message="mediaType cannot be blank")
	private String mediaType;
	
	@JsonIgnore
	private String docPath;
	
	@NotEmpty(message="fileName cannot be blank")
	private String fileName;
	
	@Transient
	@NotEmpty(message="base64String cannot be blank")
	private String base64String;

}

