package com.hpy.ops360.AssetService.dto;

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

	@NotEmpty(message="imageId cannot be blank!!")
	private String imageId;


}
