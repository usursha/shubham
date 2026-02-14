package com.hpy.ops360.AssetService.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileTypeResponseDto {
	
	@JsonProperty("responseCode")
	private int responseCode;
	@JsonProperty("message")
	private String message;
	@JsonProperty("data")
	private ImageResponseDto data;
	@JsonProperty("error")
	private String error;
	@JsonProperty("errorNumber")
	private int errorNumber;
	@JsonProperty("timestamp")
	private LocalDateTime timestamp;

}
