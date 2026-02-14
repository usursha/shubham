package com.hpy.ops360.sampatti.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class LeadBoardDetailsEntity {

	@Id
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