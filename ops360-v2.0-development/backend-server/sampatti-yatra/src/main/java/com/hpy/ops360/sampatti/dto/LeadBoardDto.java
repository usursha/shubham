package com.hpy.ops360.sampatti.dto;

import lombok.Data;

@Data
public class LeadBoardDto {

	private Integer atmceoIncentiveId;
	private String month;
	private String userLoginId;
	private Integer expectedTarget;
	private Integer actualTransactions;
	private Double incentive;
	private Integer rank;
	private Integer role;
	private String imgUrl;
	private String city;
	private String roleShortName;

}
