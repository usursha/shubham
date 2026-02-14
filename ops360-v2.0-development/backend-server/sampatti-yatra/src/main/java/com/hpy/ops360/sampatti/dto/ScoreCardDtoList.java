package com.hpy.ops360.sampatti.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.sampatti.entity.AppScoreCardEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreCardDtoList extends GenericDto{
	
	@JsonIgnore
	private Long id;
	private List<AppScoreCardEntity> scorecardlist;

}