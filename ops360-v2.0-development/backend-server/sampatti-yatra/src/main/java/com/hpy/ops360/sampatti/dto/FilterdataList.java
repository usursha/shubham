package com.hpy.ops360.sampatti.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterdataList {

	private Integer allIndiaRank;
	private List<LeaderboardResponseDto> children;
}
