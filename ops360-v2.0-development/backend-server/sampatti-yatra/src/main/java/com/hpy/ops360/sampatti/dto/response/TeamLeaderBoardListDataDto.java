package com.hpy.ops360.sampatti.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.sampatti.entity.CeAgainstCmEntity;
import com.hpy.ops360.sampatti.entity.SortFilterData;
import com.hpy.ops360.sampatti.entity.UserIncentiveMonthYearEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class TeamLeaderBoardListDataDto extends GenericDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1148344745644028463L;
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
	
	private List<UserIncentiveMonthYearEntity> monthYearList;
	
	List<CeAgainstCmEntity> ceList;

	public TeamLeaderBoardListDataDto(List<UserIncentiveMonthYearEntity> monthYearList, List<CeAgainstCmEntity> ceList) {
		super();
		this.monthYearList = monthYearList;
		this.ceList= ceList;
	}
	
	

}
