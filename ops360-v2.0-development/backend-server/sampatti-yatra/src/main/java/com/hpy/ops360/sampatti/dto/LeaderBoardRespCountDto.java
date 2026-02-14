package com.hpy.ops360.sampatti.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderBoardRespCountDto extends GenericDto {

	@JsonIgnore
	private Long id;

	private int totalcounts;

	private List<FilterdataList> filterdata;

	public LeaderBoardRespCountDto(int totalcounts, List<FilterdataList> filterdata) {
		super();
		this.totalcounts = totalcounts;
		this.filterdata = filterdata;
	}
	
	

}
