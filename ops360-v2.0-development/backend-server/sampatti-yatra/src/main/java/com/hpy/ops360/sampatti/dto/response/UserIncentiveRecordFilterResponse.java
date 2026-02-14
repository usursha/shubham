package com.hpy.ops360.sampatti.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserIncentiveRecordFilterResponse {

	private String year;
    private String month;
    private Integer teamRank;
    private Integer rankDifference;
    private Integer allIndiaRank;
    private Integer target;
    private Integer achieved;
    private Integer diffAchievedTarget;
    private String userLoginId;
    @JsonIgnore
    private String monthKey;
    private Double incentiveAmount;
    @JsonIgnore
    private String financialYear;
}
