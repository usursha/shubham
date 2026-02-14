package com.hpy.ops360.dashboard.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="atm_ce_mapping")
public class UserReportingHierarchy {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty("sr_no")
	private Long srNo;
	@JsonProperty("cm_user_id")
	private String cmUserId;
	@JsonProperty("rcm_user_id")
	private String rcmUserId;
	@JsonProperty("scm_user_id")
	private String scmUserId;

}
