package com.hpy.ops360.sampatti.dto;

import java.util.List;
import java.util.Map;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMapperDto extends GenericDto{
	
	private Map<Integer, List<AppLeaderBoardDTO>> data;

}
