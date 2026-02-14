package com.hpy.ops360.sampatti.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ScoreCardResponseEntity {
	
	@Id
	private Long srno;
    private Long atmceoMonthlyIncentiveId;
    private String monthDate;
    private String userLoginId;
    private String target;
    private String achieved;
    private Integer rank;
    private String incentiveAmount;
    private Integer roleId;
    private String rankImagePath;
    private String updatedDate;
}
