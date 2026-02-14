package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AtmWithSource {
	
	@Id
	private Long srno;

	@Column(name="atm_code")
	private String atmCode;
	
	private String source;
}
