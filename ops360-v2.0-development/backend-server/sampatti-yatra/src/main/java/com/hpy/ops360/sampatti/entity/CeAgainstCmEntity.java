package com.hpy.ops360.sampatti.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CeAgainstCmEntity {
	
	@Id
	@Column(name="sr_no")
	private Long srNo;
	
	@Column(name="ce_user_id")
	private String ceUserId;
	
	private String fullName;
	
	

}
