package com.hpy.ops360.sampatti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankChildrenDto{
	
	private Long srno;
    private String fullName;
    private Integer target;
    private Integer achieved;
    private Integer differenceTarget;
    private String incentiveAmount;
    private String consistencyAmount;
    private String reward;
    private String location;
    private String reportsTo;
    private String profilePicture;

}