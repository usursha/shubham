package com.hpy.ops360.sampatti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadBoardDetailsDto {
    private Long atmceoMonthlyIncentiveId;
    private String monthDate;
    private String userLoginId;
    private Double target;
    private Double achieved;
    private Integer rank;
    private Double incentiveAmount;
    private Integer roleId;
    private String city;
    private String roleCode;
    private String rankImagePath;
}
