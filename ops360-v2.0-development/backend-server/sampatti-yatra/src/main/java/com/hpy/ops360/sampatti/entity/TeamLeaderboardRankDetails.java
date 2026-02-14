package com.hpy.ops360.sampatti.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamLeaderboardRankDetails {
	
	@Id
	@Column(name="sr_no")
	private Long sNo;
	
	@Column(name="rank")
	private int rank;
	
	@Column(name="user_display_name")
	private String userDisplayName;
	
	private String username;
	
	@Column(name="diff_previous_rank")
	private int diffPreviousRank;
	
	@Column(name="all_india_rank")
	private int allIndiaRank;
	
	private int achieved; 
	
	private int target;
	
	@Column(name="diff_achieved_target")
	private int diffAchievedTarget;
	
	@Column(name="incentive_amount")
	private String incentiveAmount;
	
	@Column(name="consistency_bonus")
	private String consistencyBonus;
 
	private String reward;
	
	private String location;
	
	@Column(name="cm_fullname")
	private String cmFullname;
	
	@Column(name="scm_fullname")
	private String scmFullname;
	
	@Column(name="rcm_fullname")
	private String rcmFullname;
	
	@Column(name="national_head")
	private String nationalHead;

}
