package com.hpy.ops360.atmservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeparatedIndexOfUpDawnAtmForCmResponse {
	private List<IndexOfUpDawnAtmForCmResponse> upAtms;
	private List<IndexOfUpDawnAtmForCmResponse> downAtms;
}
