package com.hpy.ops360.AssetService.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

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
	
	private String imageId;


}
