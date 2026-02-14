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
public class UserIncentiveMonthYearEntity {
	
		@Id
		@Column(name="atmceo_monthly_incentive_id")
	    private Long atmceoMonthlyIncentiveId;

	    @Column(name = "monthyear")
	    private String monthYear;
}
