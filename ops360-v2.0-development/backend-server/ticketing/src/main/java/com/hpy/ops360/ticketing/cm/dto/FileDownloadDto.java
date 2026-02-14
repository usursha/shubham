package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDownloadDto extends GenericDto {
//	byte[] data;
//	String fileName;
	
	@JsonIgnore
	private Long id;
	private String fileName;
	private String data;
	
	@Builder
	public FileDownloadDto(String fileName, String data) {
		
		this.fileName = fileName;
		this.data = data;
	}
	
	

}
