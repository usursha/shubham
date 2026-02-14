package com.hpy.ops360.dashboard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CmMtdUptime {

	@Id
	@Column(name = "sr_no")
	@JsonIgnore
	private Long srNo;

	@Column(name = "CM_UPTIME")
	private String cmMtdUptime;

}
