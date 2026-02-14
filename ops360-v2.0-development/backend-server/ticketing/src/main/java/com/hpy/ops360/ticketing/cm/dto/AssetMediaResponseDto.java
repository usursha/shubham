package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import lombok.Data;

@Data
public class AssetMediaResponseDto {
	private String atmId;
	private String ticketId;
	private List<String> imagesUrl;
}
