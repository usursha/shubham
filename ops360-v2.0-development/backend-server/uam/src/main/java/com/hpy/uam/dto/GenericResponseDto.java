package com.hpy.uam.dto;

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
	
	Map<String, String> map=new HashMap<>();

	public void put(String fieldname, String message) {
		// TODO Auto-generated method stub
		
	}

}
