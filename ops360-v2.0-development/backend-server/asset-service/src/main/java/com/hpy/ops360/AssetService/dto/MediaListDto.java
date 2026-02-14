package com.hpy.ops360.AssetService.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
	
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaListDto {
	
	@NotEmpty(message = "mediaList cannot be null!!")
	private List<DtoAssetRequestWithMultipleFiles> mediaList;

}
