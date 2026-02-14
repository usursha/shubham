package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AllAssignedAtms {
	
	@Id
	@Column(name="srno")
	private Long srno;
	
	@Column(name="atm_id")
	private String atmId;
	
	@Column(name="bank_name")
	private String bankName;
	
	@Column(name="site_name")
	private String siteName;

}
