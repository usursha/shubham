package com.hpy.ops360.sampatti.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardMonthEntity {

	@Id
	private Long srno;
	
    private String displayMonth;

}
