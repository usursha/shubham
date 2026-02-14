package com.hpy.ops360.AssetService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaRequestDto {
	
	private String atmId;
	private String ticketId;
	private String fileId;

}
