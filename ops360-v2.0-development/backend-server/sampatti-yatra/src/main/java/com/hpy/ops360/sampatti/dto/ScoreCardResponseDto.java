package com.hpy.ops360.sampatti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreCardResponseDto {
	private Long srno;
    private Long atmceoMonthlyIncentiveId;
    private String monthDate;
    private String userLoginId;
    private String target;
    private String achieved;
    private Integer rank;
    private String incentiveAmount;
    private Integer roleId;
    private String rankImagePath;
    private String updatedDate;
}
