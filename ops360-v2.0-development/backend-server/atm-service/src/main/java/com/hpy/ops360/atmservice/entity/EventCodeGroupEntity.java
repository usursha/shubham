package com.hpy.ops360.atmservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class EventCodeGroupEntity {

	@Id
	@Column(name = "SR_NO")
	private Long srNo;

	@Column(name = "eventcode")
	private String eventCode;

	@Column(name = "eventgroup")
	private String eventGroup;

	@Column(name = "isbreakdown")
	private Integer isBreakdown;

}
