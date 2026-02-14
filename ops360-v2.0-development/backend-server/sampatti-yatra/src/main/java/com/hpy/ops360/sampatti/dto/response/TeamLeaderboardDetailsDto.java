package com.hpy.ops360.sampatti.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.sampatti.entity.TeamLeaderboardRankDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class TeamLeaderboardDetailsDto extends GenericDto {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private Long id;
	private int rank;
    private String userDisplayName;
    private String username;
    private int diffPreviousRank;
    private int allIndiaRank;
    private int achieved;
    private int target;
    private int diffAchievedTarget;
    private String incentive;
    private String consistencyBonus;
    private String reward;
    private String location;
    private String cmFullname;
    private String scmFullname;
    private String rcmFullname;
    private String nationalHead;
    private String profilePic;
    
    

    public static TeamLeaderboardDetailsDto fromEntity(TeamLeaderboardRankDetails entity,String profilePic) {
        return new TeamLeaderboardDetailsDto(
            entity.getRank(),
            entity.getUserDisplayName(),
            entity.getUsername(),
            entity.getDiffPreviousRank(),
            entity.getAllIndiaRank(),
            entity.getAchieved(),
            entity.getTarget(),
            entity.getDiffAchievedTarget(),
            entity.getIncentiveAmount(),
            entity.getConsistencyBonus(),
            entity.getReward(),
            entity.getLocation(),
            entity.getCmFullname(),
            entity.getScmFullname(),
            entity.getRcmFullname(),
            entity.getNationalHead(),
            profilePic
        );
    }



	public TeamLeaderboardDetailsDto(int rank, String userDisplayName, String username, int diffPreviousRank,
			int allIndiaRank, int achieved, int target, int diffAchievedTarget, String incentive, String consistencyBonus,
			String reward, String location, String cmFullname, String scmFullname, String rcmFullname, String nationalHead,
			String profilePic) {
		super();
		this.rank = rank;
		this.userDisplayName = userDisplayName;
		this.username = username;
		this.diffPreviousRank = diffPreviousRank;
		this.allIndiaRank = allIndiaRank;
		this.achieved = achieved;
		this.target = target;
		this.diffAchievedTarget = diffAchievedTarget;
		this.incentive = incentive;
		this.consistencyBonus = consistencyBonus;
		this.reward = reward;
		this.location = location;
		this.cmFullname = cmFullname;
		this.scmFullname = scmFullname;
		this.rcmFullname = rcmFullname;
		this.nationalHead = nationalHead;
		this.profilePic = profilePic;
	}
	
}
