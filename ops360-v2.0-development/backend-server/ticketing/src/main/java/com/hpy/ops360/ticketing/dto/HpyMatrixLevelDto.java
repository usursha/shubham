package com.hpy.ops360.ticketing.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HpyMatrixLevelDto {

	private Integer level;
	private List<HpyMatrixDto> contacts;
}
