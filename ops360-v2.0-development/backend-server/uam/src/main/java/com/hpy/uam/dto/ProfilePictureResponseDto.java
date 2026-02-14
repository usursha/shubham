package com.hpy.uam.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProfilePictureResponseDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
	@JsonIgnore
	protected String createdBy;
	@JsonIgnore
	protected LocalDateTime createdAt;
	@JsonIgnore
	protected String modifiedBy;
	@JsonIgnore
	protected LocalDateTime modifiedAt;

	@NotEmpty(message="imageId cannot be blank!!")
	private String imageId;


}
