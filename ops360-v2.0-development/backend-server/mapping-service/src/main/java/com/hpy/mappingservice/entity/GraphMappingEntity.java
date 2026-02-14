package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class GraphMappingEntity {

	@Id
	@Column(name = "sr_no")
	private Integer srNo;
	
	@Column(name = "primary_ce_username")
	private String primaryCeUsername;

	@Column(name = "temp_ce_username")
	private String tempCeUsername;

	@Column(name = "mapped_atm_code")
	private String mappedAtmCode;

}
