package com.hpy.ops360.AssetService.dto;

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
public class GenericResponseDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
	private Integer responseCode;
	private String message;
	
	Map<String, String> error=new HashMap<>();
	public void put(String fieldname, String message) {
	}

}
