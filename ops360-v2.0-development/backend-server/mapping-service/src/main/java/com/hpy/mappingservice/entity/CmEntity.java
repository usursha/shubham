package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CmEntity {
	
	@Id
	@Column(name="sr_no")
	private Long srNo;
	
	@Column(name="status")
	private int status;
	
	@Column(name="cm_user_id")
	private String cmUserId;

}
