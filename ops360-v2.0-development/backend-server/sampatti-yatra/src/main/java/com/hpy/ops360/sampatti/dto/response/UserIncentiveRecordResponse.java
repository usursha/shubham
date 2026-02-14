package com.hpy.ops360.sampatti.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserIncentiveRecordResponse {

	private String year;
	private String month;
	private Integer teamRank;
	private Integer rankDifference;
	private Integer allIndiaRank;
	private Double target;
	private Double achieved;
	private Integer differenceAchieved;
	private String userLoginId;
	private String monthKey;
	private Double rewards;

}
