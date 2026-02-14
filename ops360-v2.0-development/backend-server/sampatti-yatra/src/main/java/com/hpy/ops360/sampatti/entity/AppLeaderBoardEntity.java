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
public class AppLeaderBoardEntity {
	@Id
	private Long id;
	private Long srno;
	private Integer rank;
	private Integer allIndiaRank;
	private String fullName;
	private String username;
	private Integer target;
	private Integer achieved;
	private Integer differenceTarget;
	private String incentiveAmount;
	private String consistencyAmount;
	private String reward;
	private String location;
	private String reportsTo;

}
