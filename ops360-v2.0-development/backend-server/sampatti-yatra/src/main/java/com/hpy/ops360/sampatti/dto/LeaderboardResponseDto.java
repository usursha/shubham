package com.hpy.ops360.sampatti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardResponseDto {
	private Long srno;
    private String fullName;
    private String loginId;
    private Integer target;
    private Integer achieved;
    private Integer differenceTarget;
    private String incentiveAmount;
    private String consistencyAmount;
    private String reward;
    private String location;
    private String profileLocation;
    private String reportsTo;
    private String nationalHead;
    private String zonalHead;
    private String stateHead;
    private String channelManager;
    private String profilePic;
}
