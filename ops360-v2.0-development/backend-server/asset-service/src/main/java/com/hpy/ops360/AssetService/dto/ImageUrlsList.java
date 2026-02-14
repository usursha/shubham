package com.hpy.ops360.AssetService.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.Data;

@Data
public class ImageUrlsList extends GenericDto{
	
	@JsonIgnore
	private Long id;
	private List<String> imageurls;

}
