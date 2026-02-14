package com.hpy.ops360.sampatti.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamLeaderboardDetailsReqDto {
	
	private String username;
	private String paramMonthYear;
	private Integer sortTypeId;
	private Integer targetAchievedMin;
	private Integer targetAchievedMax;

}
