package com.hpy.ops360.sampatti.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class LeaderboardEntity {

	@Id
	private Long srno;
	
    private Integer allIndiaRank;
    private String fullName;
    private String loginId;
    private Integer target;
    private Integer achieved;
    private Integer differenceTarget;
    private Integer rewardValue;
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

}
