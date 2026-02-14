package com.hpy.ops360.dto.response;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPromptResponse extends GenericDto{

	private String eventCode;
	private int promptNo;
	private String promptDescription;

}
