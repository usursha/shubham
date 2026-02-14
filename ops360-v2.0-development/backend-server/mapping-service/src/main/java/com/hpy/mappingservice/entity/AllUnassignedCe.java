package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AllUnassignedCe {
	
	@Id
	@Column(name="sr_no")
	private Long srno;
	
	@Column(name="username")
	private String username;
	
	@Column(name="display_name")
	private String displayName;
	
	

}
