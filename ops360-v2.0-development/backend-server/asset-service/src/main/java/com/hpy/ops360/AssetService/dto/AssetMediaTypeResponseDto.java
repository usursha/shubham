package com.hpy.ops360.AssetService.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssetMediaTypeResponseDto extends GenericDto {
	
	@JsonIgnore
	private Long id;
	
	private String atmId;
	private String ticketId;
	private List<String> imagesUrl;
	

}
