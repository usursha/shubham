package com.hpy.mappingservice.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Base64FileRequestDto {
	
	private String base64FileString;
	
	private String fileName;

}
