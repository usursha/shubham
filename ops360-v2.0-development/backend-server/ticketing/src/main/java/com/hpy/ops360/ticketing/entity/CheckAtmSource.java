package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CheckAtmSource {
	
	@Id
	private Long srno;
	
	@Column(name="atm_code")
	private String atmCode;
	
	@Column(name="source_code")
	private int sourceCode;
	

}
