package com.hpy.ops360.sampatti.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LeaderBoardIncentiveDownloadEntity {
	
	@Id
	@JsonIgnore
	private Long srno;
	
	private Integer rank;
    private String fullName;
    private String role;
    private Integer target;
    private Integer achieved;
    @Column(name="Overage/Shortage")
    private Integer overageShortage;
    @Column(name="reward")
    private Integer incentiveAmount; //rewarda
    private String channelManager;
    @Column(name="stateHead")
    private String stateChannelManager;
    private String zonalHead;
	
    public LeaderBoardIncentiveDownloadEntity(Integer rank, String fullName, String role, Integer target,
			Integer achieved, Integer overageShortage, Integer incentiveAmount, String channelManager,
			String stateChannelManager, String zonalHead) {
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
	
    
	
	
	
	
	
	
	/*
	 * private String fullName; private String userLoginId; private String role;
	 * private String zone; private Integer target; private Integer achieved;
	 * 
	 * @Column(name="Overage/Shortage") private Integer overageShortage; private
	 * Integer rank; private Integer incentiveAmount; private Integer
	 * consistencyAmount; private Integer reward;
	 */


}
