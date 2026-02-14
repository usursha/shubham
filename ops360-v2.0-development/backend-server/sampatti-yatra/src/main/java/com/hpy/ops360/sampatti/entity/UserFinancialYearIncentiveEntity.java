package com.hpy.ops360.sampatti.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserFinancialYearIncentiveEntity {
	
	@Id
	private Long srno;
	
    private String year;
    private Double incentiveAmount;
}
