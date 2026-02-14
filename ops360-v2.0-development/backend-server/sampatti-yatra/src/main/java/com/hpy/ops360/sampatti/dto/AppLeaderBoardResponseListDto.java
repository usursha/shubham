package com.hpy.ops360.sampatti.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.sampatti.entity.AppLeaderBoardEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppLeaderBoardResponseListDto extends GenericDto {
	@JsonIgnore
	private Long id;
	@JsonIgnore
	protected String createdBy;
	@JsonIgnore
	protected LocalDateTime createdAt;
	@JsonIgnore
	protected String modifiedBy;
	@JsonIgnore
	protected LocalDateTime modifiedAt;
	
	private long count;
	private List<AppLeaderBoardDTO> records;
//	private Map<Integer, List<AppLeaderBoardDTO>> data;

}
