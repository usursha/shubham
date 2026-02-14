package com.hpy.mappingservice.response.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemporaryExecutiveDto {
	private String temporaryCE;
	private Integer mappedCount;
	private List<String> mappedatmCodes;
}