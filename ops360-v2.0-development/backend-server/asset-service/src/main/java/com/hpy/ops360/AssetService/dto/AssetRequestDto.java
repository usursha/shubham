package com.hpy.ops360.AssetService.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetRequestDto {

	@JsonIgnore
	private String assetId;
	
	private String atmId;

	private String parentId;

	private String parentType;

	private String mediaType;
	@JsonIgnore
	private String docPath;

	private String fileName;
	@Transient
	private String base64String;
}
