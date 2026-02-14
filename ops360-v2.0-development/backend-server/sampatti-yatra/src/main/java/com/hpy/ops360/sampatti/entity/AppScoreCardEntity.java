package com.hpy.ops360.sampatti.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AppScoreCardEntity {
	
	@Id
	private Long id;
	private String monthName;
    private Integer year;
    private Integer target;
    private Integer achieved;
    private Integer rank;
    private Double incentiveAmount;
    private Double totalIncentiveAmount;
    private Double consistencyBonusAmount;


}
