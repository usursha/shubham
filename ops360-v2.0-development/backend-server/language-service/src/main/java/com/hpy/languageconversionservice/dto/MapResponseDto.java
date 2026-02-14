package com.hpy.languageconversionservice.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapResponseDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
	private Map<String, String> map=new HashMap<>();

}
