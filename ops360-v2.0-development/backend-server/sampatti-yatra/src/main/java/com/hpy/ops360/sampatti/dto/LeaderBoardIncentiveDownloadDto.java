package com.hpy.ops360.sampatti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderBoardIncentiveDownloadDto {
	
	@JsonIgnore
	private Long srno;
	
	private Integer rank;
    private String fullName;
    private String role;
    private Integer target;
    private Integer achieved;
    private Integer overageShortage;
    private Integer incentiveAmount; //rewarda
    private String channelManager;
    private String stateChannelManager;
    private String zonalHead;
	
    public LeaderBoardIncentiveDownloadDto(Integer rank, String fullName, String role, Integer target, Integer achieved,
			Integer overageShortage, Integer incentiveAmount, String channelManager, String stateChannelManager,
			String zonalHead) {
		super();
		this.rank = rank;
		this.fullName = fullName;
		this.role = role;
		this.target = target;
		this.achieved = achieved;
		this.overageShortage = overageShortage;
		this.incentiveAmount = incentiveAmount;
		this.channelManager = channelManager;
		this.stateChannelManager = stateChannelManager;
		this.zonalHead = zonalHead;
	}
	 
	
}
