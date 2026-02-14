package com.hpy.ops360.sampatti.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.sampatti.entity.SortFilterData;
import com.hpy.ops360.sampatti.entity.TargetRange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class TeamLeaderboardFilterDataDto extends GenericDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 776985621432138716L;
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
	
	private List<SortFilterData> sortFilterData;
	private TargetRange targetRange;
	public TeamLeaderboardFilterDataDto(List<SortFilterData> sortFilterData, TargetRange targetRange) {
		super();
		this.sortFilterData = sortFilterData;
		this.targetRange = targetRange;
	}
	
	
	 

}
