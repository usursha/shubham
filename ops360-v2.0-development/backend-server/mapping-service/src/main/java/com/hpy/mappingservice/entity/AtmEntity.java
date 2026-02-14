package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AtmEntity {
	
	@Id
	@Column(name="sr_no")
	private Long srNo;
	
	@Column(name="atm_id")
	private String atmId;
	
	@Column(name="status")
	private int status;
	

}
