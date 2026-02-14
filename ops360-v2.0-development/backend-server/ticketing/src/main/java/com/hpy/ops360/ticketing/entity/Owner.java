package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "owner")
public class Owner {

	@Id
	@Column(name = "SR_NO")
	private Long srNo;

	@Column(name = "name")
	private String name;
	
	@Column(name="eta_enable")
	private String etaEnable;
	
	@Column(name="update_enable")
	private String updateEnable;

}
