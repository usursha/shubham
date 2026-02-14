package com.hpy.ops360.atmservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class CeAssignedAtmEntity {
	
	@Id
	@Column(name="srno")
	private long srno;
	
	@Column(name="atm_id")
	private String atmId;
	
	@Column(name="bank_name")
	private String bankName;
	
	@Column(name="site_name")
	private String siteName;

}
