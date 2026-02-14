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
public class AppLeaderBoardDTO {//extends GenericDto{
	@JsonIgnore
	private Long id;
//    private Long srno;
    @JsonIgnore
    private Integer rank;
    private Integer allIndiaRank;
    private List<RankChildrenDto> rankChildren;
    
}
	

