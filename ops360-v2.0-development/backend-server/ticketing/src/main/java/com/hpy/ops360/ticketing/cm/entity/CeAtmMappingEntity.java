package com.hpy.ops360.ticketing.cm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class CeAtmMappingEntity {

	@Id
	@Column(name="srno")
	private long srno;
	
	@Column(name="ce_user_id")
	private String ceUserId;
	
	@Column(name="display_name")
	private String displayName;
	
	@Column(name="atm_count")
	private long atmCount;
}
